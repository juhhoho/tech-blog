package com.blog.post.service;

import com.blog.oauth2.repository.UserRepository;
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
    private final UserRepository userRepository;

    public ResponseEntity<PostBlogPostsResponse> postBlogPosts(String title, String description, String username) {
        log.info("[PostCommandService - postBlogPosts] title = {}, description = {}, username ={}", title, description, username);
        Post newPost = Post.builder()
                .title(title)
                .description(description)
                .lastBuildTime(LocalDateTime.now())
                .user(userRepository.findByUserName(username))
                .build();
        Post savedPost = postRepository.saveAndFlush(newPost);

        PostBlogPostsResponse postBlogPostsResponse = PostBlogPostsResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .description(savedPost.getDescription())
                .lastBuildTime(savedPost.getLastBuildTime())
                .userId(userRepository.findByUserName(username).getId())
                .replies(new ArrayList<>())
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postBlogPostsResponse);

    }

    public ResponseEntity<PostReplyResponse> postReply(Long postId, String content, String username) {
        log.info("[PostCommandService - postReply] postId = {}, content = {}, username ={}", postId, content, username);
        Optional<Post> opPost = postRepository.findById(postId);
        if(opPost.isPresent()){

            Reply reply = Reply.builder()
                    .content(content)
                    .post(opPost.get())
                    .user(userRepository.findByUserName(username))
                    .build();

            replyRepository.saveAndFlush(reply);

            PostReplyResponse postReplyResponse = PostReplyResponse.builder()
                    .postId(reply.getPost().getId())
                    .replyId(reply.getId())
                    .userId(userRepository.findByUserName(username).getId())
                    .content(reply.getContent())
                    .build();

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(postReplyResponse);

        }
        return ResponseEntity.notFound().build();

    }

}
