package com.zyoon.calendar.challenge;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter @Setter
public class CalendarInfoDto {
    private int id;
    private int memberId;
    private String name;
    private String content;
    private String password;
    //Json 변경 시 날짜 포맷 변경
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDate;

    public CalendarInfoDto() {

    }

    @Override
    public String toString() {
        return "CalendarListDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", password='" + password + '\'' +
                ", time=" + modifyDate +
                '}';
    }
}
