package com.blog.post.dto.response;

import com.blog.post.dto.except.UserDtoExceptFeeds;
import com.blog.post.entity.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetBlogFeedsResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime lastBuildTime;
    private int likeCount;
    private int viewCount;
    private UserDtoExceptFeeds user;

    public static GetBlogFeedsResponse convertToGetBlogFeedsResponse(Feed feed) {

        UserDtoExceptFeeds user = UserDtoExceptFeeds.builder()
                .id(feed.getUser().getId())
                .userName(feed.getUser().getUserName())
                .name(feed.getUser().getName())
                .email(feed.getUser().getEmail())
                .role(feed.getUser().getRole())
                .build();

        return new GetBlogFeedsResponse(feed.getId(), feed.getTitle(), feed.getDescription(), feed.getLastBuildTime(), feed.getLikeCount(), feed.getViewCount(), user);
    }
}