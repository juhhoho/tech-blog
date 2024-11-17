package com.blog.post.service;

import com.blog.post.dto.response.PostBlogPostsResponse;
import com.blog.post.entity.Post;
import com.blog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostCommandService {
    private final PostRepository postRepository;

    public ResponseEntity<PostBlogPostsResponse> postBlogPosts(String title, String description) {
        log.info("[PostCommandService - postBlogPosts] title = {}, description = {}", title, description);

        Post newPost = Post.builder()
                .title(title)
                .description(description)
                .lastBuildTime(LocalDateTime.now())
                .build();
        Post savedPost = postRepository.saveAndFlush(newPost);

        PostBlogPostsResponse postBlogPostsResponse = PostBlogPostsResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .description(savedPost.getDescription())
                .lastBuildTime(savedPost.getLastBuildTime())
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postBlogPostsResponse);

    }

}
