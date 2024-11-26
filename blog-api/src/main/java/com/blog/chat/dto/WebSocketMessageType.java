package com.blog.chat.dto;


/**
 * 웹소켓 메시지 타입
 */
public enum WebSocketMessageType {
    ENTER("ENTER"),
    TALK("TALK"),
    EXIT("EXIT"),
    JOIN("JOIN"),
    SUB("SUBSCRIBE"),
    PUB("PUBLISH");

    private final String type;

    WebSocketMessageType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.type;
    }
}
