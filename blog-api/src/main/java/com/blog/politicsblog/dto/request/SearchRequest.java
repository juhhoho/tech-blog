package com.blog.politicsblog.dto.request;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class SearchRequest {
    // 50자
    @NotBlank(message = "입력은 비어있을 수 없습니다.")
    @Size(max = 50, message = "query는 최대 50자를 초과할 수 없습니다.")
    private String query;


    // 1 ~ 10000
    @NotNull(message = "입력은 비어있을 수 없습니다.")
    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
    @Max(value = 10000, message = "페이지 번호는 10000 이하여야 합니다.")
    private int start;

    // 1 ~ 50
    @NotNull(message = "입력은 비어있을 수 없습니다.")
    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
    @Max(value = 50, message = "페이지 크기는 50 이하여야 합니다.")
    private int display;
}
