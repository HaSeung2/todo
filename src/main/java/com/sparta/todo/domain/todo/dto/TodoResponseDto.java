package com.sparta.todo.domain.todo.dto;

import com.sparta.todo.domain.todo.entity.Todo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final int commentsCount;

    public TodoResponseDto(Todo todo){
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.createdAt = todo.getCreatedAt();
        this.modifiedAt = todo.getModifiedAt();
        this.commentsCount = todo.getCommentCount();
    }
}
