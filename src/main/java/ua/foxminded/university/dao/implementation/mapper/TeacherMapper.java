package ua.foxminded.university.dao.implementation.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ua.foxminded.university.service.pojo.Teacher;

public class TeacherMapper implements RowMapper<Teacher>{

    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(rs.getInt("teacher_id"));
        teacher.setFirstName(rs.getString("first_name"));
        teacher.setLastName(rs.getString("last_name"));
        teacher.setPosition(rs.getString("position"));
        teacher.setDepartmentID(rs.getInt("department_id"));
        //teacher.setPassword(rs.getInt("password"));       
        return teacher;
    }
}
