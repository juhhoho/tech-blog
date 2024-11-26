package com.blog.chat.config;

import com.blog.chat.chatRoom.ChatRoom;
import com.blog.chat.dto.ChatDto;
import com.blog.chat.dto.WebSocketMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Log4j2
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ChatRoom chatRoom;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        String identifier = (String) session.getAttributes().get("identifier");
        WebSocketMessage webSocketMessage = (WebSocketMessage) objectMapper.readValue(message.getPayload(), WebSocketMessage.class);
        switch (webSocketMessage.getType().getValue()) {
            case "ENTER" -> enterChatRoom(webSocketMessage.getPayload(), session);
            case "TALK" -> sendMessage(identifier, webSocketMessage.getPayload());
            case "EXIT" -> exitChatRoom(webSocketMessage.getPayload(), session);
        }
    }

    /**
     * 메시지 전송
     * @param chatDto ChatDto
     */
    private void sendMessage(String identifier, ChatDto chatDto) {
        log.info("send chatDto : " + chatDto.toString());

        // redis로 메세지 pub
        chatRoom.sendMessage(chatDto);
    }

    /**
     * 채팅방 입장
     * @param chatDto ChatDto
     * @param session 웹소켓 세션
     */
    private void enterChatRoom(ChatDto chatDto, WebSocketSession session) {
        log.info("enter chatRoom : " + chatDto.getChatRoomId());
        chatRoom.enter(chatDto, session);
    }

    /**
     * 채팅방 퇴장
     * @param chatDto ChatDto
     * @param session 웹소켓 세션
     */
    private void exitChatRoom(ChatDto chatDto, WebSocketSession session) {
        log.info("exit chatRoom : " + chatDto.getChatRoomId());
        chatRoom.exit(chatDto, session);
    }
}