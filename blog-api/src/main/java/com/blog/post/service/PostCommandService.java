package com.blog.post.service;

import com.blog.post.dto.response.PostBlogPostsResponse;
import com.blog.post.dto.response.PostReplyResponse;
import com.blog.post.entity.Post;
import com.blog.post.entity.Reply;
import com.blog.post.repository.PostRepository;
import com.blog.post.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostCommandService {
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;

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
                .replies(new ArrayList<>())
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postBlogPostsResponse);

    }

    public ResponseEntity<PostReplyResponse> postReply(Long postId, String content) {
        Optional<Post> opPost = postRepository.findById(postId);
        if(opPost.isPresent()){

            Reply reply = Reply.builder()
                    .content(content)
                    .post(opPost.get())
                    .build();

            replyRepository.saveAndFlush(reply);

            PostReplyResponse postReplyResponse = PostReplyResponse.builder()
                    .postId(reply.getPost().getId())
                    .replyId(reply.getId())
                    .content(reply.getContent())
                    .build();

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(postReplyResponse);

        }
        return ResponseEntity.notFound().build();

    }

}
