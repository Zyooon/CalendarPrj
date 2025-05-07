package com.zyoon.calendar.required;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("calendar")
public class CalendarController {

    @Autowired
    private CalendarService service;

    @GetMapping("list")
    public List<CalendarDto> showCalendarList(@RequestParam(required = false) String name, @RequestParam(required = false) Optional<String> updateTime) {


        Date date = null;
        if(updateTime.isPresent()){
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                date = formatter.parse(updateTime.get());
            } catch (ParseException e) {
                System.out.println("날짜가 없네요");
            }
        }

        System.out.println("name : " + name);
        System.out.println("updateTime : " + updateTime);

        CalendarDto dto = new CalendarDto(name, date);


        return service.getAllCalendarList(dto);
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
