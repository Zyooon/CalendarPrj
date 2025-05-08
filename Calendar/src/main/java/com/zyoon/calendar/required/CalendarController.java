package com.zyoon.calendar.required;

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
    public List<CalendarListDto> showCalendarList(@RequestParam(required = false) Optional<String> name, @RequestParam(required = false) Optional<String> time) {

        CalendarSearchDto dto = new CalendarSearchDto(name, time);

        System.out.println(dto.toString());

        return service.getAllCalendarListBySearch(dto);
    }

    @GetMapping("detail/{id}")
    public CalendarListDto showOneCalendar(@PathVariable("id") int id){

        CalendarListDto dto = new CalendarListDto();
        dto.setId(id);


        return service.getOneCalendarById(dto);
    }

    @PostMapping("write")
    public void writeOneCalendar(@RequestBody CalendarListDto dto) {

        service.addOneCalendar(dto);

    }

    @PostMapping("update")
    public void updateOneCalendar(@RequestBody CalendarListDto dto){

        System.out.println(dto.toString());

        service.updateOneCalendarById(dto);

    }

    @PostMapping("delete")
    public void deleteOneCalendar(@RequestBody CalendarListDto dto){

        System.out.println(dto.toString());

        service.deleteOneCalendarById(dto);
    }
}
