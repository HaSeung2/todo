package com.sparta.todo.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserModifyRequestDto {
    @NotBlank(message = "수정하실 이름을 입력해주세요")
    private String userName;
}
