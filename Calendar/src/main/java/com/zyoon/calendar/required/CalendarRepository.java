package com.zyoon.calendar.required;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
class CalendarRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    CalendarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insertOneCalendar(CalendarInfoDto dto){
        String sql = "INSERT INTO calendar_db.calendar_required (name, content, password) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, dto.getName(), dto.getContent(), dto.getPassword());
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
                    dtoTemp.setEnrollDate(rs.getTimestamp("enrollDate").toLocalDateTime());
                    dtoTemp.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
                    dtoList.add(dtoTemp);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtoList;
    }

    public CalendarInfoDto selectOneCalendarById(CalendarInfoDto dto){

        String sql = "SELECT * FROM calendar_db.calendar_required WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            CalendarInfoDto resultDto = new CalendarInfoDto();
            resultDto.setId(rs.getInt("id"));
            resultDto.setContent(rs.getString("content"));
            resultDto.setName(rs.getString("name"));
            resultDto.setPassword(rs.getString("password"));
            resultDto.setEnrollDate(rs.getTimestamp("enrollDate").toLocalDateTime());
            resultDto.setModifyDate(rs.getTimestamp("modifyDate").toLocalDateTime());
            return resultDto;
        }, dto.getId());
    }

    public int updateOneCalendarById(CalendarInfoDto dto){

        String sql = "UPDATE calendar_db.calendar_required SET name = ?, content = ?, modifyDate = NOW() where id = ?";

        return jdbcTemplate.update(sql, dto.getName(), dto.getContent(), dto.getId());

    }
    public int deleteOneCalendarById(CalendarInfoDto dto){

        String sql = "DELETE FROM calendar_db.calendar_required WHERE id = ?";

        return jdbcTemplate.update(sql, dto.getId());
    }


    public boolean verifyPasswordById(CalendarInfoDto dto){

        String sql = "SELECT password FROM calendar_db.calendar_required WHERE id = ?";

        String password = jdbcTemplate.queryForObject(sql, String.class, dto.getId());

        return password.equals(dto.getPassword());

    }



}
