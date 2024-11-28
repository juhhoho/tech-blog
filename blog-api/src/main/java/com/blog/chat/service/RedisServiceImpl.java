package com.blog.chat.service;

import com.blog.auth.entity.BaseUser;
import com.blog.auth.repository.BaseUserRepository;
import com.blog.chat.entity.ChatMessageEntity;
import com.blog.chat.repository.ChatMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* redis에 저장 항목
1. chatLog(List) -> 시간 옵션x, rdb로 이전할 떄 삭제
key: chatLog:chatRoomId:[chatRoomId]
value: [chatLog1, chatLog, ...]
-> lrange [key] [start] [end]

2. chatUser(Set) -> exit하면 삭제됨, 시간에 따른 삭제는 x
key: chatUser:chatRoomId:[chatRoomId]
value: (identifier1, identifier2, ... )
-> smembers [key]
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisMessageListenerContainer messageListenerContainer;
    private final ChatMessageRepository chatMessageRepository;

    // 세션(WebSocketSession)과 구독 채널(Set<String>)을 매핑
    // 세션이란 클라이언트와 사용자의 연결로, 채팅 시스템에 접속(ws://localhost:8080/chats)하면 생성됨.
    private final Map<WebSocketSession, Set<String>> sessionSubscriptions = new ConcurrentHashMap<>();

    // 세션과 리스너 객체(Map<String, MessageListener>)를 매핑
    // 리스너 객체는 채널에 구독되어 해당 채널에서 발생한 메세지를 처리하는 객체
    // 하나의 세션에 대해 여러개의 리스너가 존재할 수 있고 각 리스너는 담당한 채널에 대해 메세지 처리
    private final Map<WebSocketSession, Map<String, MessageListener>> sessionListeners = new ConcurrentHashMap<>();
    private final BaseUserRepository baseUserRepository;


    public void enter(Long chatRoomId, String identifier){
        String userInChatRoomKey = "chatUser:chatRoomId:" + chatRoomId;
        // opsForSet: set 자료형, add: set 자료형에 데이터 삽입
        stringRedisTemplate.opsForSet().add(userInChatRoomKey, identifier);
    }

    public void exit(Long chatRoomId, String identifier){
        String userInChatRoomKey = "chatUser:chatRoomId:" + chatRoomId;
        // opsForSet: set 자료형, remove: set 자료형에 데이터 삽입
        stringRedisTemplate.opsForSet().remove(userInChatRoomKey, identifier);
    }

    public void publish(Long chatRoomId, String identifier, String channel, String message) {
        // message 저장
        System.out.println("message = " + message);
        this.saveChatLog(chatRoomId, identifier, message);

        // 해당 채널에 전파
        stringRedisTemplate.convertAndSend(channel, message);
    }

    public void subscribe(String chatRoom, WebSocketSession session) {
        // 1. Redis 메시지를 수신하고 WebSocket으로 전달하는 MessageListener 생성
        MessageListener listener = (message, pattern) -> {
            // Redis 메시지의 본문을 가져옴
            String messageContent = new String(message.getBody());
            // WebSocket 세션에 메시지 전송
            sendMessageToSession(session, messageContent);
        };

        // 2. MessageListener를 Redis와 연결하기 위해 어댑터 생성
        var listenerAdapter = new MessageListenerAdapter(listener);

        // 3. Redis 메시지 리스너 컨테이너에 Listener와 채널 연결
        messageListenerContainer.addMessageListener(listenerAdapter, new PatternTopic(chatRoom));

        // 4. 세션별 구독 채널 관리: 현재 세션의 구독 채널 집합에 채널 추가
        sessionSubscriptions
                .computeIfAbsent(session, key -> ConcurrentHashMap.newKeySet()) // 세션에 대한 채널 집합이 없으면 새로 생성
                .add(chatRoom); // 채널 추가

        // 5. 세션별 리스너 관리: 현재 세션에 대한 리스너 맵에 채널과 리스너를 매핑
        sessionListeners
                .computeIfAbsent(session, key -> new ConcurrentHashMap<>()) // 세션에 대한 리스너 맵이 없으면 새로 생성
                .put(chatRoom, listener); // 채널과 해당 리스너를 등록
    }

    public void unsubscribe(String chatRoom, WebSocketSession session) {
        // 1. 현재 세션의 구독 채널 집합과 리스너 맵 가져오기
        var subscriptions = sessionSubscriptions.get(session);
        var listeners = sessionListeners.get(session);

        // 2. 세션에 채널이 구독되어 있고, 리스너도 등록되어 있을 경우
        if (subscriptions != null && subscriptions.contains(chatRoom) && listeners != null) {
            // 3. 채널에 연결된 리스너 가져오기
            MessageListener listener = listeners.get(chatRoom);
            if (listener != null) {
                // 4. Redis 메시지 리스너 컨테이너에서 리스너를 제거
                messageListenerContainer.removeMessageListener(listener, new PatternTopic(chatRoom));
                // 5. 구독 채널 집합에서 채널 제거
                subscriptions.remove(chatRoom);
                // 6. 리스너 맵에서 채널-리스너 매핑 제거
                listeners.remove(chatRoom);

                // 7. 세션에 남아있는 구독 채널이 없다면 sessionSubscriptions 및 sessionListeners에서 세션 제거
                if (subscriptions.isEmpty()) {
                    sessionSubscriptions.remove(session); // 세션 구독 정보 삭제
                    sessionListeners.remove(session);    // 세션 리스너 정보 삭제
                }
            }
        }
    }

    public void unsubscribeAll(WebSocketSession session) {
        // 1. 현재 세션의 구독 채널 집합과 리스너 맵 가져오기
        var subscriptions = sessionSubscriptions.get(session);
        var listeners = sessionListeners.get(session);

        // 2. 세션에 구독 채널 및 리스너가 존재하는 경우
        if (subscriptions != null && listeners != null) {
            // 3. 세션이 구독한 모든 채널에 대해 반복 처리
            subscriptions.forEach(channel -> {
                // 4. 채널에 연결된 리스너 가져오기
                MessageListener listener = listeners.get(channel);
                if (listener != null) {
                    // 5. Redis 메시지 리스너 컨테이너에서 리스너 제거
                    messageListenerContainer.removeMessageListener(listener, new PatternTopic(channel));
                }
            });
            // 6. 세션의 구독 채널 및 리스너 정보를 전부 삭제
            sessionSubscriptions.remove(session);
            sessionListeners.remove(session);
        }
    }

    @Transactional
    public void migrateChatLogs() {
        log.info("[RedisServiceImpl - migrateChatLogs]");
        String cursor = "0";
        int batchSize = 100; // RDB에 저장할 배치 크기
        List<ChatMessageEntity> batchEntities = new ArrayList<>();

        do {
            // SCAN으로 키 조회
            Cursor<String> keysCursor = stringRedisTemplate.scan(
                    ScanOptions.scanOptions()
                            .match("chatLog:*") // 패턴 매칭
                            .count(100)         // 한 번에 가져올 키 개수
                            .build()
            );

            while (keysCursor.hasNext()) {
                String key = keysCursor.next();

                // Redis List 데이터 조회
                List<String> messages = stringRedisTemplate.opsForList().range(key, 0, -1);

                if (messages == null || messages.isEmpty()) {
                    continue;
                }

                for (String message : messages) {
                    // JSON 데이터를 ChatMessageEntity로 변환
                    ChatMessageEntity chatMessageEntity = parseJsonToEntity(message, key);

                    if (chatMessageEntity != null) {
                        batchEntities.add(chatMessageEntity);
                    }

                    // 배치 크기 도달 시 저장 및 초기화
                    if (batchEntities.size() >= batchSize) {
                        saveBatchToRDB(batchEntities);
                        batchEntities.clear();
                    }
                }

                // Redis에서 키 삭제
                stringRedisTemplate.delete(key);
            }

            // Cursor ID를 문자열로 저장
            cursor = keysCursor.getId().toString();

        } while (!"0".equals(cursor)); // SCAN 종료 조건

        // 남은 데이터를 저장
        if (!batchEntities.isEmpty()) {
            saveBatchToRDB(batchEntities);
        }
    }


    private void saveBatchToRDB(List<ChatMessageEntity> batchEntities) {
        try {
            // RDB에 배치 저장
            chatMessageRepository.saveAll(batchEntities);
            chatMessageRepository.flush(); // 강제 플러시 (선택적)
        } catch (Exception e) {
            // 저장 실패 처리
            System.err.println("Batch save failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private ChatMessageEntity parseJsonToEntity(String jsonMessage, String redisKey) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON을 Map으로 파싱
            Map<String, Object> messageMap = objectMapper.readValue(jsonMessage, Map.class);

            // "payload" 부분 추출
            Map<String, Object> payload = (Map<String, Object>) messageMap.get("payload");

            // Redis 키에서 roomId와 identifier 추출
            String[] keyParts = redisKey.split(":");
            Long chatRoomId = Long.parseLong(keyParts[2]);
            String identifier = keyParts[4];
            BaseUser user =  baseUserRepository.findByIdentifier(identifier);

            // ChatMessageEntity 생성
            return ChatMessageEntity.builder()
                    .user(user)
                    .chatRoomId(chatRoomId)
                    .chatDateTime(parseChatDateTime((List<Object>) payload.get("chatDateTime")))
                    .message((String) payload.get("message"))
                    .build();
        } catch (Exception e) {
            System.err.println("Failed to parse JSON message: " + jsonMessage);
            e.printStackTrace();
            return null;
        }
    }

    private LocalDateTime parseChatDateTime(List<Object> chatDateTime) {
        // JSON에서 날짜/시간 정보 파싱
        return LocalDateTime.of(
                (int) chatDateTime.get(0),
                (int) chatDateTime.get(1),
                (int) chatDateTime.get(2),
                (int) chatDateTime.get(3),
                (int) chatDateTime.get(4),
                (int) chatDateTime.get(5),
                ((Number) chatDateTime.get(6)).intValue()
        );
    }




    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void sendMessageToSession(WebSocketSession session, String message) {
        try {
            // 1. WebSocket 세션이 열려 있는 경우에만 메시지 전송
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message)); // 텍스트 메시지 생성 후 전송
            } else {
                log.warn("Session is closed: " + session.getId()); // 세션이 닫혀 있으면 경고 로그 출력
            }
        } catch (IOException e) {
            // 2. 메시지 전송 실패 시 에러 로그 출력
            log.error("Failed to send message to session: " + session.getId(), e);
        }
    }

    // pub할 때 사용자의 채팅 로그를 저장
    private void saveChatLog(Long chatRoomId, String identifier, String message) {
        String chatLogInChatRoomKey = "chatLog:chatRoomId:" + chatRoomId + ":identifier:" + identifier;
        // opsForList: list 자료형, rightPush: List 자료형의 맨 끝에 추가
        stringRedisTemplate.opsForList().rightPush(chatLogInChatRoomKey, message);
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
}