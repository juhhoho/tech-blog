package com.blog.politicsnews.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@Setter
public class QueryStatsRequest {
    // 50자
    @NotBlank(message = "입력은 비어있을 수 없습니다.")
    @Size(max = 50, message = "query는 최대 50자를 초과할 수 없습니다.")
    private String query;


    // 1 ~ 10000
    @NotNull(message = "입력은 비어있을 수 없습니다.")
    @PastOrPresent(message = "날짜는 미래일 수 없습니다.")
    private LocalDate date;

}
