package com.sparta.todo.domain.comment.controller;


import com.sparta.todo.domain.comment.dto.CommentRequestDto;
import com.sparta.todo.domain.comment.dto.CommentResponseDto;
import com.sparta.todo.domain.comment.service.CommentService;
import com.sparta.todo.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{todoId}")
    public ResponseEntity<List<CommentResponseDto>> findByTodoId(@PathVariable("todoId") Long todoId){
        return ResponseEntity.ok(commentService.findByTodoId(todoId));
    }

    @PostMapping("/{todoId}")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody @Valid CommentRequestDto commentRequestDto, @PathVariable("todoId") Long todoId, HttpServletRequest request){
        CommentResponseDto createComment = commentService.createComment(commentRequestDto,todoId,User.getUser(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(createComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Objects> modifyComment(@PathVariable("id") Long id, String content, HttpServletRequest request){
        commentService.modifyComment(id,content,User.getUser(request));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Objects> deleteComment(@PathVariable("id") Long id, HttpServletRequest request){
        commentService.deleteComment(id,User.getUser(request));
        return ResponseEntity.noContent().build();
    }
}
