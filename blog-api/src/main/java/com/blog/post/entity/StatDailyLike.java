package com.blog.post.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "stat_daily_like")
@ToString
@NoArgsConstructor
@Getter
public class StatDailyLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Column(name ="likeDateTime")
    private LocalDateTime likeDateTime;

    @Builder
    public StatDailyLike(Feed feed, LocalDateTime likeDateTime) {
        this.feed = feed;
        this.likeDateTime = likeDateTime;
    }
}
