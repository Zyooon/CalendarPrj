package com.zyoon.calendar.required;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
class CalendarInfoDto {
    private int id;
    private String content;
    private String name;
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
