package com.sparta.todo.domain.manager.service;

import com.sparta.todo.domain.manager.dto.ManagerResponseDto;
import com.sparta.todo.domain.manager.entity.Manager;
import com.sparta.todo.domain.manager.repository.ManagerRepository;
import com.sparta.todo.domain.todo.entity.Todo;
import com.sparta.todo.domain.todo.repository.TodoRepository;
import com.sparta.todo.domain.user.entity.User;
import com.sparta.todo.domain.user.repository.UserRepository;
import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Transactional
    public ManagerResponseDto managerCreate(Long todoId, Long userId, User user) {
        User paramUser = getUser(userId);
        Todo todo = differentCheckTodoId(todoId, userId, user);

        validatedDuplicatedManager(paramUser.getId(), todo.getId());

        Manager manager = Manager.createManager(paramUser, todo);
        return new ManagerResponseDto(managerRepository.save(manager));
    }

    @Transactional
    public void delete(Long id, User user) {
        isValidManagerAndUserId(id, user);
        managerRepository.deleteById(id);
    }

    private void isValidManagerAndUserId(Long id, User user) {
        if (!managerRepository.existsByUserIdAndUserId(id, user.getId())) {
            throw new CustomException(ErrorCode.NOT_MANAGER);
        }
    }

    private void validatedDuplicatedManager(Long paramUserId, Long todoId) {
        if (managerRepository.existsByUserIdAndTodoId(paramUserId, todoId)) {
            throw new CustomException(ErrorCode.MANAGER_DUPLICATION);
        }
    }

    private Todo differentCheckTodoId(Long id, Long userId, User user) {
        Todo todo = todoRepository.findByTodoId(id);
        validWriteUser(todo, userId, user);
        return todo;
    }

    private User getUser(Long id) {
        return userRepository.findByUserId(id);
    }

    private void validWriteUser(Todo todo, Long id, User user) {
        if (!todo.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.MANAGER_MY_WRITE_TODO);
        }
        if (todo.getUser().getId().equals(id)) {
            throw new CustomException(ErrorCode.NO_MANAGER_MY);
        }
    }
}
