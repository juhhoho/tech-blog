package com.blog.post.service;

import com.blog.pagination.PageResult;
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

    public PageResult<GetBlogPostsResponse> getAllBlogPosts(int page, int size) {
        return postQueryService.getAllBlogPosts(page, size);
    }

    public ResponseEntity<PostBlogPostsResponse> postBlogPosts(String title, String description, String username) {
        return postCommandService.postBlogPosts(title, description, username);
    }

    public ResponseEntity<GetOneBlogPostResponse> getOneBlogPosts(Long postId) {
        return postQueryService.getOneBlogPosts(postId);
    }

    public ResponseEntity<PostReplyResponse> postReply(Long postId, String content, String username) {
        return postCommandService.postReply(postId, content, username);
    }

    public PageResult<GetBlogPostsResponse> getSomeBlogPosts(int page, int size, String title, String description) {
        return postQueryService.getSomeBlogPosts(page, size, title, description);
    }

}
