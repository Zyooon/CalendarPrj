package com.zyoon.calendar.challenge;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class CalendarInfoDto {
    private int id;
    private int memberId;
    private String name;
    private String content;
    private String password;
    //Json 변경 시 날짜 포맷 변경
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enrollDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDate;

    public CalendarInfoDto() {
    }

    public CalendarInfoDto(int id) {
        this.id = id;
    }
}
