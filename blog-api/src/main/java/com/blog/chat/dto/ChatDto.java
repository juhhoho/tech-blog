package com.blog.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private final Long chatRoomId;

    // client에게 입력 x(역직렬화 시에 제외)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime chatDateTime;
    private String message;

    // 기본 생성자
    public ChatDto() {
        this.chatRoomId = null;
    }

}
