package com.zyoon.calendar.challenge;

//직접 처리하고 싶은 에러 클래스
public class CustomException extends RuntimeException{
    private final String errorCode;

    public CustomException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
