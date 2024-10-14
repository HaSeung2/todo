package com.sparta.todo.domain.todo.service;
import com.sparta.todo.domain.todo.dto.ModifyDto;
import com.sparta.todo.domain.todo.dto.TodoRequestDto;
import com.sparta.todo.domain.todo.dto.TodoResponseDto;
import com.sparta.todo.domain.todo.entity.Todo;
import com.sparta.todo.domain.todo.repository.TodoRepository;
import com.sparta.todo.domain.user.entity.User;
import com.sparta.todo.domain.user.entity.UserRole;
import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService{

    private final TodoRepository todoRepository;

    public List<TodoResponseDto> todoFindAll(Pageable pageable) {
        return todoRepository.findAll(pageable).stream().map(TodoResponseDto :: new).toList();
    }

    @Transactional
    public TodoResponseDto todoCreate(TodoRequestDto reqDto,HttpServletRequest request) {
        User user = returnUser(request);
        Todo todo = Todo.from(reqDto.getTitle(),reqDto.getContent(),user);
        return new TodoResponseDto(todoRepository.save(todo));
    }

    @Transactional
    public void todoModify(Long id, ModifyDto modifyDto, HttpServletRequest request) {
        if(checkAdmin(id,request)){
            Todo findTodo = isValidId(id);
            findTodo.modify(modifyDto.getTitle(),modifyDto.getContent());
        }
    }

    @Transactional
    public void todoDelete(Long id, HttpServletRequest request) {
        if(checkAdmin(id,request)) {
            todoRepository.deleteById(id);
        }
    }

    public Todo todoOne(Long id) {
        return isValidId(id);
    }

    private Todo isValidId(Long id) {
       return todoRepository.findById(id).orElseThrow(()-> new CustomException(ErrorCode.NOT_TODO_ID));
    }

    private User returnUser(HttpServletRequest request) {
        return (User)request.getAttribute("user");
    }

    private boolean checkAdmin(Long id, HttpServletRequest request) {
        Todo findTodo = isValidId(id);
        User user = returnUser(request);
        if(!user.getRole().equals(UserRole.ADMIN)) throw  new CustomException(ErrorCode.NOT_ADMIN);
        return true;
    }

}

