package com.blog.post.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class GetSomeBlogPostsRequest {

    private String title;

    private String description;
}
