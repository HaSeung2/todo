package com.sparta.todo.exception;

import lombok.Getter;

@Getter
public class ErrorMessageResponseDto {
    private int errorCode;
    private String message;

    public ErrorMessageResponseDto(ErrorCode errorCode) {
        this.errorCode = errorCode.getHttpStatus();
        this.message = errorCode.getMessage();
    }
}
