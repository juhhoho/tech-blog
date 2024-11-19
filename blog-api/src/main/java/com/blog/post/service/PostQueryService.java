package com.blog.post.service;
import com.blog.politicsnews.dto.response.PageResult;
import com.blog.post.dto.response.GetBlogPostsResponse;
import com.blog.post.dto.response.GetOneBlogPostResponse;
import com.blog.post.entity.Post;
import com.blog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostQueryService {

    private final PostRepository postRepository;

    // post 페이징 결과 반환
    public PageResult<GetBlogPostsResponse> getBlogPosts(int page, int size) {
        log.info("[PostQueryService - getBlogPosts] page = {}, size = {}", page, size);

        List<Post> blogPosts = postRepository.findAllByOrderByLastBuildTimeDesc();
        int totalElements = blogPosts.size();
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        // 페이지네이션 범위가 올바른지 확인
        if (startIndex >= totalElements) {
            // 범위를 벗어난 경우 빈 리스트 반환
            return new PageResult<>(page, size, totalElements, List.of());
        }

        // 필요한 데이터 추출 및 변환
        List<GetBlogPostsResponse> contents = blogPosts.subList(startIndex, endIndex).stream()
                .map(GetBlogPostsResponse::convertToGetBlogPostsResponse)
                .toList();

        return new PageResult<>(page, size, totalElements, contents);
    }


    // post_id로 특정 포스트 반환
    public ResponseEntity<GetOneBlogPostResponse> getOneBlogPosts(Long postId) {
        log.info("[PostQueryService - getBlogPosts] post_id {}", postId);

        Optional<Post> result = postRepository.findById(postId);
        if(result.isPresent()){
            GetOneBlogPostResponse oneBlogPostResponse = GetOneBlogPostResponse.convertToGetOneBlogPostResponse(result.get());
            return ResponseEntity
                    .ok()
                    .body(oneBlogPostResponse);
        }
        // 데이터가 없는 경우 404 반환
        log.info("[PostQueryService - getBlogPosts] Post with id {} not found", postId);
        return ResponseEntity.notFound().build();
    }


}
