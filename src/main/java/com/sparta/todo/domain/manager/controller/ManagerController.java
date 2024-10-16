package com.sparta.todo.domain.manager.controller;

import com.sparta.todo.domain.manager.dto.ManagerResponseDto;
import com.sparta.todo.domain.manager.service.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/management")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping()
    public ResponseEntity<ManagerResponseDto> managerCreate(Long todoId, Long userId, HttpServletRequest request){
        ManagerResponseDto responseDto = new ManagerResponseDto(managerService.managerCreate(todoId, userId, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
