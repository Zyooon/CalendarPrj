package com.zyoon.calendar.challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CalendarService {

    private final CalendarRepository repository;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository){
        this.repository = calendarRepository;
    }

    //멤버 추가
    public int addOneMember(MemberDto dto){
        return repository.insertOneMember(dto);
    }

    //일정 추가
    public int addOneCalendar(CalendarInfoDto dto){

        return repository.insertOneCalendar(dto);
    }

    //페이징 처리 된 일정 조회
    public List<CalendarInfoDto> getPagedCalendarListBySearch(SearchDto searchDto){

        verifyCalendarListBySearchTime(searchDto);

        return repository.selectPagedCalendarListBySearch(searchDto);
    }

    //시간 정보 검증하여 조건 검색
    private void verifyCalendarListBySearchTime(SearchDto searchDto){
        if(searchDto.getSearchTime().isPresent()){
            String searchTime = searchDto.getSearchTime().get();
            if(searchTime.contains("-")){
                String[] splitDate = searchTime.split("-");

                searchDto.setFirstTime(changeStringToLocalDateTime((splitDate[0])));
                searchDto.setLastTime(changeStringToLocalDateTime((splitDate[1])).plusDays(1));

            }else {
                searchDto.setFirstTime(changeStringToLocalDateTime((searchTime)));
            }
        }
    }

    //String 을 LocalDateTime 으로 변경
    private LocalDateTime changeStringToLocalDateTime(String dateStr){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate ld = LocalDate.parse(dateStr, fmt);
        return ld.atStartOfDay();
    }

    //일정 하나 조회
    public CalendarInfoDto getOneCalendarById(CalendarInfoDto dto){
        return repository.selectOneCalendarById(dto);

    }

    //일정 업데이트. 이름, 내용 각각 업데이트 및 트랜잭션 처리
    @Transactional
    public boolean updateOneCalendarById(CalendarInfoDto dto){
        boolean isUpdateComplete = true;

        if(!repository.verifyPasswordById(dto)){
            throw new CustomException("password","비밀번호가 일치하지 않습니다.");
        }
        if(dto.getName() != null && !dto.getName().isEmpty()){
            dto.setMemberId(repository.selectOneMemberIdById(dto));
            isUpdateComplete = repository.updateOneNameById(dto);
        }
        if(dto.getContent() != null && !dto.getContent().isEmpty()){
            isUpdateComplete = repository.updateOneContentById(dto);
        }

        return isUpdateComplete;
    }

    //일정 삭제
    public int deleteOneCalendarById(CalendarInfoDto dto){

        if(repository.verifyPasswordById(dto)){
            System.out.println(dto);
            return repository.deleteOneCalendarById(dto);
        }else {
            throw new CustomException("비밀번호 오류","비밀번호가 일치하지 않습니다.");
        }
    }

}
