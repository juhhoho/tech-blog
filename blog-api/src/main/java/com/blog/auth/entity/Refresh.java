package com.blog.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Table(name = "refresh_token")
public class Refresh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "refresh")
    private String refresh;

    @Column(name = "expiration")
    private String expiration;

    @Builder
    public Refresh(String username, String refresh, String expiration) {
        this.username = username;
        this.refresh = refresh;
        this.expiration = expiration;
    }
}
