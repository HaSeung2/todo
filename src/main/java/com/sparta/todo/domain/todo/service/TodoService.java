package com.sparta.todo.domain.todo.service;

import com.sparta.todo.api.WeatherService;
import com.sparta.todo.domain.comment.repository.CommentRepository;
import com.sparta.todo.domain.todo.dto.ModifyDto;
import com.sparta.todo.domain.todo.dto.TodoRequestDto;
import com.sparta.todo.domain.todo.dto.TodoResponseDto;
import com.sparta.todo.domain.todo.entity.Todo;
import com.sparta.todo.domain.todo.repository.TodoRepository;
import com.sparta.todo.domain.user.entity.User;
import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherService weatherService;
    private final CommentRepository commentRepository;

    public List<TodoResponseDto> todoFindAll(Pageable pageable) {
            return todoRepository.findAll(pageable).map(todo ->{
                todo.createCommentsCount(commentRepository.countByTodoId(todo.getId()));
                return new TodoResponseDto(todo);
            }).toList();
    }

    public TodoResponseDto todoCreate(TodoRequestDto reqDto, User user) {
        String weather = weatherService.getWeather();
        Todo todo = Todo.createTodo(reqDto.getTitle(), reqDto.getContent(), user, weather);

        return new TodoResponseDto(todoRepository.save(todo));
    }

    @Transactional
    public void todoModify(Long id, ModifyDto modifyDto, User user) {
        Todo findTodo = getTodo(id);
        findTodo.modify(modifyDto.getTitle(), modifyDto.getContent());
    }

    public void todoDelete(Long id, User user) {
        getTodo(id);
        todoRepository.deleteById(id);
    }

    public Todo todoFindById(Long id) {
        Todo todo = getTodo(id);
        todo.createCommentsCount(commentRepository.countByTodoId(id));
        return todo;
    }

    private Todo getTodo(Long id) {
        return todoRepository.findById(id)
                             .orElseThrow(() -> new CustomException(ErrorCode.NOT_TODO_ID));
    }
}

