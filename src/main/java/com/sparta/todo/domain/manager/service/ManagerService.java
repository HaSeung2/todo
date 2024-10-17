package com.sparta.todo.domain.manager.service;

import com.sparta.todo.domain.manager.entity.Manager;
import com.sparta.todo.domain.manager.repository.ManagerRepository;
import com.sparta.todo.domain.todo.entity.Todo;
import com.sparta.todo.domain.todo.repository.TodoRepository;
import com.sparta.todo.domain.user.entity.User;
import com.sparta.todo.domain.user.repository.UserRepository;
import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
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
    public Manager managerCreate(Long todoId ,Long userId, HttpServletRequest request) {
        User user = (User)request.getAttribute("user");

        User paramUser = isValidUserID(userId);
        Todo todo = todoIdDifferentCheck(todoId,userId,user);

        managerDuplication(paramUser.getId(),todo.getId());

        Manager manager = Manager.create(paramUser,todo);
        return managerRepository.save(manager);
    }

    @Transactional
    public void delete(Long id, HttpServletRequest request) {
        isValidManagerAndUserId(id,request);
        managerRepository.deleteById(id);
    }

    private void isValidManagerAndUserId(Long id, HttpServletRequest request) {
        User user = (User)request.getAttribute("user");
        managerRepository.findByIdAndUserId(id,user.getId()).orElseThrow(()-> new CustomException(ErrorCode.NOT_MANAGER));
    }


    private void managerDuplication(Long paramUserId, Long todoId) {
        managerRepository.findByUserIdAndTodoId(paramUserId,todoId).ifPresent(a ->{
            throw new CustomException(ErrorCode.MANAGER_DUPLICATION);
        });
    }

    private Todo todoIdDifferentCheck(Long id,Long userId, User user){
        Todo todo = todoRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.NOT_TODO_ID));
        Long todoWriteUserId = todo.getUser().getId();
        Long getUserId = user.getId();

        if(todoWriteUserId.equals(userId)) throw  new CustomException(ErrorCode.NO_MANAGER_MY);
        if(!todoWriteUserId.equals(getUserId)) throw new CustomException(ErrorCode.MANAGER_MY_WRITE_TODO);

        return todo;
    }

    private User isValidUserID(Long id){
        return userRepository.findById(id).orElseThrow(()-> new CustomException(ErrorCode.NOT_USER_ID));
    }
}
