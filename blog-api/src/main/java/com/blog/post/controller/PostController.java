package com.blog.post.controller;

import com.blog.oauth2.jwt.JWTUtil;
import com.blog.pagination.PageResult;
import com.blog.post.dto.request.GetSomeBlogPostsRequest;
import com.blog.post.dto.request.PaginationRequest;
import com.blog.post.dto.request.PostBlogPostsRequest;
import com.blog.post.dto.request.PostReplyRequest;
import com.blog.post.dto.response.GetBlogPostsResponse;
import com.blog.post.dto.response.GetOneBlogPostResponse;
import com.blog.post.dto.response.PostBlogPostsResponse;
import com.blog.post.dto.response.PostReplyResponse;
import com.blog.post.service.PostApplicationService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JWTUtil jwtUtil;

    @GetMapping("/posts")
    public PageResult<GetBlogPostsResponse> getAllBlogPosts(@Valid PaginationRequest paginationRequest){
        log.info("[PostController - getBlogPosts] paginationRequest = {}", paginationRequest);
        return postApplicationService.getAllBlogPosts(paginationRequest.getPage(), paginationRequest.getSize());
    }

    @PostMapping("/posts")
    public ResponseEntity<PostBlogPostsResponse> postBlogPosts(@Valid @RequestBody PostBlogPostsRequest postBlogPostsRequest,
                                                               HttpServletRequest request){
        log.info("[PostController - postBlogPosts] postBlogPostsRequest = {}, username = {}", postBlogPostsRequest, jwtUtil.getUsernameFromCookies(request));
        return postApplicationService.postBlogPosts(postBlogPostsRequest.getTitle(), postBlogPostsRequest.getDescription(), jwtUtil.getUsernameFromCookies(request));
    }

    @GetMapping("/posts/{post_id}")
    public ResponseEntity<GetOneBlogPostResponse> getOneBlogPost(@PathVariable("post_id") Long postId){
        log.info("[PostController - getBlogPosts] post_id = {}", postId);
        return postApplicationService.getOneBlogPosts(postId);
    }

    @PostMapping("/posts/{post_id}/reply")
    public ResponseEntity<PostReplyResponse> postReply(@PathVariable("post_id") Long postId,
                                                       @Valid @RequestBody PostReplyRequest postReplyRequest,
                                                       HttpServletRequest request){
        log.info("[PostController - postReply] post_id = {}, postReplyRequest = {}, username = {}", postId, postReplyRequest, jwtUtil.getUsernameFromCookies(request));
        return postApplicationService.postReply(postId, postReplyRequest.getContent(), jwtUtil.getUsernameFromCookies(request));
    }

    @GetMapping("/search")
    public PageResult<GetBlogPostsResponse> getSomeBlogPosts(@Valid PaginationRequest paginationRequest,
                                                             @RequestBody GetSomeBlogPostsRequest getSomeBlogPostsRequest){
        log.info("[PostController - getSomeBlogPosts] paginationRequest = {}, getSomeBlogPostsRequest = {}", paginationRequest, getSomeBlogPostsRequest);
        return postApplicationService.getSomeBlogPosts(paginationRequest.getPage(), paginationRequest.getSize(), getSomeBlogPostsRequest.getTitle(), getSomeBlogPostsRequest.getDescription());
    }
}

