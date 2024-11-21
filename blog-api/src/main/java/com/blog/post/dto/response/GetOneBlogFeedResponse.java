package com.blog.post.dto.response;

import com.blog.post.dto.except.ReplyDtoExceptFeedAndUser;
import com.blog.post.dto.except.UserDtoExceptFeeds;
import com.blog.post.entity.Feed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class GetOneBlogFeedResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime lastBuildTime;
    private UserDtoExceptFeeds user;
    private List<ReplyDtoExceptFeedAndUser> replies;




    public static GetOneBlogFeedResponse convertToGetOneBlogFeedResponse(Feed feed) {
        List<ReplyDtoExceptFeedAndUser> replies = feed.getReplies().stream()
                .map(reply -> new ReplyDtoExceptFeedAndUser(reply.getId(), reply.getContent()))
                .toList();

        UserDtoExceptFeeds user = UserDtoExceptFeeds.builder()
                .id(feed.getUser().getId())
                .userName(feed.getUser().getUserName())
                .name(feed.getUser().getName())
                .email(feed.getUser().getEmail())
                .role(feed.getUser().getRole())
                .build();

        return new GetOneBlogFeedResponse(feed.getId(), feed.getTitle(), feed.getDescription(), feed.getLastBuildTime(), user,replies);
    }
}
