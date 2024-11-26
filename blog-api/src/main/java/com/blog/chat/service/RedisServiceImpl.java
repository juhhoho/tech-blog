package com.blog.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisMessageListenerContainer messageListenerContainer;
    // 세션(WebSocketSession)과 구독 채널(Set<String>)을 매핑
    // 세션이란 클라이언트와 사용자의 연결로, 채팅 시스템에 접속(ws://localhost:8080/chats)하면 생성됨.
    private final Map<WebSocketSession, Set<String>> sessionSubscriptions = new ConcurrentHashMap<>();
    // 세션과 리스너 객체(Map<String, MessageListener>)를 매핑
    // 리스너 객체는 채널에 구독되어 해당 채널에서 발생한 메세지를 처리하는 객체
    // 하나의 세션에 대해 여러개의 리스너가 존재할 수 있고 각 리스너는 담당한 채널에 대해 메세지 처리
    private final Map<WebSocketSession, Map<String, MessageListener>> sessionListeners = new ConcurrentHashMap<>();


    /**
     * 메세지 발행
     * @param channel
     * @param message
     * 단순히 subscriber에게 메세지를 전달, 보관이 필요하지 않은 경우 적합
     */
    public void publish(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }


    /**
     * 메시지 구독
     * @param channel 채널
     * @param session WebSocketSession
     * 고수준 컨테이너 사용: redis의 RedisMessageListenerContainer를 사용해 메세지 리스너를 관리함
     * - MessageListenerAdapter를 통해 메세지 핸들러 감쌈
     * - addMessageListener를 사용해 특정 채널에 리스너 등록
     * 세션 및 리스너 관리:
     * - sessionSubscriptions: 세션과 구독 채널 맵핑
     * - sessionListeners: 세션과 리스너 객체 맵핑
     * 위 방식을 통해 메세지 리스너를 효율적으로 관리할 수 있으며, 동적 구독 해제가 가능해진다.
     * 이를 통해 여러 채널을 구독하고, 리스너 삭제 등의 기능 확장이 용이하다.
     */
    public void subscribe(String channel, WebSocketSession session) {
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
        messageListenerContainer.addMessageListener(listenerAdapter, new PatternTopic(channel));

        // 4. 세션별 구독 채널 관리: 현재 세션의 구독 채널 집합에 채널 추가
        sessionSubscriptions
                .computeIfAbsent(session, key -> ConcurrentHashMap.newKeySet()) // 세션에 대한 채널 집합이 없으면 새로 생성
                .add(channel); // 채널 추가

        // 5. 세션별 리스너 관리: 현재 세션에 대한 리스너 맵에 채널과 리스너를 매핑
        sessionListeners
                .computeIfAbsent(session, key -> new ConcurrentHashMap<>()) // 세션에 대한 리스너 맵이 없으면 새로 생성
                .put(channel, listener); // 채널과 해당 리스너를 등록
    }

    /**
     * 구독 해제
     * @param channel 채널
     * @param session WebSocketSession
     */
    public void unsubscribe(String channel, WebSocketSession session) {
        // 1. 현재 세션의 구독 채널 집합과 리스너 맵 가져오기
        var subscriptions = sessionSubscriptions.get(session);
        var listeners = sessionListeners.get(session);

        // 2. 세션에 채널이 구독되어 있고, 리스너도 등록되어 있을 경우
        if (subscriptions != null && subscriptions.contains(channel) && listeners != null) {
            // 3. 채널에 연결된 리스너 가져오기
            MessageListener listener = listeners.get(channel);
            if (listener != null) {
                // 4. Redis 메시지 리스너 컨테이너에서 리스너를 제거
                messageListenerContainer.removeMessageListener(listener, new PatternTopic(channel));
                // 5. 구독 채널 집합에서 채널 제거
                subscriptions.remove(channel);
                // 6. 리스너 맵에서 채널-리스너 매핑 제거
                listeners.remove(channel);

                // 7. 세션에 남아있는 구독 채널이 없다면 sessionSubscriptions 및 sessionListeners에서 세션 제거
                if (subscriptions.isEmpty()) {
                    sessionSubscriptions.remove(session); // 세션 구독 정보 삭제
                    sessionListeners.remove(session);    // 세션 리스너 정보 삭제
                }
            }
        }
    }

    /**
     * 모든 구독 해제
     * @param session WebSocketSession
     * 특정 세션이 구독한 모든 채널의 리스너를 제거
     */
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

}