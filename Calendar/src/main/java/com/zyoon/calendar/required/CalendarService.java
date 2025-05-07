package com.zyoon.calendar.required;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {

    @Autowired
    private CalendarRepository repository;

    public void writeOneCalendar(CalendarDto dto){
        repository.writeOneCalendar(dto);
    }

}
