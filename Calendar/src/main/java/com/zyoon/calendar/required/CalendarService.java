package com.zyoon.calendar.required;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class CalendarService {

    @Autowired
    private CalendarRepository repository;

    public void addOneCalendar(CalendarListDto dto){
        repository.insertOneCalendar(dto);
    }

    public List<CalendarListDto> getAllCalendarListBySearch(CalendarSearchDto dto){

        if(dto.getSearchTime().isPresent()){
            String searchTime = dto.getSearchTime().get();
            if(searchTime.contains("-")){
                String[] splitDate = searchTime.split("-");

                dto.setFirstTime(changeStringToLocalDateTime((splitDate[0])));
                dto.setLastTime(changeStringToLocalDateTime((splitDate[1])));

            }else {
                dto.setFirstTime(changeStringToLocalDateTime((searchTime)));
            }

        }


        return repository.selectAllCalendarListBySearch(dto);
    }

    //String 에서 타입 변경
    public LocalDateTime changeStringToLocalDateTime(String dateStr){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate ld = LocalDate.parse(dateStr, fmt);
        return ld.atStartOfDay();
    }

    public CalendarListDto getOneCalendarById(CalendarListDto dto){
        return repository.selectOneCalendarById(dto);

    }

}
