package com.zyoon.calendar.challenge;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("calendar")
public class CalendarController {


    private final CalendarService service;

    @Autowired
    public CalendarController(CalendarService calendarService){
        this.service = calendarService;
    }

    //멤버 등록
    @PostMapping("join")
    public ResponseEntity<String>  joinOneMember(@RequestBody @Valid MemberDto dto){

        if(service.addOneMember(dto) == 1){
            return ResponseEntity.ok("멤버 등록에 성공했습니다.");
        }
        throw new CustomException("등록 오류","멤버 등록에 실패했습니다.");
    }

    //리스트 조회
    @GetMapping("list")
    public ResponseEntity<List<CalendarInfoDto>> showCalendarList(@RequestParam(defaultValue = "1")int page,
                                                                  @RequestParam(defaultValue = "5")int pageLimit ,
                                                                 @RequestParam(required = false) Optional<Integer> memberId,
                                                                 @RequestParam(required = false) Optional<String> time) {

        SearchDto searchDto = new SearchDto(page, pageLimit, memberId, time);

        List<CalendarInfoDto> resultList = service.getPagedCalendarListBySearch(searchDto);

        return ResponseEntity.ok(resultList);
    }

    //하나의 일정 조회
    @GetMapping("detail/{id}")
    public ResponseEntity<CalendarInfoDto> showOneCalendar(@PathVariable("id") String id){
        try {
            int intId = Integer.parseInt(id);
            if (intId <= 0) {
                throw new CustomException("숫자 오류","요청한 값은 0보다 커야합니다.");
            }

            CalendarInfoDto dto = new CalendarInfoDto(intId);
            dto = service.getOneCalendarById(dto);
            return ResponseEntity.ok(dto);


        } catch (NumberFormatException e) {
            throw new CustomException("값 오류","요청한 값은 숫자여야 합니다.");
        }
    }

    //일정 등록
    @PostMapping("write")
    public ResponseEntity<String> writeOneCalendar(@RequestBody @Valid CalendarInfoDto dto) {



        if(service.addOneCalendar(dto) == 1){
            return ResponseEntity.ok("일정 등록에 성공했습니다.");
        }
        throw new CustomException("등록 오류","일정 등록에 실패했습니다.");

    }

    //일정 변경
    @PutMapping("update/{id}")
    public ResponseEntity<String> updateOneCalendar(@PathVariable int id, @RequestBody @Valid CalendarInfoDto dto){

        dto.setId(id);

        if(service.updateOneCalendarById(dto)){
            return ResponseEntity.ok("일정 변경에 성공했습니다.");
        }
        throw new CustomException("변경 오류","일정 변경에 실패했습니다.");

    }

    //일정 삭제
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteOneCalendar(@PathVariable int id, @RequestBody CalendarInfoDto dto){

        dto.setId(id);

        if(service.deleteOneCalendarById(dto) == 1){
            return ResponseEntity.ok("일정 삭제에 성공했습니다.");
        }
        throw new CustomException("삭제 오류","일정 삭제에 실패했습니다.");
    }
}
