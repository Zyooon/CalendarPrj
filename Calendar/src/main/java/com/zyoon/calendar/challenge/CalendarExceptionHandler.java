package com.zyoon.calendar.challenge;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CalendarExceptionHandler extends RuntimeException{

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionDto> handleBadRequest(CustomException e) {
        return ResponseEntity.badRequest().body(new ExceptionDto(e.getErrorCode(), e.getMessage()));
    }
}
