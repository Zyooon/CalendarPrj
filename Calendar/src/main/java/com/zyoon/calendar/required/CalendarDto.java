package com.zyoon.calendar.required;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class CalendarDto {
    private int id;
    private String content;
    private String name;
    private String password;
    private Date date;

    public CalendarDto() {
    }
}
