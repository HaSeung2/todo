package com.sparta.todo.domain.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ModifyDto {

    @NotBlank(message = "수정하실 제목을 입력해주세요")
    private String title;
    @NotBlank(message = "수정하실 내용을 입력해주세요")
    private String content;
}
