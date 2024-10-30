package com.sparta.todo.domain.user.dto;

import com.sparta.todo.domain.user.entity.User;
import com.sparta.todo.domain.user.entity.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;
import lombok.ToString;

@Getter
@ToString
public class UserResponseDto {
    private final Long id;
    private final String email;
    private final String userName;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final UserRole userRole;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
        this.userRole = user.getRole();
    }
}
