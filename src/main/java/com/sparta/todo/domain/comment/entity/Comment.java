package com.sparta.todo.domain.comment.entity;

import com.sparta.todo.date.AuditingDate;
import com.sparta.todo.domain.todo.entity.Todo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    public static Comment createComment(String content, String userName, Long commentWriteUserId, Todo todo) {
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
}
