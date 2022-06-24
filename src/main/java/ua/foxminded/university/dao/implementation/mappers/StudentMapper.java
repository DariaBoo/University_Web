package ua.foxminded.university.dao.implementation.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import ua.foxminded.university.service.pojo.Student;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public class StudentMapper implements RowMapper<Student>{
    private static final Logger log = LoggerFactory.getLogger(StudentMapper.class.getName());
    
    /**
     * Returns rowMapper for Student
     */
    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.trace("Create rowMapper for Studetn");
        Student student = new Student();        
        student.setId(rs.getInt("student_id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setGroupID(rs.getInt("group_id"));    
        student.setIdCard(rs.getString("id_card"));        
        return student;
    }    
}
