package com.zyoon.calendar.challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CalendarRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    CalendarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //멤버 추가
    public int insertOneMember(MemberDto dto){

        String sql = "INSERT INTO calendar_db.member (name, email) VALUES (?, ?)";

        return jdbcTemplate.update(sql, dto.getName(), dto.getEmail());
    }

    //하나의 일정 추가 
    public int insertOneCalendar(CalendarInfoDto dto){

        String sql = "INSERT INTO calendar_db.calendar_challenge (memberId, content, password) VALUES (?, ?, ?)";

        return jdbcTemplate.update(sql, dto.getMemberId(), dto.getContent(), dto.getPassword());
    }

    //페이징 된 리스트 조회 - 예전 JDBC 사용 (현재 사용 X)
    public List<CalendarInfoDto> selectPagedCalendarListBySearchOld(SearchDto searchDto){

        List<CalendarInfoDto> dtoList = new ArrayList<>();
        try (Connection conn = MySqlConnection.getConnection()) {

            StringBuilder sql = new StringBuilder("SELECT c.id,c.memberId ,name, content,c.enrollDate, c.modifyDate \n" +
                    "FROM calendar_db.calendar_challenge AS c \n" +
                    "JOIN calendar_db.member AS m ON c.memberId = m.id\n" +
                    "WHERE name = name");

            List<Object> list = new ArrayList<>();

            // 조건 추가
            if (searchDto.getSearchMemberId().isPresent()) {
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
                    dtoTemp.setEnrollDate(rs.getTimestamp("enrollDate").toLocalDateTime());
                    dtoTemp.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());

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

    //JDBC 템플릿 사용
    public List<CalendarInfoDto> selectPagedCalendarListBySearch(SearchDto searchDto) {

        StringBuilder sql = new StringBuilder("SELECT c.id,c.memberId ,name, content,c.enrollDate, c.modifyDate \n" +
                "FROM calendar_db.calendar_challenge AS c \n" +
                "JOIN calendar_db.member AS m ON c.memberId = m.id\n" +
                "WHERE name = name");

        List<Object> list = new ArrayList<>();

        // 조건 추가
        if (searchDto.getSearchMemberId().isPresent()) {
            sql.append(" AND c.memberId = ?");
            list.add(searchDto.getSearchMemberId().get());
        }

        if (searchDto.getFirstTime() != null) {
            if (searchDto.getLastTime() == null) {
                sql.append(" AND DATE(c.modifyDate) = ?");
                list.add(searchDto.getFirstTime());
            } else {
                sql.append(" AND c.modifyDate BETWEEN ? AND ?");
                list.add(searchDto.getFirstTime());
                list.add(searchDto.getLastTime());
            }
        }

        sql.append(" ORDER BY c.modifyDate DESC");
        sql.append(" LIMIT ? OFFSET ?");
        list.add(searchDto.getPageLimit());
        list.add((searchDto.getPageNumber() - 1) * searchDto.getPageLimit());

        List<CalendarInfoDto> resultList = jdbcTemplate.query(sql.toString(), list.toArray(),
            (rs, rowNum) -> {
                CalendarInfoDto dto = new CalendarInfoDto();
                dto.setId(rs.getInt("id"));
                dto.setMemberId(rs.getInt("memberId"));
                dto.setContent(rs.getString("content"));
                dto.setName(rs.getString("name"));
                dto.setEnrollDate(rs.getTimestamp("enrollDate").toLocalDateTime());
                dto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
                return dto;
            }
        );

        if (resultList.isEmpty()) {
            throw new CustomException("없는 요청", "해당 정보는 없습니다.");
        }

        return resultList;
    }

    //특정 일정 하나 조회
    public CalendarInfoDto selectOneCalendarById(CalendarInfoDto dto){

        String sql = "SELECT c.id,c.memberId ,name, content,c.enrollDate, c.modifyDate \n" +
                    "FROM calendar_db.calendar_challenge AS c \n" +
                    "JOIN calendar_db.member AS m ON c.memberId = m.id\n" +
                    "WHERE c.id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            CalendarInfoDto result = new CalendarInfoDto();
            result.setContent(rs.getString("content"));
            result.setName(rs.getString("name"));
            result.setEnrollDate(rs.getTimestamp("enrollDate").toLocalDateTime());
            result.setMemberId(rs.getInt("memberId"));
            result.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
            return result;
        }, dto.getId());

    }

    //특정 멤버 id 하나 조회
    public int selectOneMemberIdById(CalendarInfoDto dto){

        String sql = "SELECT memberId FROM calendar_db.calendar_challenge WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, dto.getId());
        } catch (NullPointerException e) {
            return 0;
        }
    }

    //일정 내용 업데이트
    public boolean updateOneContentById(CalendarInfoDto dto){

        String sql = "UPDATE calendar_db.calendar_challenge SET content = ?, modifyDate = NOW() where id = ?";

        return jdbcTemplate.update(sql, dto.getContent(), dto.getId()) == 1;
    }

    //일정 작성자 이름 업데이트
    public boolean updateOneNameById(CalendarInfoDto dto){

        String sql = "UPDATE calendar_db.member SET name = ?, modifyDate = NOW() where id = ?";

        return jdbcTemplate.update(sql, dto.getName(), dto.getId()) == 1;
    }

    //일정 삭제
    public int deleteOneCalendarById(CalendarInfoDto dto){

        String sql = "DELETE FROM calendar_db.calendar_challenge WHERE id = ?";

        return jdbcTemplate.update(sql, dto.getId());
    }

    //입력된 비밀번호와 DB 비밀번호 검증
    public boolean verifyPasswordById(CalendarInfoDto dto){

        String sql = "SELECT password FROM calendar_db.calendar_challenge WHERE id = ?";

        String password = jdbcTemplate.queryForObject(sql, String.class, dto.getId());

        return password.equals(dto.getPassword());
    }



}
