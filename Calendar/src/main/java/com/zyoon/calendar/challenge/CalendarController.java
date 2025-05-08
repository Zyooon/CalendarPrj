package com.zyoon.calendar.challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("calendar")
public class CalendarController {

    @Autowired
    private CalendarService service;

    @GetMapping("list")
    public List<CalendarInfoDto> showCalendarList(@RequestParam(required = false) Optional<String> name, @RequestParam(required = false) Optional<String> time) {

        CalendarSearchDto dto = new CalendarSearchDto(name, time);

        System.out.println(dto.toString());

        return service.getAllCalendarListBySearch(dto);
    }

    @GetMapping("detail/{id}")
    public CalendarInfoDto showOneCalendar(@PathVariable("id") int id){

        CalendarInfoDto dto = new CalendarInfoDto();
        dto.setId(id);


        return service.getOneCalendarById(dto);
    }

    @PostMapping("write")
    public void writeOneCalendar(@RequestBody CalendarInfoDto dto) {

        service.addOneCalendar(dto);

    }

    @PostMapping("update")
    public void updateOneCalendar(@RequestBody CalendarInfoDto dto){

        System.out.println(dto.toString());

        service.updateOneCalendarById(dto);

    }

    @PostMapping("delete")
    public void deleteOneCalendar(@RequestBody CalendarInfoDto dto){

        System.out.println(dto.toString());

        service.deleteOneCalendarById(dto);
    }
}
