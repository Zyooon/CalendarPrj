package com.zyoon.calendar.required;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CalendarRepository {

    public void insertOneCalendar(CalendarDto dto){
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "INSERT INTO calendarlist (content, name, password) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, dto.getContent());
                stmt.setString(2, dto.getName());
                stmt.setString(3, dto.getPassword());

                int rows = stmt.executeUpdate();
                System.out.println(rows + "행 삽입 완료!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<CalendarDto> selectAllCalendarList(CalendarDto Dto){
        List<CalendarDto> dtoList = new ArrayList<>();
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "SELECT * \n" +
                    "FROM calendar_db.calendarlist\n" +
                    "order by updateTime desc";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    CalendarDto dtoTemp = new CalendarDto();
                    dtoTemp.setId(rs.getInt("id"));
                    dtoTemp.setContent(rs.getString("content"));
                    dtoTemp.setName(rs.getString("name"));
                    dtoTemp.setPassword(rs.getString("password"));
                    dtoTemp.setUpdateTime(rs.getTimestamp("updateTime"));

                    dtoList.add(dtoTemp);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtoList;
    }
}
