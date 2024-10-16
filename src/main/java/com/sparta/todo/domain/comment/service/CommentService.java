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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository cmtRepo;
    private final TodoRepository todoRepo;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentReqDto, Long todoId, HttpServletRequest req) {
        Todo todo = isValidTodoId(todoId);
        todoRepo.updateCommentCount(todo.getCommentCount()+1, todoId);
        User user = (User)req.getAttribute("user");
        Comment comment = Comment.from(commentReqDto.getContent(),user.getUserName(),user.getId(), todo);
        return new CommentResponseDto(cmtRepo.save(comment));
    }

    public List<CommentResponseDto> findByTodoId(Long todoId) {
        isValidTodoId(todoId);
        return cmtRepo.findByTodoId(todoId).stream().map(CommentResponseDto :: new).toList();
    }

    @Transactional
    public void modifyComment(Long id, String content, HttpServletRequest req) {
        Comment findComment = isValidCommentIdAndUser(id,req);
        findComment.modify(content);
    }

    @Transactional
    public void deleteComment(Long id, HttpServletRequest req) {
        Comment comment = isValidCommentIdAndUser(id,req);
        Todo todo = isValidTodoId(comment.getTodo().getId());
        todoRepo.updateCommentCount(todo.getCommentCount()-1,todo.getId());
        cmtRepo.deleteById(id);
    }

    private Todo isValidTodoId(Long todoId) {
        return todoRepo.findById(todoId).orElseThrow(()-> new CustomException(ErrorCode.NOT_TODO_ID));
    }

    private Comment isValidCommentIdAndUser(Long commentId, HttpServletRequest req) {
        Comment comment = cmtRepo.findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.NOT_COMMENT_ID));
        User user = (User)req.getAttribute("user");
        if(!comment.getCommentWriteUserId().equals(user.getId())) throw new CustomException(ErrorCode.NO_MY_WRITE_COMMENT);
        return comment;
    }
}
