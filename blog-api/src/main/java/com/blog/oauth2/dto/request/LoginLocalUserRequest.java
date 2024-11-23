package com.blog.oauth2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
@Setter
@AllArgsConstructor
@Builder
public class LoginLocalUserRequest {

    @NotBlank(message = "identifier는 비어있을 수 없습니다.")
    private String identifier;

    @NotBlank(message = "password는 비어있을 수 없습니다.")
    @Size(min = 8, max = 20, message = "password는 8자 이상, 20자 이하로 입력해야 합니다.")
    private String password;
}
