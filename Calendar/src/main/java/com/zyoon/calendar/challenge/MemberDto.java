package com.zyoon.calendar.challenge;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

//멤버 관련 Dto
@Data
public class MemberDto {
    private int id;
    private String name;
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enrollDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDate;
}
