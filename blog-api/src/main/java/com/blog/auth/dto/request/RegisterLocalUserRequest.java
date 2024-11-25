package com.blog.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
@Setter
@AllArgsConstructor
@Builder
public class RegisterLocalUserRequest {

    @NotBlank(message = "identifier 입력은 비어있을 수 없습니다.")
    @Size(max = 20, message = "id는 최대 20자를 초과할 수 없습니다.")
    private String identifier;

    @NotBlank(message = "name 입력은 비어있을 수 없습니다.")
    @Size(max = 20, message = "name은 최대 20자를 초과할 수 없습니다.")
    private String name;

    @NotBlank(message = "email 입력은 비어있을 수 없습니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    // 나중에 뭐 개발자/디자이너 이런거 등록할 떄 스면 될듯
//    @NotBlank(message = "role 입력은 비어있을 수 없습니다.")
//    @Pattern(regexp = "ROLE_ADMIN|ROLE_USER", message = "role은 ROLE_ADMIN 또는 ROLE_USER만 가능합니다.")
//    private String role;

    @NotBlank(message = "password 입력은 비어있을 수 없습니다.")
    @Size(min = 8, max = 20, message = "password는 8자 이상, 20자 이하로 입력해야 합니다.")
    private String password;

}
