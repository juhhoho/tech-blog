package com.blog.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private final Long chatRoomId;
    private final String identifier;
    private String message;

    // 기본 생성자
    public ChatDto() {
        this.chatRoomId = null;
        this.identifier = null;
    }

}
