package com.blog.post.entity;

import com.blog.oauth2.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blog_post")
@ToString
@NoArgsConstructor
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "lastBuildTime")
    private LocalDateTime lastBuildTime;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Builder
    public Post(String title, String description, LocalDateTime lastBuildTime, User user) {
        this.title = title;
        this.description = description;
        this.lastBuildTime = lastBuildTime;
        this.user = user;
    }

    public void setIdForTest(Long id){
        this.id = id;
    }
}
