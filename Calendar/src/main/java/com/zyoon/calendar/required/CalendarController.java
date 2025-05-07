package com.zyoon.calendar.required;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("calendar")
public class CalendarController {

    @Autowired
    private CalendarService service;

    @GetMapping("list")
    public String showCalendarList(){

        return "this is my List";
    }

    @GetMapping("detail/{id}")
    public String showOneCalendar(@PathVariable("id") int id){

        return id+"";
    }

    @PostMapping("write")
    public void writeOneCalendar(@RequestBody CalendarDto dto){
        String name = dto.getName();
        String content = dto.getContent();
        String password = dto.getPassword();
        System.out.println("name: " + name);
        System.out.println("content: " + content);
        System.out.println("password: " + password);

        service.writeOneCalendar(dto);


    }

    @GetMapping("update/{id}")
    public void updateOneCalendar(@PathVariable("id") int id){

    }

    @GetMapping("delete/{id}")
    public void deleteOneCalendar(@PathVariable("id") int id){

    }

}
