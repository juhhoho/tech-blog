package com.blog.post.service;

import com.blog.politicsnews.dto.response.PageResult;
import com.blog.post.dto.response.GetBlogPostsResponse;
import com.blog.post.dto.response.GetOneBlogPostResponse;
import com.blog.post.dto.response.PostBlogPostsResponse;
import com.blog.post.dto.response.PostReplyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostApplicationService {
    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    public PageResult<GetBlogPostsResponse> getBlogPosts(int page, int size) {
        return postQueryService.getBlogPosts(page, size);
    }

    public ResponseEntity<PostBlogPostsResponse> postBlogPosts(String title, String description) {
        return postCommandService.postBlogPosts(title, description);
    }

    public ResponseEntity<GetOneBlogPostResponse> getOneBlogPosts(Long postId) {
        return postQueryService.getOneBlogPosts(postId);
    }

    public ResponseEntity<PostReplyResponse> postReply(Long postId, String content) {
        return postCommandService.postReply(postId, content);
    }

}
