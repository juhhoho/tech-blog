package com.blog.post.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

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

    @Builder
    public Post(String title, String description, LocalDateTime lastBuildTime) {
        this.title = title;
        this.description = description;
        this.lastBuildTime = lastBuildTime;
    }

    public void setIdForTest(Long id){
        this.id = id;
    }
}
