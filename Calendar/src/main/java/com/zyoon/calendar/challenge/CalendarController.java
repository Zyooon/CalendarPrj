package com.zyoon.calendar.challenge;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("calendar")
public class CalendarController {

    @Autowired
    private CalendarService service;

    //멤버 등록
    @PostMapping("join")
    public void joinOneMember(@RequestBody @Valid MemberDto dto){
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new CustomException("이름 오류","값이 비어 있거나 공백입니다.");
        }
        if(!isValidEmail(dto.getEmail())){
            throw new CustomException("이메일 오류","이메일 형식이 올바르지 않습니다.");

        }
        service.addOneMember(dto);
    }

    //리스트 조회
    @GetMapping("list")
    public List<CalendarInfoDto> showCalendarList(@RequestParam(defaultValue = "1")int page,
                                                  @RequestParam(required = false) Optional<Integer> memberId,
                                                  @RequestParam(required = false) Optional<String> time) {

        int pageLimit = 5;
        SearchDto searchDto = new SearchDto(page, pageLimit, memberId, time);

        return service.getPagedCalendarListBySearch(searchDto);
    }

    //하나의 일정 조회
    @GetMapping("detail/{id}")
    public CalendarInfoDto showOneCalendar(@PathVariable("id") String id){
        try {
            int intId = Integer.parseInt(id);
            if (intId <= 0) {
                throw new CustomException("숫자 오류","요청한 값은 0보다 커야합니다.");
            }

            CalendarInfoDto dto = new CalendarInfoDto(intId);
            return service.getOneCalendarById(dto);
        } catch (NumberFormatException e) {
            throw new CustomException("값 오류","요청한 값은 숫자여야 합니다.");
        }
    }

    //일정 등록
    @PostMapping("write")
    public void writeOneCalendar(@RequestBody @Valid CalendarInfoDto dto) {

        service.addOneCalendar(dto);

    }

    //일정 변경
    @PutMapping("update/{id}")
    public void updateOneCalendar(@PathVariable int id, @RequestBody CalendarInfoDto dto){

        dto.setId(id);

        service.updateOneCalendarById(dto);

    }

    //일정 삭제
    @DeleteMapping("delete/{id}")
    public void deleteOneCalendar(@PathVariable int id, @RequestBody CalendarInfoDto dto){

        dto.setId(id);

        service.deleteOneCalendarById(dto);
    }

    //이메일 정합성 검사
    private boolean isValidEmail(String email){
        String validEmail = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (email == null || email.trim().isEmpty()) return false;
        return email.matches(validEmail);
    }
}
