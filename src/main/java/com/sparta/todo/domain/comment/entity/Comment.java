package com.sparta.todo.domain.comment.entity;

import com.sparta.todo.date.AuditingDate;
import com.sparta.todo.domain.todo.entity.Todo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends AuditingDate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false, name = "user_name")
    private String userName;
    @Column(nullable = false)
    private Long commentWriteUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    public static Comment from(String content, String userName, Long commentWriteUserId, Todo todo) {
        Comment comment = new Comment();
        comment.init(content, userName, commentWriteUserId, todo);
        return comment;
    }

    private void init(String content, String userName, Long commentWriteUserId, Todo todo) {
        this.content = content;
        this.userName = userName;
        this.commentWriteUserId = commentWriteUserId;
        this.todo = todo;
    }

    public void modify(String content) {
        this.content = content;
    }
}
