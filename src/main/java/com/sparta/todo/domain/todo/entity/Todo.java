package com.sparta.todo.domain.todo.entity;

import com.sparta.todo.date.AuditingDate;
import com.sparta.todo.domain.comment.entity.Comment;
import com.sparta.todo.domain.manager.entity.Manager;
import com.sparta.todo.domain.user.entity.User;
import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "todo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Todo extends AuditingDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String weather;

    @Transient
    private int commentCount;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE)
    private List<Manager> manager = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Todo createTodo(String title, String content, User user, String weather) {
        Todo todo = new Todo();
        todo.title = title;
        todo.content = content;
        todo.user = user;
        todo.weather = weather;
        return todo;
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void createCommentsCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isValidWriteUser(Long id) {
        if (! this.getUser().getId().equals(id)) {
            throw new CustomException(ErrorCode.NO_MY_WRITE_TODO);
        }
        return true;
    }
}
