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

    @Autowired
    private CalendarRepository repository;

    //멤버 추가
    public void addOneMember(MemberDto dto){
        repository.insertOneMember(dto);
    }

    //일정 추가
    public void addOneCalendar(CalendarInfoDto dto){
        repository.insertOneCalendar(dto);
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
    public LocalDateTime changeStringToLocalDateTime(String dateStr){
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
    public void updateOneCalendarById(CalendarInfoDto dto){

        if(!repository.verifyPasswordById(dto)){
            throw new CustomException("password","비밀번호가 일치하지 않습니다.");
        }
        if(dto.getName() != null && !dto.getName().isEmpty()){
            dto.setMemberId(repository.selectOneMemberIdById(dto));
            repository.updateOneNameById(dto);
        }
        if(dto.getContent() != null && !dto.getContent().isEmpty()){
            repository.updateOneContentById(dto);
        }
    }

    //일정 삭제
    public void deleteOneCalendarById(CalendarInfoDto dto){

        if(repository.verifyPasswordById(dto)){
            System.out.println(dto);
            repository.deleteOneCalendarById(dto);
        }
    }

}
