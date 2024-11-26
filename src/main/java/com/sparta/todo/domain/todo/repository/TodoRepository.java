package com.sparta.todo.domain.todo.repository;

import com.sparta.todo.domain.todo.entity.Todo;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<Todo,Long> {

    @Query("select t from Todo t left join fetch t.commentList c where t.id = :id")
    Optional<Todo> findByTodoId(Long id);

    @Query("select t from Todo t left join fetch t.commentList c")
    Page<Todo> findAllTodo(Pageable pageable);
}
