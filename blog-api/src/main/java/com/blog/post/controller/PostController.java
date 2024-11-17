package com.blog.post.controller;

import com.blog.politicsnews.dto.response.PageResult;
import com.blog.post.dto.request.GetBlogPostsRequest;
import com.blog.post.dto.request.PostBlogPostsRequest;
import com.blog.post.dto.response.GetBlogPostsResponse;
import com.blog.post.dto.response.PostBlogPostsResponse;
import com.blog.post.service.PostApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/blog")
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostApplicationService postApplicationService;

    @GetMapping("/posts")
    public PageResult<GetBlogPostsResponse> getBlogPosts(@Valid GetBlogPostsRequest getBlogPostsRequest){
        log.info("[PostController - getBlogPosts] getBlogPostsRequest = {}", getBlogPostsRequest);
        return postApplicationService.getBlogPosts(getBlogPostsRequest.getPage(), getBlogPostsRequest.getSize());
    }

    @PostMapping("/posts")
    public ResponseEntity<PostBlogPostsResponse> postBlogPosts(@RequestBody PostBlogPostsRequest postBlogPostsRequest){
        log.info("[PostController - postBlogPosts] postBlogPostsRequest = {}", postBlogPostsRequest);
        return postApplicationService.postBlogPosts(postBlogPostsRequest.getTitle(), postBlogPostsRequest.getDescription());
    }
}
