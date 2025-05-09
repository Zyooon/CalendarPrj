package com.zyoon.calendar.challenge;

import lombok.Data;

@Data
public class ExceptionDto {
    private final String errorCode;
    private final String message;

    public ExceptionDto(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}