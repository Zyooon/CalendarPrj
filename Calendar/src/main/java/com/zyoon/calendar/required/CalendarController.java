package com.zyoon.calendar.required;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("calendar")
class CalendarController {

    @Autowired
    private CalendarService service;

    @GetMapping("list")
    public ResponseEntity<List<CalendarInfoDto>> showCalendarList(@RequestParam(required = false) Optional<String> name, @RequestParam(required = false) Optional<String> time) {

        CalendarSearchDto dto = new CalendarSearchDto(name, time);

        List<CalendarInfoDto> resultList = service.getAllCalendarListBySearch(dto);

        return ResponseEntity.ok(resultList);
    }

    @GetMapping("detail/{id}")
    public ResponseEntity<CalendarInfoDto> showOneCalendar(@PathVariable("id") int id){

        CalendarInfoDto dto = new CalendarInfoDto(id);

        dto = service.getOneCalendarById(dto);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("write")
    public void writeOneCalendar(@RequestBody CalendarInfoDto dto) {

        service.addOneCalendar(dto);

    }

    @PutMapping("update/{id}")
    public void updateOneCalendar(@PathVariable("id") int id,@RequestBody CalendarInfoDto dto){

        dto.setId(id);

        service.updateOneCalendarById(dto);

    }

    @DeleteMapping("delete/{id}")
    public void deleteOneCalendar(@PathVariable("id") int id,@RequestBody CalendarInfoDto dto){

        dto.setId(id);

        service.deleteOneCalendarById(dto);
    }
}
