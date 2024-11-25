package com.blog.post.entity;

import com.blog.auth.entity.BaseUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "blog_feed_reply")
@ToString
@NoArgsConstructor
@Getter
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Feed feed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private BaseUser user;

    @Builder
    public Reply(String content, Feed feed, BaseUser user) {
        this.content = content;
        this.feed = feed;
        this.user = user;
    }
}
