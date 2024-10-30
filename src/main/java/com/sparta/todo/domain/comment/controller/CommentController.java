package com.sparta.todo.domain.comment.controller;


import com.sparta.todo.config.LoginUser;
import com.sparta.todo.domain.comment.dto.CommentModifyRequestDto;
import com.sparta.todo.domain.comment.dto.CommentRequestDto;
import com.sparta.todo.domain.comment.dto.CommentResponseDto;
import com.sparta.todo.domain.comment.service.CommentService;
import com.sparta.todo.domain.user.entity.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{todoId}")
    public ResponseEntity<List<CommentResponseDto>> findByTodoId(
        @PathVariable("todoId") Long todoId) {
        return ResponseEntity.ok(commentService.findByTodoId(todoId));
    }

    @PostMapping("/{todoId}")
    public ResponseEntity<CommentResponseDto> createComment(
        @RequestBody @Valid CommentRequestDto commentRequestDto,
        @PathVariable("todoId") Long todoId, @LoginUser User user) {
        CommentResponseDto createComment = commentService.createComment(commentRequestDto, todoId,
            user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyComment(@PathVariable("id") Long id,
        @Valid @RequestBody CommentModifyRequestDto commentModifyRequestDto, @LoginUser User user) {
        commentService.modifyComment(id, commentModifyRequestDto.getComment(), user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id, @LoginUser User user) {
        commentService.deleteComment(id, user);
        return ResponseEntity.noContent().build();
    }
}
