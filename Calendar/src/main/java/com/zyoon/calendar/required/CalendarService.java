package com.zyoon.calendar.required;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarService {

    @Autowired
    private CalendarRepository repository;

    public void addOneCalendar(CalendarDto dto){
        repository.insertOneCalendar(dto);
    }

    public List<CalendarDto> getAllCalendarList(){
        return repository.selectAllCalendarList();
    }

}
