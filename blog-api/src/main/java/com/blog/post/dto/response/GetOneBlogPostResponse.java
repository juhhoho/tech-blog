package com.blog.post.dto.response;

import com.blog.post.dto.ReplyDtoExceptPostAndUser;
import com.blog.post.dto.UserDtoExceptPosts;
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
    private UserDtoExceptPosts user;
    private List<ReplyDtoExceptPostAndUser> replies;


    @Builder
    public GetOneBlogPostResponse(String title, String description, LocalDateTime lastBuildTime, UserDtoExceptPosts user, List<ReplyDtoExceptPostAndUser> replies) {
        this.title = title;
        this.description = description;
        this.lastBuildTime = lastBuildTime;
        this.user = user;
        this.replies = replies;
    }




    public static GetOneBlogPostResponse convertToGetOneBlogPostResponse(Post post) {
        List<ReplyDtoExceptPostAndUser> replies = post.getReplies().stream()
                .map(reply -> new ReplyDtoExceptPostAndUser(reply.getId(), reply.getContent()))
                .toList();
        UserDtoExceptPosts user = new UserDtoExceptPosts(
                post.getUser().getId(),
                post.getUser().getUserName(),
                post.getUser().getName(),
                post.getUser().getEmail(),
                post.getUser().getEmail());


        return new GetOneBlogPostResponse(post.getId(), post.getTitle(), post.getDescription(), post.getLastBuildTime(), user,replies);
    }
}
