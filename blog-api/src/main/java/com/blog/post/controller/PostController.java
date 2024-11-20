package com.blog.post.controller;

import com.blog.oauth2.jwt.JWTUtil;
import com.blog.pagination.PageResult;
import com.blog.post.dto.request.PostBlogPostsRequest;
import com.blog.post.dto.request.PostReplyRequest;
import com.blog.post.dto.response.GetBlogPostsResponse;
import com.blog.post.dto.response.GetOneBlogPostResponse;
import com.blog.post.dto.response.PostBlogPostsResponse;
import com.blog.post.dto.response.PostReplyResponse;
import com.blog.post.service.PostApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    // 전체 post 조회 + 페이징
    @GetMapping("/posts")
    public PageResult<GetBlogPostsResponse> getAllBlogPosts(
            @RequestParam(defaultValue = "1") @Min(1) @Max(10000) int page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size)
    {
        log.info("[PostController - getBlogPosts] page = {}, size = {}", page, size);
        return postApplicationService.getAllBlogPosts(page, size);
    }

    // 키워드 기반 posts 조회 + 페이징
    @GetMapping("/search")
    public PageResult<GetBlogPostsResponse> getSomeBlogPosts(
            @RequestParam(defaultValue = "1") @Min(1) @Max(10000) int page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int size,
            @RequestParam(defaultValue = "") @NotNull(message = "입력은 비어있을 수 없습니다.") String title,
            @RequestParam(defaultValue = "") @NotNull(message = "입력은 비어있을 수 없습니다.") String description)
    {
        log.info("[PostController - getSomeBlogPosts] page = {}, size = {}, title = {}, size = {}", page, size, title, description);
        return postApplicationService.getSomeBlogPosts(page, size, title, description);
    }

    // post_id 기반 특정 post 조회
    @GetMapping("/posts/{post_id}")
    public ResponseEntity<GetOneBlogPostResponse> getOneBlogPost(
            @PathVariable("post_id") Long postId)
    {
        log.info("[PostController - getBlogPosts] post_id = {}", postId);
        return postApplicationService.getOneBlogPosts(postId);
    }


    // post 작성
    @PostMapping("/posts")
    public ResponseEntity<PostBlogPostsResponse> postBlogPosts(
            @Valid @RequestBody PostBlogPostsRequest postBlogPostsRequest,
            HttpServletRequest request)
    {
        log.info("[PostController - postBlogPosts] postBlogPostsRequest = {}, username = {}", postBlogPostsRequest, jwtUtil.getUsernameFromCookies(request));
        return postApplicationService.postBlogPosts(postBlogPostsRequest.getTitle(), postBlogPostsRequest.getDescription(), jwtUtil.getUsernameFromCookies(request));
    }

    // reply 작성
    @PostMapping("/posts/{post_id}/reply")
    public ResponseEntity<PostReplyResponse> postReply(
            @PathVariable("post_id") Long postId,
            @Valid @RequestBody PostReplyRequest postReplyRequest,
            HttpServletRequest request)
    {
        log.info("[PostController - postReply] post_id = {}, postReplyRequest = {}, username = {}", postId, postReplyRequest, jwtUtil.getUsernameFromCookies(request));
        return postApplicationService.postReply(postId, postReplyRequest.getContent(), jwtUtil.getUsernameFromCookies(request));
    }


}

