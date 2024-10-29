package com.sparta.todo.domain.user.controller;


import com.sparta.todo.domain.user.dto.JoinRequestDto;
import com.sparta.todo.domain.user.dto.LoginRequestDto;
import com.sparta.todo.domain.user.dto.UserResponseDto;
import com.sparta.todo.domain.user.entity.User;
import com.sparta.todo.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/myInfo")
    public ResponseEntity<UserResponseDto> findMyInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.findById(User.getUser(request)));
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.join(joinRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(
        @Valid @RequestBody LoginRequestDto loginRequestDto) {
        UserResponseDto user = userService.login(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Objects> modify(@PathVariable("id") Long id,
                                          String userName,
                                          HttpServletRequest request) {
        userService.modify(id, userName, User.getUser(request));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id, HttpServletRequest request) {
        userService.delete(id, User.getUser(request));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
