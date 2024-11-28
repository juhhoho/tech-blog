package com.blog.chat.entity;

import com.blog.auth.entity.BaseUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
@ToString
@NoArgsConstructor
@Getter
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private BaseUser user;

    @Column(name = "chatRoomId")
    private Long chatRoomId;

    @Column(name = "message")
    private String message;

    @Column(name = "chatDateTime")
    private LocalDateTime chatDateTime;

    @Builder
    public ChatMessageEntity(BaseUser user, Long chatRoomId, String message, LocalDateTime chatDateTime) {
        this.user = user;
        this.chatRoomId = chatRoomId;
        this.message = message;
        this.chatDateTime = chatDateTime;
    }
}
