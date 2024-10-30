package com.sparta.todo.domain.comment.dto;

import com.sparta.todo.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import lombok.ToString;

@Getter
@ToString
public class CommentResponseDto {
    private Long id;
    private String userName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long commentWriteUserId;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.userName = comment.getUserName();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.commentWriteUserId = comment.getWriteUserId();
    }
}
