package com.zyoon.calendar.challenge;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
public class SearchDto {
    private int pageNumber;
    private int pageLimit;
    private Optional<Integer> searchMemberId;
    private Optional<String> searchTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime firstTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime  lastTime;

    public SearchDto(int pageNumber, int pageLimit, Optional<Integer> searchMemberId, Optional<String> searchTime) {
        this.pageNumber = pageNumber;
        this.pageLimit = pageLimit;
        this.searchMemberId = searchMemberId;
        this.searchTime = searchTime;
    }
}
