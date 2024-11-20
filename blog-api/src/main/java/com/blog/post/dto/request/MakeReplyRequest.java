package com.blog.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MakeReplyRequest {
    // 50자
    @NotBlank(message = "입력은 비어있을 수 없습니다.")
    @Size(min = 1,max = 1000, message = "content은 1자 이상 1000자 이하여야 합니다.")
    private String content;
}
