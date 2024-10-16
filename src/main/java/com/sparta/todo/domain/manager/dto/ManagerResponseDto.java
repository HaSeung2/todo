package com.sparta.todo.domain.manager.dto;

import com.sparta.todo.domain.manager.entity.Manager;
import lombok.Getter;

@Getter
public class ManagerResponseDto {
    private Long id;
    private Long todoId;
    private Long userId;
    private String managerUserName;

    public ManagerResponseDto(Manager manager) {
        this.id = manager.getId();
        this.todoId = manager.getTodo().getId();
        this.userId = manager.getUser().getId();
        this.managerUserName = manager.getManagerUserName();
    }
}
