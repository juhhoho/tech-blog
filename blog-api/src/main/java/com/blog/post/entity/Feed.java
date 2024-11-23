package com.blog.post.entity;

import com.blog.oauth2.entity.BaseUser;
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

    @Column(name = "createTime")
    private LocalDateTime createTime;

    @Column(name = "lastBuildTime")
    private LocalDateTime lastBuildTime;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private BaseUser user;

    @ColumnDefault("0")
    @Column(name = "likeCount", nullable = false)
    private int likeCount;

    @ColumnDefault("0")
    @Column(name = "viewCount", nullable = false)
    private int viewCount = 0;

    @Builder
    public Feed(String title, String description, LocalDateTime createTime, LocalDateTime lastBuildTime, BaseUser user) {
        this.title = title;
        this.description = description;
        this.createTime = createTime;
        this.lastBuildTime = lastBuildTime;
        this.user = user;
    }

    //----------------------------------------------------------------------------
    // static -> 인스터스화 x 일 때 사용
    public static void viewCountUp(Feed feed) {
        feed.viewCount++;
    }

    //----------------------------------------------------------------------------
    // non-static -> 인스터스화일 때 사용
    public  void updateFeed(String title, String description, LocalDateTime lastBuildTime){
        this.title = title;
        this.description = description;
        this.lastBuildTime = lastBuildTime;
    }

    public void setIdForTest(Long id){
        this.id = id;
    }
    //----------------------------------------------------------------------------
}
