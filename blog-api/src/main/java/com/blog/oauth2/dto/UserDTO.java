package com.blog.oauth2.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDTO {

    private String role;
    private String name;
    private String username;

    @Builder
    public UserDTO(String role, String name, String username) {
        this.role = role;
        this.name = name;
        this.username = username;
    }
}