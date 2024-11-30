package com.blog.post.repository.recommend;

import com.blog.post.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Recommend,Long>, RecommendCustomRepository {
}
