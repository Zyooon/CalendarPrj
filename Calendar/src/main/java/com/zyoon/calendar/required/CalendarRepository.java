package com.zyoon.calendar.required;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Repository
public class CalendarRepository {

    public void writeOneCalendar(CalendarDto dto){
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
}
