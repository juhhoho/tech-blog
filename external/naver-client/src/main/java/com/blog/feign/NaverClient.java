package com.blog.feign;

import com.blog.NaverNewsResponse;
import com.blog.config.NaverClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naverClient", url = "${external.naver.url}", configuration = NaverClientConfiguration.class)
public interface NaverClient {

    @GetMapping("/v1/search/news.json")
    NaverNewsResponse search(
            @RequestParam("query") String query,
            @RequestParam("start") int page,
            @RequestParam("display") int size);
}
