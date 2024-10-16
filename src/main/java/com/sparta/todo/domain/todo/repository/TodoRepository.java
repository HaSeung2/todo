package com.sparta.todo.domain.todo.repository;

import com.sparta.todo.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo,Long> {
    @Modifying
    @Query("update Todo t set t.commentCount = :commentCount where t.id = :id")
    void updateCommentCount(int commentCount, Long id);

    @Query("select t from Todo t order by t.modifiedAt desc limit :limitNum offset :offsetNum")
    List <Todo> findAll(int limitNum, int offsetNum);
}
