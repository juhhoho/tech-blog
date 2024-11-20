package com.blog.post.dto.response;

import com.blog.post.dto.ReplyDtoExceptPostAndUser;
import com.blog.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetBlogPostsResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime lastBuildTime;
    private List<ReplyDtoExceptPostAndUser> replies;

    public static GetBlogPostsResponse convertToGetBlogPostsResponse(Post post) {
        List<ReplyDtoExceptPostAndUser> replies = post.getReplies().stream()
                .map(reply -> new ReplyDtoExceptPostAndUser(reply.getId(), reply.getContent()))
                .toList();

        return new GetBlogPostsResponse(post.getId(), post.getTitle(), post.getDescription(), post.getLastBuildTime(), replies);
    }
}