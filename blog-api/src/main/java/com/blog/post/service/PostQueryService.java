package com.blog.post.service;
import com.blog.politicsnews.dto.response.PageResult;
import com.blog.post.dto.response.GetBlogPostsResponse;
import com.blog.post.entity.Post;
import com.blog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostQueryService {

    private final PostRepository postRepository;

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
                .map(this::createGetBlogPostsResponse)
                .toList();

        // 결과 반환
        return new PageResult<>(page, size, totalElements, contents);
    }

    private GetBlogPostsResponse createGetBlogPostsResponse(Post post) {
        return GetBlogPostsResponse.builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .lastBuildTime(post.getLastBuildTime())
                .build();
    }

}
