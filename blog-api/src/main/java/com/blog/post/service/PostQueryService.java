package com.blog.post.service;
import com.blog.pagination.PageResult;
import com.blog.pagination.PaginateUtils;
import com.blog.post.dto.response.GetBlogPostsResponse;
import com.blog.post.dto.response.GetOneBlogPostResponse;
import com.blog.post.entity.Post;
import com.blog.post.repository.PostRepository;
import com.blog.post.repository.PostSpecRepository;
import com.blog.post.specification.PostSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostQueryService {

    private final PostRepository postRepository;
    private final PostSpecRepository postSpecRepository;

    // post 페이징 결과 반환
    public PageResult<GetBlogPostsResponse> getAllBlogPosts(int page, int size) {
        log.info("[PostQueryService - getAllBlogPosts] page = {}, size = {}", page, size);

        List<Post> blogPosts = postRepository.findAllByOrderByLastBuildTimeDesc();
        return PaginateUtils.paginate(blogPosts, page, size, GetBlogPostsResponse::convertToGetBlogPostsResponse);
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


    // post 페이징 결과 반환
    public PageResult<GetBlogPostsResponse> getSomeBlogPosts(int page, int size, String title, String description) {
        log.info("[PostViewService - getSomeBlogPosts] page = {}, size = {}, title = {}, description = {}", page, size, title, description);

        // Specification을 사용하여 검색 조건 생성
        Specification<Post> spec = Specification.where(null);

        if (title != null && !title.isBlank()) {
            spec = spec.and(PostSpecification.searchTypeTitle(title));
        }
        if (description != null && !description.isBlank()) {
            spec = spec.and(PostSpecification.searchDescription(description));
        }

        // 조건에 따라 검색된 모든 결과 가져오기
        List<Post> blogPosts = postSpecRepository.findAll(spec);

        return PaginateUtils.paginate(blogPosts, page, size, GetBlogPostsResponse::convertToGetBlogPostsResponse);

    }

}
