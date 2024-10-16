package com.sparta.todo.domain.user.entity;

import com.sparta.todo.date.AuditingDate;
import com.sparta.todo.domain.manager.entity.Manager;
import com.sparta.todo.domain.todo.entity.Todo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AuditingDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Todo> todo = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Manager> manager = new ArrayList<>();

    public static User from(String email, String password, String userName, UserRole role) {
        User user = new User();
        user.init(email,password,userName,role);
        return user;
    }

    private void init(String email, String password, String userName, UserRole role) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.role = role;
    }

    public void modify(String userName) {
        this.userName = userName;
    }
}
