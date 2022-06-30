package ua.foxminded.university.dao.implementation.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.Teacher;

public class TeacherAbsentMapper implements RowMapper<Teacher> {

    private static final Logger log = LoggerFactory.getLogger(TeacherAbsentMapper.class.getName());

    /**
     * Returns rowMapper for Teacher
     */
    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.trace("Create rowMapper for Teacher");
        Day day = new Day();
        day.setDateOne(rs.getDate("date_start").toLocalDate());
        day.setDateTwo(rs.getDate("date_end").toLocalDate());
        Teacher teacher = new Teacher();
        teacher.setId(rs.getInt("teacher_id"));
        teacher.setAbsentPeriod(day);
        return teacher;
    }
}
