package com.sparta.todo.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor// 테스트 코드 용
public class LoginRequestDto {
    @NotBlank(message = "이메일을 입력해야합니다.")
    private String email;
    @NotBlank(message = "password 입력해주세요.")
    private String password;
}
