package com.zyoon.calendar.required;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class CalendarDto {
    private int id;
    private String content;
    private String name;
    private String password;
    //Json 변경 시 날짜 포맷 변경
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateTime;

    public CalendarDto() {

    }

    public CalendarDto(String name, Date updateTime) {
        this.name = name;
        this.updateTime = updateTime;
    }
}
