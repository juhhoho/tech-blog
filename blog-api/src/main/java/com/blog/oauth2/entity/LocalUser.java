package com.blog.oauth2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "local_user")
@SuperBuilder
public class LocalUser extends  BaseUser {

    @Column(name = "password")
    private String password;
}
