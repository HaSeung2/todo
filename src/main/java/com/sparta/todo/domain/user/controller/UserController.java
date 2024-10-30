package com.sparta.todo.domain.user.controller;


import com.sparta.todo.config.LoginUser;
import com.sparta.todo.domain.user.dto.JoinRequestDto;
import com.sparta.todo.domain.user.dto.LoginRequestDto;
import com.sparta.todo.domain.user.dto.UserModifyRequestDto;
import com.sparta.todo.domain.user.dto.UserResponseDto;
import com.sparta.todo.domain.user.entity.User;
import com.sparta.todo.domain.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<UserResponseDto> findMyInfo(@LoginUser User user) {
        return ResponseEntity.ok(userService.findById(user));
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.join(joinRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        UserResponseDto user = userService.login(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modify(@PathVariable("id") Long id, @Valid @RequestBody UserModifyRequestDto userModifyRequestDto, @LoginUser User user) {
        userService.modify(id, userModifyRequestDto.getUserName(), user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, @LoginUser User user) {
        userService.delete(id, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
