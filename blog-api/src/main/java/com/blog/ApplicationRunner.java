package com.blog;

import com.blog.oauth2.entity.User;
import com.blog.oauth2.repository.UserRepository;
import com.blog.politicsnews.entity.DailyStat;
import com.blog.politicsnews.repository.DailyStatRepository;
import com.blog.post.entity.Feed;
import com.blog.post.entity.Recommend;
import com.blog.post.repository.feed.FeedRepository;
import com.blog.post.repository.recommend.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {

    private final DailyStatRepository dailyStatRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final RecommendRepository recommendRepository;

    @Override
    public void run(String... args) throws Exception {

        DailyStat stat1 = new DailyStat("PYTHON", LocalDateTime.now());

        DailyStat stat2 = new DailyStat("DB", LocalDateTime.now());
        DailyStat stat3 = new DailyStat("DB", LocalDateTime.now());

        DailyStat stat4 = new DailyStat("HTTP", LocalDateTime.now());
        DailyStat stat5 = new DailyStat("HTTP", LocalDateTime.now());
        DailyStat stat6 = new DailyStat("HTTP", LocalDateTime.now());

        DailyStat stat7 = new DailyStat("JAVA", LocalDateTime.now());
        DailyStat stat8 = new DailyStat("JAVA", LocalDateTime.now());
        DailyStat stat9 = new DailyStat("JAVA", LocalDateTime.now());
        DailyStat stat10 = new DailyStat("JAVA", LocalDateTime.now());

        DailyStat stat11 = new DailyStat("KOTLIN", LocalDateTime.now());
        DailyStat stat12 = new DailyStat("KOTLIN", LocalDateTime.now());
        DailyStat stat13 = new DailyStat("KOTLIN", LocalDateTime.now());
        DailyStat stat14 = new DailyStat("KOTLIN", LocalDateTime.now());
        DailyStat stat15 = new DailyStat("KOTLIN", LocalDateTime.now());

        DailyStat stat16 = new DailyStat("SWIFT", LocalDateTime.now());
        DailyStat stat17 = new DailyStat("SWIFT", LocalDateTime.now());
        DailyStat stat18 = new DailyStat("SWIFT", LocalDateTime.now());
        DailyStat stat19 = new DailyStat("SWIFT", LocalDateTime.now());
        DailyStat stat20 = new DailyStat("SWIFT", LocalDateTime.now());
        DailyStat stat21 = new DailyStat("SWIFT", LocalDateTime.now());

        dailyStatRepository.saveAll(List.of(stat1,stat2,stat3,stat4,stat5,stat6,stat7,stat8,stat9
                ,stat10,stat11,stat12,stat13,stat14,stat15,stat16,stat17,stat18,stat19,stat20,stat21));

        User user1 = User.builder()
                .userName("ex_username1")
                .name("정정정준호")
                .email("qwer@naver.com")
                .role("ROLE_USER")
                .build();
        User user2 = User.builder()
                .userName("ex_username2")
                .name("김채은")
                .email("asdf@naver.com")
                .role("ROLE_USER")
                .build();

        userRepository.saveAllAndFlush(List.of(user1, user2));




        Feed feed1 = new Feed("title1", "desc1",LocalDateTime.now(),user1, 0);
        Feed feed2 = new Feed("title2", "desc2",LocalDateTime.now().minusDays(1),user1,0);
        Feed feed3 = new Feed("title3", "desc3",LocalDateTime.now().minusDays(2),user1,0);
        Feed feed4 = new Feed("title4", "desc4",LocalDateTime.now().minusDays(3),user1,0);
        Feed feed5 = new Feed("title5", "desc5",LocalDateTime.now().minusDays(4),user1,1);
        Feed feed6 = new Feed("title6", "desc6",LocalDateTime.now().minusDays(5),user2,1);
        Feed feed7 = new Feed("title7", "desc7",LocalDateTime.now().minusDays(6),user2,1);
        Feed feed8 = new Feed("title8", "desc8",LocalDateTime.now().minusDays(7),user2,2);
        Feed feed9 = new Feed("title9", "desc9",LocalDateTime.now().minusDays(8),user2,2);
        Feed feed10 = new Feed("title10", "desc10",LocalDateTime.now().minusDays(9),user2,2);

        feedRepository.saveAllAndFlush(List.of(feed1, feed2, feed3, feed4, feed5, feed6, feed7, feed8, feed9, feed10));

        Recommend recommend1 = Recommend.builder().feed(feed8).user(user1).build();
        Recommend recommend2 = Recommend.builder().feed(feed9).user(user1).build();
        Recommend recommend3 = Recommend.builder().feed(feed10).user(user1).build();

        Recommend recommend4 = Recommend.builder().feed(feed5).user(user2).build();
        Recommend recommend5 = Recommend.builder().feed(feed6).user(user2).build();
        Recommend recommend6 = Recommend.builder().feed(feed7).user(user2).build();
        Recommend recommend7 = Recommend.builder().feed(feed8).user(user2).build();
        Recommend recommend8 = Recommend.builder().feed(feed9).user(user2).build();
        Recommend recommend9 = Recommend.builder().feed(feed10).user(user2).build();

        recommendRepository.saveAll(List.of(recommend1,recommend2,recommend3,recommend4,recommend5,recommend6,recommend7,recommend8,recommend9));

    }
}
