package com.blog.post.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@ToString
@Setter
@AllArgsConstructor
@Builder
public class MakeBlogFeedRequest {
    // 50자
    @NotBlank(message = "입력은 비어있을 수 없습니다.")
    @Size(max = 50, message = "title은 최대 50자를 초과할 수 없습니다.")
    private String title;

    // 50자
    @NotBlank(message = "입력은 비어있을 수 없습니다.")
    @Size(max = 1000, message = "description은 최대 1000자를 초과할 수 없습니다.")
    private String description;
}
