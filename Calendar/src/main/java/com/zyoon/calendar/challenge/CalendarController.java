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
    public List<CalendarInfoDto> showCalendarList(@RequestParam(defaultValue = "1")int page,
                                                  @RequestParam(required = false) Optional<Integer> memberId,
                                                  @RequestParam(required = false) Optional<String> time) {

        int pageLimit = 5;
        SearchDto searchDto = new SearchDto(page, pageLimit, memberId, time);

        return service.getAllCalendarListBySearch(searchDto);
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

    @PutMapping("update")
    public void updateOneCalendar(@RequestBody CalendarInfoDto dto){

        service.updateOneCalendarById(dto);

    }

    @DeleteMapping("delete")
    public void deleteOneCalendar(@RequestBody CalendarInfoDto dto){

        System.out.println(dto.toString());

        service.deleteOneCalendarById(dto);
    }
}
