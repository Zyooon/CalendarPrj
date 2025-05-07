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
            System.out.println("연결 성공!");

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

    public List<CalendarDto> selectAllCalendarList(){
        List<CalendarDto> dtoList = new ArrayList<>();
        try (Connection conn = MySqlConnection.getConnection()) {
            System.out.println("연결 성공!");

            String sql = "SELECT * FROM calendarlist";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    CalendarDto dto = new CalendarDto();
                    dto.setId(rs.getInt("id"));
                    dto.setContent(rs.getString("content"));
                    dto.setName(rs.getString("name"));
                    dto.setPassword(rs.getString("password"));
                    dto.setDate(rs.getTimestamp("date")); // DATETIME → Timestamp → Date

                    dtoList.add(dto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtoList;
    }
}
