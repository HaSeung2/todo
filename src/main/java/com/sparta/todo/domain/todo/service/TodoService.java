package com.sparta.todo.domain.todo.service;

import com.sparta.todo.api.WeatherService;
import com.sparta.todo.domain.todo.dto.ModifyDto;
import com.sparta.todo.domain.todo.dto.TodoRequestDto;
import com.sparta.todo.domain.todo.dto.TodoResponseDto;
import com.sparta.todo.domain.todo.entity.Todo;
import com.sparta.todo.domain.todo.repository.TodoRepository;
import com.sparta.todo.domain.user.entity.User;
import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherService weatherService;

    @Cacheable(cacheNames = "todoList")
    public Page<TodoResponseDto> todoFindAll(Pageable pageable) {
        return todoRepository.findAllTodo(pageable).map(TodoResponseDto::new);
    }

    @Cacheable(value = "todoOne", key = "#id")
    public TodoResponseDto todoFindById(Long id) {
        Todo todo = getTodo(id);
        return new TodoResponseDto(todo);
    }

    public TodoResponseDto todoCreate(TodoRequestDto reqDto, User user) {
        String weather = weatherService.getWeather();
        Todo todo = Todo.createTodo(reqDto.getTitle(), reqDto.getContent(), user, weather);

        return new TodoResponseDto(todoRepository.save(todo));
    }

    @Transactional
    public void todoModify(Long id, ModifyDto modifyDto) {
        Todo findTodo = getTodo(id);
        findTodo.modify(modifyDto.getTitle(), modifyDto.getContent());
    }

    @CacheEvict(value = {"todoList", "todoOne"}, allEntries = true)
    public void todoDelete(Long id) {
        getTodo(id);
        todoRepository.deleteById(id);
    }

    public Todo getTodo(Long id) {
        return todoRepository.findByTodoId(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_TODO_ID));
    }
}

