package com.sparta.todo.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor// 테스트 코드 용
public class JoinRequestDto {
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$",
            message = "비밀번호는 최소 8자리 이상, 영문, 숫자, 특수문자를 1자 이상 포함되어야 합니다.")
    @NotBlank
    private String password;
    @NotBlank
    private String userName;
    private String adminToken = "";
}
