package com.blog.post.entity;

import com.blog.oauth2.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blog_feed")
@ToString
@NoArgsConstructor
@Getter
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "lastBuildTime")
    private LocalDateTime lastBuildTime;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ColumnDefault("0")
    @Column(name = "likeCount", nullable = false)
    private int likeCount;

    @Builder
    public Feed(String title, String description, LocalDateTime lastBuildTime, User user, int likeCount) {
        this.title = title;
        this.description = description;
        this.lastBuildTime = lastBuildTime;
        this.user = user;
        this.likeCount = likeCount;
    }

    public void setIdForTest(Long id){
        this.id = id;
    }
}
