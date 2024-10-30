package com.sparta.todo.domain.manager.controller;

import com.sparta.todo.config.LoginUser;
import com.sparta.todo.domain.manager.dto.ManagerResponseDto;
import com.sparta.todo.domain.manager.service.ManagerService;
import com.sparta.todo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping
    public ResponseEntity<ManagerResponseDto> managerCreate(Long todoId, Long userId, @LoginUser User user) {
        ManagerResponseDto responseDto = managerService.managerCreate(todoId, userId, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ManagerResponseDto> managerDelete(@PathVariable("id") Long id, @LoginUser User user) {
        managerService.delete(id, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
