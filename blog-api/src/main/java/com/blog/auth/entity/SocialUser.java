package com.blog.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "social_user")
@SuperBuilder
public class SocialUser extends  BaseUser {
    @Column(name = "provider")
    private String provider;

    public void changeName(String name){
        this.name = name;
    }
    public void changeEmail(String email){
        this.email = email;
    }
}
