package com.zyoon.calendar.required;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Repository
class CalendarRepository {
    Calendar kstTime = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));

    public void insertOneCalendar(CalendarInfoDto dto){
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "INSERT INTO calendar_db.calendar_required (name, content, password) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, dto.getName());
                stmt.setString(2, dto.getContent());
                stmt.setString(3, dto.getPassword());

                int rows = stmt.executeUpdate();
                System.out.println(rows + "행 삽입 완료!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<CalendarInfoDto> selectAllCalendarListBySearch(CalendarSearchDto dto){

        List<CalendarInfoDto> dtoList = new ArrayList<>();
        try (Connection conn = MySqlConnection.getConnection()) {

            StringBuilder sql = new StringBuilder("SELECT * FROM calendar_db.calendar_required WHERE 1=1");

            List<Object> list = new ArrayList<>();

            // 조건 추가
            if (!dto.getSearchName().isEmpty()) {
                sql.append(" AND name LIKE ?");
                list.add("%" + dto.getSearchName().get() + "%");
            }

            if (dto.getFirstTime() != null) {
                if(dto.getLastTime() == null){
                    sql.append(" AND DATE(modifyDate) = ?");
                    list.add(dto.getFirstTime());
                }else {
                    sql.append(" AND modifyDate BETWEEN ? AND ?");
                    list.add(dto.getFirstTime());
                    list.add(dto.getLastTime());
                }

            }

            sql.append(" ORDER BY modifyDate DESC");


            try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < list.size(); i++) {
                    stmt.setObject(i + 1, list.get(i));
                }

                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    CalendarInfoDto dtoTemp = new CalendarInfoDto();
                    dtoTemp.setId(rs.getInt("id"));
                    dtoTemp.setContent(rs.getString("content"));
                    dtoTemp.setName(rs.getString("name"));
                    dtoTemp.setPassword(rs.getString("password"));
                    dtoTemp.setEnrollDate(rs.getTimestamp("enrollDate", kstTime).toLocalDateTime());
                    dtoTemp.setModifyDate(rs.getTimestamp("modifyDate", kstTime).toLocalDateTime());
                    dtoList.add(dtoTemp);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtoList;
    }

    public CalendarInfoDto selectOneCalendarById(CalendarInfoDto dto){
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "SELECT * FROM calendar_db.calendar_required WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setObject(1, dto.getId());

                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    dto.setContent(rs.getString("content"));
                    dto.setName(rs.getString("name"));
                    dto.setPassword(rs.getString("password"));
                    dto.setEnrollDate(rs.getTimestamp("enrollDate", kstTime).toLocalDateTime());
                    dto.setModifyDate(rs.getTimestamp("modifyDate", kstTime).toLocalDateTime());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    public void updateOneCalendarById(CalendarInfoDto dto){
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "UPDATE calendar_db.calendar_required SET name = ?, content = ?, modifyDate = NOW() where id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, dto.getName());
                stmt.setString(2, dto.getContent());
                stmt.setInt(3, dto.getId());

                int rows = stmt.executeUpdate();
                System.out.println(rows + "행 변경 완료!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteOneCalendarById(CalendarInfoDto dto){
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "DELETE FROM calendar_db.calendar_required WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, dto.getId());

                int rows = stmt.executeUpdate();
                System.out.println(rows + "행 삭제 완료!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean verifyPasswordById(CalendarInfoDto dto){

        String password = "";
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "SELECT password FROM calendar_db.calendar_required WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setObject(1, dto.getId());

                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    password = rs.getString("password");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return password.equals(dto.getPassword());

    }



}
