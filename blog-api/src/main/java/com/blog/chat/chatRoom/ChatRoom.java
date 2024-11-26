package com.blog.chat.chatRoom;


import com.blog.chat.dto.ChatDto;
import com.blog.chat.dto.WebSocketMessage;
import com.blog.chat.dto.WebSocketMessageType;
import com.blog.chat.service.RedisServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;


@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class ChatRoom {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RedisServiceImpl redisService;

    /**
     * 채팅방 입장
     * @param chatDto ChatDto
     * @param session 웹소켓 세션
     */
    public void enter(ChatDto chatDto, WebSocketSession session) {
        // chatDto에서 chatRoomId를 통해 입장하고자하는 채팅방 확인 후 해당 채팅방 sub
        String channel = "chatRoom: "+chatDto.getChatRoomId();
        redisService.subscribe(channel, session);

        // session에서 id값 get한 뒤 해당 채널(채팅방)에 입장알림 pub
        String identifier = (String) session.getAttributes().get("identifier");
        chatDto.setMessage(identifier + "님이 입장하셨습니다.");
        redisService.publish(channel, getTextMessage(WebSocketMessageType.ENTER, chatDto));
    }

    /**
     * 채팅방 퇴장
     * @param chatDto ChatDto
     * @param session 웹소켓 세션
     */
    public void exit(ChatDto chatDto, WebSocketSession session) {
        String channel = "chatRoom: " + chatDto.getChatRoomId();
        redisService.unsubscribe(channel, session);  // 구독 해제

        String identifier = (String) session.getAttributes().get("identifier");
        chatDto.setMessage(identifier + "님이 퇴장하셨습니다.");
        redisService.publish(channel, getTextMessage(WebSocketMessageType.EXIT, chatDto));  // 퇴장 메시지 발행
    }

    /**
     * 메시지 전송
     * @param chatDto ChatDto
     */
    public void sendMessage(ChatDto chatDto) {
        // chatDto에서 chatRoomId를 통해 입장하고자하는 채팅방 확인 후 해당 채널(채팅방)에 메세지 pub
        String channel = "chatRoom:"+chatDto.getChatRoomId();
        redisService.publish(channel, getTextMessage(WebSocketMessageType.TALK, chatDto));
    }

    /**
     * 메시지 전송
     * @param type 메시지 타입
     * @param chatDto ChatDto
     * @return String
     */
    private String getTextMessage(WebSocketMessageType type, ChatDto chatDto) {
        try {
            return objectMapper.writeValueAsString(new WebSocketMessage(type, chatDto));
        }catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}