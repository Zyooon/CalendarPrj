package com.zyoon.calendar.challenge;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class CalendarInfoDto {
    private int id;
    private int memberId;
    private String name;
    @Size(max = 200, message = "내용은 200자 이내여야 합니다.")
    private String content;
    @NotBlank(message = "비밀번호는 필수입니다.")
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
