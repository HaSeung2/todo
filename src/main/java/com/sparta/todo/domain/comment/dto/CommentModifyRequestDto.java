package com.sparta.todo.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentModifyRequestDto {
    @NotBlank(message = "수정하실 내용을 입력해주세요.")
    private String comment;
}
