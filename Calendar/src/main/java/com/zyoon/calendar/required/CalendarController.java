package com.zyoon.calendar.required;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("calendar")
public class CalendarController {

    @Autowired
    private CalendarService service;

    @GetMapping("list")
    public List<CalendarDto> showCalendarList(){

        return service.getAllCalendarList();
    }

    @GetMapping("detail/{id}")
    public String showOneCalendar(@PathVariable("id") int id){

        return id+"";
    }

    @PostMapping("write")
    public void writeOneCalendar(@RequestBody CalendarDto dto) {

        service.addOneCalendar(dto);

    }

    @GetMapping("update/{id}")
    public void updateOneCalendar(@PathVariable("id") int id){

    }

    @GetMapping("delete/{id}")
    public void deleteOneCalendar(@PathVariable("id") int id){

    }
}
