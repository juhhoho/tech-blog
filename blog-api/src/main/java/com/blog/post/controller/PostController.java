package com.blog.post.controller;

import com.blog.politicsnews.dto.response.PageResult;
import com.blog.post.dto.request.GetBlogPostsRequest;
import com.blog.post.dto.response.GetBlogPostsResponse;
import com.blog.post.service.PostQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/blog")
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostQueryService postQueryService;

    @GetMapping("/posts")
    public PageResult<GetBlogPostsResponse> getBlogPosts(@Valid GetBlogPostsRequest getBlogPostsRequest){
        log.info("[PostController - getBlogPosts] getBlogPostsRequest = {}", getBlogPostsRequest);
        return postQueryService.getBlogPosts(getBlogPostsRequest.getPage(), getBlogPostsRequest.getSize());
    }
}
