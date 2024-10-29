package com.sparta.todo.domain.manager.controller;

import com.sparta.todo.domain.manager.dto.ManagerResponseDto;
import com.sparta.todo.domain.manager.service.ManagerService;
import com.sparta.todo.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping
    public ResponseEntity<ManagerResponseDto> managerCreate(
        Long todoId,
        Long userId,
        HttpServletRequest request
    ) {
        ManagerResponseDto responseDto = new ManagerResponseDto(managerService.managerCreate(todoId,
            userId,
            User.getUser(request)
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ManagerResponseDto> managerDelete(
        @PathVariable("id") Long id,
        HttpServletRequest request
    ) {
        managerService.delete(id, User.getUser(request));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
