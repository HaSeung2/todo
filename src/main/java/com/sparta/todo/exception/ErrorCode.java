package com.sparta.todo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효기간 만료된 토큰"),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED,"지원하지 않는 토큰"),
    NOT_MATCH_LOGIN(HttpStatus.UNAUTHORIZED,"이메일과 비밀번호 틀림"),
    EMAIL_DUPLICATION(HttpStatus.UNAUTHORIZED,"중복된 이메일입니다."),
    NULL_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 존재하지 않습니다."),
    NO_API_DATA(HttpStatus.BAD_REQUEST,"api 데이터가 존재하지 않습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 JWT 서명."),
    INVALID_ENCODE(HttpStatus.BAD_REQUEST, "인코딩 에러"),
    NOT_TODO_ID(HttpStatus.BAD_REQUEST,"존재하지 않는 TODO ID"),
    NO_MANAGER_MY(HttpStatus.BAD_REQUEST,"본인이 쓴 글에 본인을 담당 유저로 배치할 수 없습니다."),
    NO_MY_WRITE_TODO(HttpStatus.BAD_REQUEST,"타인의 일정에 다른 유저를 담당유저로 배치할 수 없습니다."),
    NOT_COMMENT_ID(HttpStatus.BAD_REQUEST,"존재하지 않는 Comment ID"),
    NOT_USER_ID(HttpStatus.BAD_REQUEST,"존재하지 않는 USER ID"),
    DIFFERENT_USER(HttpStatus.FORBIDDEN,"본인 계정만 삭제 및 수정할 수 있습니다."),
    NO_MY_WRITE_COMMENT(HttpStatus.FORBIDDEN,"본인이 쓴 댓글만 삭제 및 수정 할 수 있습니다."),
    MANAGER_DUPLICATION(HttpStatus.FORBIDDEN,"이미 담당 유저로 배치 되어 있는 일정입니다."),
    NOT_ADMIN(HttpStatus.FORBIDDEN,"권한이 없습니다.");

    private final int httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus.value();
        this.message = message;
    }
}
