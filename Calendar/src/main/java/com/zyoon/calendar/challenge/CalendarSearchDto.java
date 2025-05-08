package com.zyoon.calendar.challenge;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter @Setter
public class CalendarSearchDto {
    private Optional<String> searchName;
    private Optional<String> searchTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime firstTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime  lastTime;

    public CalendarSearchDto() {
    }

    public CalendarSearchDto(Optional<String> searchName, Optional<String> searchTime) {
        this.searchName = searchName;
        this.searchTime = searchTime;
    }

    @Override
    public String toString() {
        return "CalendarSearchDto{" +
                "searchName=" + searchName +
                ", searchTime=" + searchTime +
                ", firstTime=" + firstTime +
                ", lastTime=" + lastTime +
                '}';
    }

    public CalendarSearchDto(Optional<String> searchName, LocalDateTime firstTime, LocalDateTime lastTime) {
        this.searchName = searchName;
        this.firstTime = firstTime;
        this.lastTime = lastTime;
    }
}
