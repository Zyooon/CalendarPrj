package com.zyoon.calendar.required;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter @Setter
public class CalendarListDto {
    private int id;
    private String content;
    private String name;
    private String password;
    //Json 변경 시 날짜 포맷 변경
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    public CalendarListDto() {

    }

    public CalendarListDto(String name, Date time) {
        this.name = name;
        this.time = time;
    }

    @Override
    public String toString() {
        return "CalendarListDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", time=" + time +
                '}';
    }
}
