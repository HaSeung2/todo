package com.sparta.todo.domain.todo.entity;

import com.sparta.todo.date.AuditingDate;
import com.sparta.todo.domain.comment.entity.Comment;
import com.sparta.todo.domain.manager.entity.Manager;
import com.sparta.todo.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column
    private int commentCount;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE)
    private List <Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE)
    private List<Manager> manager = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Todo from(String title, String content, User user, String weather) {
       Todo todo = new Todo();
       todo.init(title,content,user,weather);
       return todo;
    }

    private void init(String title, String content,User user, String weather) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.weather = weather;
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
