package com.sparta.todo.domain.comment.controller;


import com.sparta.todo.domain.comment.dto.CommentRequestDto;
import com.sparta.todo.domain.comment.dto.CommentResponseDto;
import com.sparta.todo.domain.comment.service.CommentService;
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

    private final CommentService cmtService;

    @GetMapping("/{todoId}")
    public ResponseEntity<List<CommentResponseDto>> findByTodoId(@PathVariable("todoId") Long todoId){
        return ResponseEntity.ok(cmtService.findByTodoId(todoId));
    }

    @PostMapping("/{todoId}")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody @Valid CommentRequestDto commentReqDto, @PathVariable("todoId") Long todoId, HttpServletRequest req){
        CommentResponseDto createComment = cmtService.createComment(commentReqDto,todoId,req);
        return ResponseEntity.status(HttpStatus.CREATED).body(createComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Objects> modifyComment(@PathVariable("id") Long id, String content, HttpServletRequest req){
        cmtService.modifyComment(id,content,req);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Objects> deleteComment(@PathVariable("id") Long id, HttpServletRequest req){
        cmtService.deleteComment(id,req);
        return ResponseEntity.noContent().build();
    }
}
