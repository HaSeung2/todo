package com.sparta.todo.domain.manager.entity;

import com.sparta.todo.domain.todo.entity.Todo;
import com.sparta.todo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "manager")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Manager{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String managerUserName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    public static Manager create(User user, Todo todo) {
        Manager manager = new Manager();
        manager.managerUserName = user.getUserName();
        manager.user = user;
        manager.todo = todo;
        return manager;
    }
}
