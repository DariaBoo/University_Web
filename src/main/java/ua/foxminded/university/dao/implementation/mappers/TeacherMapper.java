package ua.foxminded.university.dao.implementation.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import ua.foxminded.university.service.pojo.Teacher;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public class TeacherMapper implements RowMapper<Teacher>{
    private static final Logger log = LoggerFactory.getLogger(TeacherMapper.class.getName());

    /**
     * Returns rowMapper for Teacher
     */
    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.trace("Create rowMapper for Teacher");
        Teacher teacher = new Teacher();
        teacher.setId(rs.getInt("teacher_id"));
        teacher.setFirstName(rs.getString("first_name"));
        teacher.setLastName(rs.getString("last_name"));
        teacher.setPosition(rs.getString("position"));
        teacher.setDepartmentID(rs.getInt("department_id")); 
        return teacher;
    }
}
