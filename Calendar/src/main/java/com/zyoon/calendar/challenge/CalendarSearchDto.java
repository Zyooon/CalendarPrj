package com.zyoon.calendar.challenge;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter @Setter
public class CalendarSearchDto {
    private Optional<Integer> searchMemberId;
    private Optional<String> searchTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime firstTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime  lastTime;

    public CalendarSearchDto() {
    }

    public CalendarSearchDto(Optional<Integer> searchMemberId, Optional<String> searchTime) {
        this.searchMemberId = searchMemberId;
        this.searchTime = searchTime;
    }

    @Override
    public String toString() {
        return "CalendarSearchDto{" +
                "searchName=" + searchMemberId +
                ", searchTime=" + searchTime +
                ", firstTime=" + firstTime +
                ", lastTime=" + lastTime +
                '}';
    }

    public CalendarSearchDto(Optional<Integer> searchMemberId, LocalDateTime firstTime, LocalDateTime lastTime) {
        this.searchMemberId = searchMemberId;
        this.firstTime = firstTime;
        this.lastTime = lastTime;
    }
}
