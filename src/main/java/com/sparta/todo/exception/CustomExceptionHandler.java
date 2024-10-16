package com.sparta.todo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorMessageResponseDto> CustomException(CustomException e) {
        return returnResponse(e.getErrorCode());
    }

    private ResponseEntity<ErrorMessageResponseDto> returnResponse(ErrorCode errorCode) {
        ErrorMessageResponseDto responseDto = new ErrorMessageResponseDto(errorCode);
        return ResponseEntity.status(responseDto.getErrorCode()).body(responseDto);
    }
}
