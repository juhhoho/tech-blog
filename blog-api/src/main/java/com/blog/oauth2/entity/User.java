package com.blog.oauth2.entity;

import com.blog.post.entity.Feed;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "blog_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userName")
    private String userName;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Feed> feeds = new ArrayList<>();


    @Builder
    public User(String userName, String name, String email, String role) {
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public void changeName(String name){
        this.name = name;
    }
    public void changeEmail(String email){
        this.email = email;
    }
}