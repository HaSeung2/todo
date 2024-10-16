package com.sparta.todo.domain.manager.repository;

import com.sparta.todo.domain.manager.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager,Long> {
    Optional<Manager> findByUserIdAndTodoId(Long paramUserId, Long todoId);
}
