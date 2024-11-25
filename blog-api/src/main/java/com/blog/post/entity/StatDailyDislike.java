package com.blog.post.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "stat_daily_dislike")
@ToString
@NoArgsConstructor
@Getter
public class StatDailyDislike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Column(name ="dislikeDateTime")
    private LocalDateTime dislikeDateTime;

    @Builder
    public StatDailyDislike(Feed feed, LocalDateTime dislikeDateTime) {
        this.feed = feed;
        this.dislikeDateTime = dislikeDateTime;
    }
}