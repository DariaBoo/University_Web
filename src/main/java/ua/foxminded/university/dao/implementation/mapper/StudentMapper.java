package ua.foxminded.university.dao.implementation.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ua.foxminded.university.service.pojo.Student;

public class StudentMapper implements RowMapper<Student>{
    private String firstName;
    private String lastName;

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();
        firstName = rs.getString("first_name");
        lastName = rs.getString("last_name");
        
        student.setId(rs.getInt("student_id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setGroupID(rs.getInt("group_id"));
        //student.setPassword(rs.getInt("password"));      
        student.setIdCard(rs.getString("id_card"));        
        return student;
    } 
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
}
