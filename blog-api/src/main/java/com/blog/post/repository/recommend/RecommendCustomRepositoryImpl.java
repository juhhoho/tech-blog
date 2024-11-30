package com.blog.post.repository.recommend;

import com.blog.auth.entity.BaseUser;
import com.blog.post.entity.Feed;
import com.blog.post.entity.QRecommend;
import com.blog.post.entity.Recommend;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RecommendCustomRepositoryImpl implements RecommendCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Recommend> checkDuplicate(BaseUser user, Feed feed, String type) {
         return Optional.ofNullable(jpaQueryFactory
                .selectFrom(QRecommend.recommend)
                .where(
                        QRecommend.recommend.user.eq(user),
                        QRecommend.recommend.feed.eq(feed),
                        QRecommend.recommend.type.eq(type))
                .fetchOne());

    }
}
