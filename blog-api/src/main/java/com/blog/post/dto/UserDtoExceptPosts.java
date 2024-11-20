package com.blog.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDtoExceptPosts {
    private Long id;
    private String userName;
    private String name;
    private String email;
    private String role;
}
