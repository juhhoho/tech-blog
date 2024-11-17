package com.blog;

import com.blog.politicsnews.entity.DailyStat;
import com.blog.politicsnews.repository.DailyStatRepository;
import com.blog.post.entity.Post;
import com.blog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {

    private final DailyStatRepository dailyStatRepository;
    private final PostRepository postRepository;

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

        Post post1 = new Post("title1", "desc1",LocalDateTime.now());
        Post post2 = new Post("title2", "desc2",LocalDateTime.now().minusDays(1));
        Post post3 = new Post("title3", "desc3",LocalDateTime.now().minusDays(2));
        Post post4 = new Post("title4", "desc4",LocalDateTime.now().minusDays(3));
        Post post5 = new Post("title5", "desc5",LocalDateTime.now().minusDays(4));
        Post post6 = new Post("title6", "desc6",LocalDateTime.now().minusDays(5));
        Post post7 = new Post("title7", "desc7",LocalDateTime.now().minusDays(6));
        Post post8 = new Post("title8", "desc8",LocalDateTime.now().minusDays(7));
        Post post9 = new Post("title9", "desc9",LocalDateTime.now().minusDays(8));
        Post post10 = new Post("title10", "desc10",LocalDateTime.now().minusDays(9));

        postRepository.saveAll(List.of(post1, post2, post3, post4, post5, post6, post7, post8, post9, post10));

    }
}
