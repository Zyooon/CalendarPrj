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

    public void addOneCalendar(CalendarInfoDto dto){
        repository.insertOneCalendar(dto);
    }

    public List<CalendarInfoDto> getAllCalendarListBySearch(SearchDto searchDto){

        verifyCalendarListBySearchTime(searchDto);

        return repository.selectAllCalendarListBySearch(searchDto);
    }

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

    //String 에서 타입 변경
    public LocalDateTime changeStringToLocalDateTime(String dateStr){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate ld = LocalDate.parse(dateStr, fmt);
        return ld.atStartOfDay();
    }

    public CalendarInfoDto getOneCalendarById(CalendarInfoDto dto){
        return repository.selectOneCalendarById(dto);

    }

    @Transactional
    public void updateOneCalendarById(CalendarInfoDto dto){
        if(repository.verifyPasswordById(dto)){
            if(dto.getName() != null && !dto.getName().isEmpty()){
                dto.setMemberId(repository.selectOneMemberIdById(dto));
                repository.updateOneNameById(dto);
            }
            if(dto.getContent() != null && !dto.getContent().isEmpty()){
                repository.updateOneContentById(dto);
            }
        }
    }

    public void deleteOneCalendarById(CalendarInfoDto dto){

        if(repository.verifyPasswordById(dto)){
            System.out.println(dto);
            repository.deleteOneCalendarById(dto);
        }
    }

}
