package com.sparta.todo.domain.comment.service;

import com.sparta.todo.domain.comment.dto.CommentRequestDto;
import com.sparta.todo.domain.comment.dto.CommentResponseDto;
import com.sparta.todo.domain.comment.entity.Comment;
import com.sparta.todo.domain.comment.repository.CommentRepository;
import com.sparta.todo.domain.todo.entity.Todo;
import com.sparta.todo.domain.todo.repository.TodoRepository;
import com.sparta.todo.domain.user.entity.User;
import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;

    public CommentResponseDto createComment(CommentRequestDto commentReqDto, Long todoId, User user) {
        Todo todo = getTodoById(todoId);
        Comment comment = Comment.createComment(commentReqDto.getContent(),user.getUserName(),user.getId(), todo);
        return new CommentResponseDto(commentRepository.save(comment));
    }

    public List<CommentResponseDto> findByTodoId(Long todoId) {
        getTodoById(todoId);
        return commentRepository.findByTodoId(todoId).stream().map(CommentResponseDto :: new).toList();
    }

    @Transactional
    public void modifyComment(Long id, String content, User user) {
        Comment findComment = getCommentByIdAndUser(id,user);
        findComment.modify(content);
    }

    public void deleteComment(Long id, User user) {
        Comment comment = getCommentByIdAndUser(id,user);
        getTodoById(comment.getTodo().getId());
        commentRepository.deleteById(id);
    }

    private Todo getTodoById(Long todoId) {
        return todoRepository.findById(todoId).orElseThrow(()-> new CustomException(ErrorCode.NOT_TODO_ID));
    }

    private Comment getCommentByIdAndUser(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.NOT_COMMENT_ID));
        comment.validWriteUser(user.getId());
        return comment;
    }
}
