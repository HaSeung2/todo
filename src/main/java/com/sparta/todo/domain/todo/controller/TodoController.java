package com.sparta.todo.domain.todo.controller;

import com.sparta.todo.domain.todo.dto.ModifyDto;
import com.sparta.todo.domain.todo.dto.TodoRequestDto;
import com.sparta.todo.domain.todo.dto.TodoResponseDto;
import com.sparta.todo.domain.todo.entity.Todo;
import com.sparta.todo.domain.todo.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoController {
    private final int pageSize = 10;
    private final TodoService service;

    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> todoFindAll(@RequestParam(defaultValue = "1", value = "nowPage")int nowPage){
        Pageable pageable = PageRequest.of(nowPage-1, pageSize, Sort.Direction.DESC, "modifiedAt");
        List<TodoResponseDto> todo = service.todoFindAll(pageable);
        if(todo.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.status(HttpStatus.OK).body(todo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> todoFindById(@PathVariable("id") Long id){
        Todo todo = service.todoOne(id);
        return ResponseEntity.status(HttpStatus.OK).body(new TodoResponseDto(todo));
    }

    @PostMapping
    public ResponseEntity<TodoResponseDto> todoCreate(@RequestBody @Valid TodoRequestDto reqDto,HttpServletRequest request){
        TodoResponseDto createTodo = service.todoCreate(reqDto,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createTodo);
    }

    @PutMapping("/modify/{id}")
    public void todoModify(@PathVariable("id")Long id, @RequestBody ModifyDto modifyDto){
        service.todoModify(id,modifyDto);
    }

    @DeleteMapping("/delete/{id}")
    public void todoDelete(@PathVariable("id") Long id){
        service.todoDelete(id);
    }
}
