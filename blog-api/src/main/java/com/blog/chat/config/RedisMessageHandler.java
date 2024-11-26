package com.blog.chat.config;

import com.blog.chat.dto.WebSocketMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@RequiredArgsConstructor
public class RedisMessageHandler implements MessageListener {
    private final WebSocketSession session;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Redis 메시지 수신
     * @param message 메시지
     * @param pattern 패턴
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            com.blog.chat.dto.WebSocketMessage webSocketMessage = objectMapper.readValue(message.getBody(), WebSocketMessage.class);
            if(session.isOpen() && !webSocketMessage.getPayload().getIdentifier().equals(session.getAttributes().get("identifier"))){
                session.sendMessage(new TextMessage(new String(message.getBody())));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
