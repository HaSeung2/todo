package com.sparta.todo.domain.user.repository;

import com.sparta.todo.domain.user.entity.User;
import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    default User findByUserId(Long id) {
           return findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_USER_ID));
    }
}
