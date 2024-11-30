package com.blog.post.repository.feed;

import com.blog.post.entity.Feed;
import com.blog.post.entity.QFeed;
import com.blog.post.entity.QStatDailyDislike;
import com.blog.post.entity.QStatDailyLike;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class FeedCustomRepositoryImpl implements FeedCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Feed> getAllFeeds(){
        QFeed feed = QFeed.feed;

        return jpaQueryFactory.selectFrom(feed)
                .orderBy(
                        feed.lastBuildTime.desc(),
                        feed.createTime.desc()
                )
                .fetch();
    }

    @Override
    @Transactional
    public void addLikeCount(Feed feed) {
        jpaQueryFactory.update(QFeed.feed)
                .set(QFeed.feed.likeCount, QFeed.feed.likeCount.add(1))
                .where(QFeed.feed.eq(feed))
                .execute();
    }

    @Override
    @Transactional
    public void addDislikeCount(Feed feed) {
        jpaQueryFactory.update(QFeed.feed)
                .set(QFeed.feed.dislikeCount, QFeed.feed.dislikeCount.add(1))
                .where(QFeed.feed.eq(feed))
                .execute();
    }

    @Override
    @Transactional
    public void subLikeCount(Feed feed) {
        jpaQueryFactory.update(QFeed.feed)
                .set(QFeed.feed.likeCount, QFeed.feed.likeCount.subtract(1))
                .where(QFeed.feed.eq(feed))
                .execute();
    }

    @Override
    @Transactional
    public void subDislikeCount(Feed feed) {
        jpaQueryFactory.update(QFeed.feed)
                .set(QFeed.feed.dislikeCount, QFeed.feed.dislikeCount.subtract(1))
                .where(QFeed.feed.eq(feed))
                .execute();

    }

    @Override
    @Transactional
    public void addViewCount(Feed feed) {
        jpaQueryFactory.update(QFeed.feed)
                .set(QFeed.feed.viewCount, QFeed.feed.viewCount.add(1))
                .where(QFeed.feed.eq(feed))
                .execute();
    }

    @Override
    @Transactional
    public void updateFeed(Feed feed, String title, String description, LocalDateTime lasBuildTime) {
        QFeed oldFeed = QFeed.feed;

        jpaQueryFactory.update(oldFeed) // update 쿼리 시작
                .set(oldFeed.title, title)
                .set(oldFeed.description, description)
                .set(oldFeed.lastBuildTime, lasBuildTime)
                .where(oldFeed.eq(feed)) // 조건 설정
                .execute(); // 실행
    }

    @Override
    public List<Feed> keywordSearch(String keyword) {
        return jpaQueryFactory.selectFrom(QFeed.feed)
                .where(
                        likeTitleAndDescAndUser(keyword)
                )
                .orderBy(
                        QFeed.feed.lastBuildTime.desc(),
                        QFeed.feed.likeCount.desc(),
                        QFeed.feed.viewCount.desc()
                )
                .fetch();
    }

    private BooleanExpression likeTitleAndDescAndUser(String keyword) {
        return keyword != null ?
                QFeed.feed.title.likeIgnoreCase("%" + keyword + "%")
                        .or(QFeed.feed.description.likeIgnoreCase("%" + keyword + "%"))
                        .or(QFeed.feed.user.identifier.likeIgnoreCase("%" +keyword + "%"))
                : null;
    }

    @Override
    public List<Feed> getNFeedsByMostLiked(int count) {
        return jpaQueryFactory.selectFrom(QFeed.feed)
                .orderBy(QFeed.feed.likeCount.desc())
                .limit(count)
                .fetch();
    }

    @Override
    public List<Feed> getNFeedsByMostDailyLiked(int count, LocalDate date) {

        QFeed feed = QFeed.feed;
        QStatDailyLike sdl = QStatDailyLike.statDailyLike;

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        return jpaQueryFactory.selectFrom(feed)
                .join(sdl).on(sdl.feed.eq(feed)) // inner join -> feed가 같은 행만 반환
                .where(sdl.likeDateTime.between(startOfDay, endOfDay))
                .groupBy(feed)
                .orderBy(sdl.id.count().desc())
                .limit(count)
                .fetch();
    }

    @Override
    public List<Feed> getNFeedsByMostDisliked(int count) {

        QFeed feed = QFeed.feed;

        return jpaQueryFactory.selectFrom(feed)
                .orderBy(feed.dislikeCount.desc())
                .limit(count)
                .fetch();
    }

    @Override
    public List<Feed> getNFeedsByMostDailyDisliked(int count, LocalDate date) {

        QFeed feed = QFeed.feed;
        QStatDailyDislike sddl = QStatDailyDislike.statDailyDislike;

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23,59,59);

        return jpaQueryFactory.selectFrom(feed)
                .join(sddl).on(sddl.feed.eq(feed))
                .where(sddl.dislikeDateTime.between(startOfDay, endOfDay))
                .groupBy(feed)
                .orderBy(sddl.id.count().desc())
                .limit(count)
                .fetch();
    }

    @Override
    public List<Feed> getNFeedsByMostViewed(int count) {
        QFeed feed = QFeed.feed;

        return jpaQueryFactory.selectFrom(feed)
                .orderBy(feed.viewCount.desc())
                .limit(count)
                .fetch();
    }
}
