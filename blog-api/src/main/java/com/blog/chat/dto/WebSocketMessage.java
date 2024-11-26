package com.blog.chat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * 웹소켓 메시지 프로토콜
 */
@Getter
@Builder
public class WebSocketMessage{
    private final WebSocketMessageType type;
    private final ChatDto payload;

    @JsonCreator
    public WebSocketMessage(
            @JsonProperty("type") WebSocketMessageType type,
            @JsonProperty("payload") ChatDto payload) {
        this.type = type;
        this.payload = payload;
    }
}

