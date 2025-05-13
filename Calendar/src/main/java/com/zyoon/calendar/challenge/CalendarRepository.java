package com.zyoon.calendar.challenge;

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

    //멤버 추가
    public void insertOneMember(MemberDto dto){
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "INSERT INTO calendar_db.member (name, email) VALUES (?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, dto.getName());
                stmt.setString(2, dto.getEmail());

                int rows = stmt.executeUpdate();
                System.out.println(rows + "행 삽입 완료!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //하나의 일정 추가 
    public void insertOneCalendar(CalendarInfoDto dto){
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "INSERT INTO calendar_db.calendar_challenge (memberId, content, password) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, dto.getMemberId());
                stmt.setString(2, dto.getContent());
                stmt.setString(3, dto.getPassword());

                int rows = stmt.executeUpdate();
                System.out.println(rows + "행 삽입 완료!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //조건에 맞는 총 리스트 갯수 조회 -> 현재 사용 안함
    public int selectCalendarListCountBySearch(SearchDto dto){

        List<CalendarInfoDto> dtoList = new ArrayList<>();
        try (Connection conn = MySqlConnection.getConnection()) {

            StringBuilder sql = new StringBuilder("SELECT count(*) AS count \n" +
                    "FROM calendar_db.calendar_challenge AS c \n" +
                    "WHERE id = id");

            List<Object> list = new ArrayList<>();

            // 조건 추가
            if (!dto.getSearchMemberId().isEmpty()) {
                sql.append(" AND memberId = ?");
                list.add(dto.getSearchMemberId().get());
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

            try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < list.size(); i++) {
                    stmt.setObject(i + 1, list.get(i));
                }

                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    return rs.getInt("count");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return 0;
    }

    //페이징 된 리스트 조회
    public List<CalendarInfoDto> selectPagedCalendarListBySearch(SearchDto searchDto){

        List<CalendarInfoDto> dtoList = new ArrayList<>();
        try (Connection conn = MySqlConnection.getConnection()) {

            StringBuilder sql = new StringBuilder("SELECT c.id,c.memberId ,name, content,c.enrollDate, c.modifyDate \n" +
                    "FROM calendar_db.calendar_challenge AS c \n" +
                    "JOIN calendar_db.member AS m ON c.memberId = m.id\n" +
                    "WHERE name = name");

            List<Object> list = new ArrayList<>();

            // 조건 추가
            if (!searchDto.getSearchMemberId().isEmpty()) {
                sql.append(" AND memberId = ?");
                list.add(searchDto.getSearchMemberId().get());
            }

            if (searchDto.getFirstTime() != null) {
                if(searchDto.getLastTime() == null){
                    sql.append(" AND DATE(c.modifyDate) = ?");
                    list.add(searchDto.getFirstTime());
                }else {
                    sql.append(" AND c.modifyDate BETWEEN ? AND ?");
                    list.add(searchDto.getFirstTime());
                    list.add(searchDto.getLastTime());
                }

            }

            sql.append(" ORDER BY c.modifyDate DESC\n");
            sql.append("LIMIT ? OFFSET ?");
            list.add(searchDto.getPageLimit());
            list.add((searchDto.getPageNumber()-1) * searchDto.getPageLimit());


            try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < list.size(); i++) {
                    stmt.setObject(i + 1, list.get(i));
                }

                ResultSet rs = stmt.executeQuery();


                while (rs.next()) {
                    CalendarInfoDto dtoTemp = new CalendarInfoDto();
                    dtoTemp.setId(rs.getInt("id"));
                    dtoTemp.setMemberId(rs.getInt("memberId"));
                    dtoTemp.setContent(rs.getString("content"));
                    dtoTemp.setName(rs.getString("name"));
                    dtoTemp.setEnrollDate(rs.getTimestamp("enrollDate", kstTime).toLocalDateTime());
                    dtoTemp.setModifyDate(rs.getTimestamp("modifyDate", kstTime).toLocalDateTime());

                    dtoList.add(dtoTemp);
                }
                if(dtoList.isEmpty()){
                    throw new CustomException("없는 요청","해당 정보는 없습니다.");
                }

            }

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtoList;
    }

    //특정 일정 하나 조회
    public CalendarInfoDto selectOneCalendarById(CalendarInfoDto dto){
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "SELECT c.id,c.memberId ,name, content,c.enrollDate, c.modifyDate \n" +
                    "FROM calendar_db.calendar_challenge AS c \n" +
                    "JOIN calendar_db.member AS m ON c.memberId = m.id\n" +
                    "WHERE c.id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setObject(1, dto.getId());

                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    dto.setContent(rs.getString("content"));
                    dto.setName(rs.getString("name"));
                    dto.setEnrollDate(rs.getTimestamp("enrollDate", kstTime).toLocalDateTime());
                    dto.setMemberId(rs.getInt("memberId"));
                    dto.setModifyDate(rs.getTimestamp("modifyDate", kstTime).toLocalDateTime());
                }else{
                    throw new CustomException("없는 요청","해당 정보는 없습니다.");
                }
            }
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    //특정 멤버 id 하나 조회
    public int selectOneMemberIdById(CalendarInfoDto dto){
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "SELECT memberId FROM calendar_db.calendar_challenge WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setObject(1, dto.getId());

                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    return rs.getInt("memberId");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //일정 내용 업데이트
    public void updateOneContentById(CalendarInfoDto dto){
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "UPDATE calendar_db.calendar_challenge SET content = ?, modifyDate = NOW() where id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, dto.getContent());
                stmt.setInt(2, dto.getId());

                int rows = stmt.executeUpdate();
                System.out.println(rows + "행 변경 완료!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //일정 작성자 이름 업데이트
    public void updateOneNameById(CalendarInfoDto dto){
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "UPDATE calendar_db.member SET name = ?, modifyDate = NOW() where id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, dto.getName());
                stmt.setInt(2, dto.getId());

                int rows = stmt.executeUpdate();
                System.out.println(rows + "행 변경 완료!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //일정 삭제
    public void deleteOneCalendarById(CalendarInfoDto dto){
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "DELETE FROM calendar_db.calendar_challenge WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, dto.getId());

                int rows = stmt.executeUpdate();
                System.out.println(rows + "행 삭제 완료!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //입력된 비밀번호와 DB 비밀번호 검증
    public boolean verifyPasswordById(CalendarInfoDto dto){

        String password = "";
        try (Connection conn = MySqlConnection.getConnection()) {

            String sql = "SELECT password FROM calendar_db.calendar_challenge WHERE id = ?";

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
