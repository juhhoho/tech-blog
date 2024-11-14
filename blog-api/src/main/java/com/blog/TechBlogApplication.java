package com.blog;

import com.blog.feign.NaverClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {NaverClient.class})
public class TechBlogApplication {

    public static void main(String[] args){
        SpringApplication.run(TechBlogApplication.class);
    }
}