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
    public ResponseEntity<String> writeOneCalendar(@RequestBody CalendarInfoDto dto) {

        if(service.addOneCalendar(dto) == 1){
            return ResponseEntity.ok("일정 등록에 성공했습니다.");
        }
        return ResponseEntity.ok("일정 등록에 실패했습니다.");

    }

    @PutMapping("update/{id}")
    public ResponseEntity<String> updateOneCalendar(@PathVariable("id") int id,@RequestBody CalendarInfoDto dto){

        dto.setId(id);

        if(service.updateOneCalendarById(dto) == 1){
            return ResponseEntity.ok("일정 변경에 성공했습니다.");
        }
        return ResponseEntity.ok("일정 변경에 실패했습니다.");
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteOneCalendar(@PathVariable("id") int id,@RequestBody CalendarInfoDto dto){

        dto.setId(id);

        if(service.deleteOneCalendarById(dto) == 1){
            return ResponseEntity.ok("일정 삭제에 성공했습니다.");
        }

        return ResponseEntity.ok("일정 삭제에 실패했습니다.");
    }
}
