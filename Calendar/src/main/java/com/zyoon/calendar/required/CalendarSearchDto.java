package com.zyoon.calendar.required;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
class CalendarSearchDto {
    private Optional<String> searchName;
    private Optional<String> searchTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime firstTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime  lastTime;

    public CalendarSearchDto(Optional<String> searchName, Optional<String> searchTime) {
        this.searchName = searchName;
        this.searchTime = searchTime;
    }
}
