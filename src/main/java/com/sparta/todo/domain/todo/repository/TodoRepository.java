package com.sparta.todo.domain.todo.repository;

import com.sparta.todo.domain.todo.entity.Todo;
import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo,Long> {
    default Todo findByTodoId(Long id){
        return findById(id).orElseThrow(()-> new CustomException(ErrorCode.NOT_TODO_ID));
    }
}
