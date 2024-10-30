package com.sparta.todo.domain.todo.controller;

import com.sparta.todo.config.LoginUser;
import com.sparta.todo.domain.todo.dto.ModifyDto;
import com.sparta.todo.domain.todo.dto.TodoRequestDto;
import com.sparta.todo.domain.todo.dto.TodoResponseDto;
import com.sparta.todo.domain.todo.entity.Todo;
import com.sparta.todo.domain.todo.service.TodoService;
import com.sparta.todo.domain.user.entity.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoController {

    private final int pageSize = 10;
    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> todoFindAll(
        @RequestParam(defaultValue = "1", value = "nowPage") int nowPage) {
        Pageable pageable = PageRequest.of(nowPage - 1, pageSize, Sort.Direction.DESC,
            "modifiedAt");
        List<TodoResponseDto> todo = todoService.todoFindAll(pageable);
        if (todo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.status(HttpStatus.OK).body(todo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> todoFindById(@PathVariable("id") Long id) {
        Todo todo = todoService.todoFindById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new TodoResponseDto(todo));
    }

    @PostMapping
    public ResponseEntity<TodoResponseDto> todoCreate(@RequestBody @Valid TodoRequestDto reqDto,
        @LoginUser User user) {
        TodoResponseDto createTodo = todoService.todoCreate(reqDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createTodo);
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<Void> todoModify(@PathVariable("id") Long id,
        @Valid @RequestBody ModifyDto modifyDto, @LoginUser User user) {
        todoService.todoModify(id, modifyDto, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> todoDelete(@PathVariable("id") Long id, @LoginUser User user) {
        todoService.todoDelete(id, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
