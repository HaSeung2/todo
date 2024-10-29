package com.sparta.todo.domain.comment.entity;

import com.sparta.todo.date.AuditingDate;
import com.sparta.todo.domain.todo.entity.Todo;
import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends AuditingDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false, name = "user_name")
    private String userName;
    @Column(nullable = false)
    private Long writeUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    public static Comment createComment(
        String content,
        String userName,
        Long commentWriteUserId,
        Todo todo
    ) {
        Comment comment = new Comment();
        comment.content = content;
        comment.userName = userName;
        comment.writeUserId = commentWriteUserId;
        comment.todo = todo;
        return comment;
    }

    public void modify(String content) {
        this.content = content;
    }

    public void validWriteUser(Long id) {
        if (! this.writeUserId.equals(id)) {
            throw new CustomException(ErrorCode.NO_MY_WRITE_COMMENT);
        }
    }
}
