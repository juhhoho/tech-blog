package com.blog.post.entity;

import com.blog.oauth2.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "blog_feed_recommendation")
@ToString
@NoArgsConstructor
@Getter
public class Recommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    public Recommend(User user, Feed feed) {
        this.user = user;
        this.feed = feed;
    }
}
