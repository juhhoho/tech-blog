package com.blog.post.dto.response;

import com.blog.post.dto.ReplyDto;
import com.blog.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetOneBlogPostResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime lastBuildTime;
    private List<ReplyDto> replies;


    @Builder
    public GetOneBlogPostResponse(String title, String description, LocalDateTime lastBuildTime, List<ReplyDto> replies) {
        this.title = title;
        this.description = description;
        this.lastBuildTime = lastBuildTime;
        this.replies = replies;
    }




    public static GetOneBlogPostResponse convertToGetOneBlogPostResponse(Post post) {
        List<ReplyDto> replies = post.getReplies().stream()
                .map(reply -> new ReplyDto(reply.getId(), reply.getContent()))
                .toList();

        return new GetOneBlogPostResponse(post.getId(), post.getTitle(), post.getDescription(), post.getLastBuildTime(), replies);
    }
}
