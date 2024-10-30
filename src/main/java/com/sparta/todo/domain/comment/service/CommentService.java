package com.sparta.todo.domain.comment.service;

import com.sparta.todo.domain.comment.dto.CommentRequestDto;
import com.sparta.todo.domain.comment.dto.CommentResponseDto;
import com.sparta.todo.domain.comment.entity.Comment;
import com.sparta.todo.domain.comment.repository.CommentRepository;
import com.sparta.todo.domain.todo.entity.Todo;
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

    public CommentResponseDto createComment(CommentRequestDto commentReqDto, Todo todo, User user) {
        Comment comment = Comment.createComment(commentReqDto.getContent(),user.getUserName(),user.getId(), todo);
        return new CommentResponseDto(commentRepository.save(comment));
    }

    public List<CommentResponseDto> findByTodoId(Todo todo) {
        return commentRepository.findByTodoId(todo.getId()).stream().map(CommentResponseDto :: new).toList();
    }

    @Transactional
    public void modifyComment(Long id, String content, User user) {
        Comment comment = getCommentById(id);
        validWriteUser(comment, user.getId());
        comment.modify(content);
    }

    public void deleteComment(Long id, User user) {
        validWriteUser(getCommentById(id), user.getId());
        commentRepository.deleteById(id);
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.NOT_COMMENT_ID));
    }

    private void validWriteUser(Comment comment, Long id) {
        if (! comment.getWriteUserId().equals(id)) {
            throw new CustomException(ErrorCode.NO_MY_WRITE_COMMENT);
        }
    }
}
