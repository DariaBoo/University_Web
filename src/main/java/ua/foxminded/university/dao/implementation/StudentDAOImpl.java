package ua.foxminded.university.dao.implementation;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.service.pojo.Student;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Component
public class StudentDAOImpl implements StudentDAO {
    private final JdbcTemplate jdbcTemplate;
    private final GroupDAOImpl groupDAOImpl;
    private final String DATABASE_NAME = "university";
    private final String ADD_STUDENT = "INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES (?, ?, ?, 1234, ?, true) RETURNING group_id";
    private final String ADD_STUDENT_TEST = "INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES (?, ?, ?, 1234, ?, true)";
    private final String IS_STUDENT_UNIQUE = "SELECT COUNT(*) FROM timetable.students WHERE first_name = ? AND last_name = ? AND isActive = true;";
    private final String DELETE_STUDENT = "UPDATE timetable.students SET isActive = false WHERE student_id = ?;";
    private final String CHANGE_GROUP = "UPDATE timetable.students SET group_id = ? WHERE student_id = ?";
    private final String IS_STUDENT_EXISTS = "SELECT COUNT(*) FROM timetable.students WHERE group_id = ? AND isActive = true";
    private static final Logger log = LoggerFactory.getLogger(StudentDAOImpl.class.getName());

    /**
     * Returns instance of the class
     * 
     * @param jdbcTemplate
     * @param groupDAOImpl
     */
    @Autowired
    public StudentDAOImpl(JdbcTemplate jdbcTemplate, GroupDAOImpl groupDAOImpl) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupDAOImpl = groupDAOImpl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addStudent(Student student) throws SQLException {
        log.trace("Start student adding method");
        KeyHolder key = new GeneratedKeyHolder();
        int result = -1;
        log.debug(
                "Choose correct sql query depends on the database: if work database = ADD_STUDENT, if test database = ADD_STUDENT_TEST");
        String sql = jdbcTemplate.getDataSource().getConnection().getCatalog().equals(DATABASE_NAME) ? ADD_STUDENT
                : ADD_STUDENT_TEST;
        log.info("Check if group exists in the timetable.groups and student is unique");
        if (isStudentUnique(student) && groupDAOImpl.isGroupExists(student.getGroupID())) {
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(sql,
                        java.sql.Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, student.getFirstName());
                statement.setString(2, student.getLastName());
                statement.setInt(3, student.getGroupID());
                statement.setInt(4, student.getIdCard());
                return statement;
            }, key);
            log.info("Executed sql query to add student to the database");
            result = key.getKey().intValue();
            log.debug("Took added student id {}", result);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteStudent(int studentID) {
        log.debug(
                "Execute sql to delete student from the database and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_STUDENT, studentID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int changeGroup(int studentID, int groupID) {
        int result = -1;
        log.info(
                "Check if student id {} is exists in the timetable.studetns and group id {} exists in the timetable.groups",
                studentID, groupID);
        if (isStudentExists(studentID) && groupDAOImpl.isGroupExists(groupID)) {
            result = jdbcTemplate.update(CHANGE_GROUP, groupID, studentID);
            log.debug("Executed sql to change group in the timetable.students and took the result {}", result);
        } else {
            log.info("Student wiht id {} is not exists or group with id {} is not exists", studentID, groupID);
        }
        return result;
    }

    private boolean isStudentUnique(Student student) {
        log.trace("Check if student first and last name are unique in the timetable.students");
        boolean result = true;
        int countOfRows = jdbcTemplate.queryForObject(IS_STUDENT_UNIQUE,
                new Object[] { student.getFirstName(), student.getLastName() }, Integer.class);
        log.debug(
                "Executed sql to take count of rows {} in the timetable.students with student first name {} and last name {}",
                countOfRows, student.getFirstName(), student.getLastName());
        if (countOfRows != 0) {
            result = false;
            log.info("Student with first name {} and last name {} is not unique and exists in the timetable.students",
                    student.getFirstName(), student.getLastName());
        } else {
            log.info("Student is unique");
        }
        return result;
    }

    private boolean isStudentExists(int studentID) {
        log.trace("Check if student id {} is exists in the timetable.students", studentID);
        boolean result = false;
        int countOfRows = jdbcTemplate.queryForObject(IS_STUDENT_EXISTS, new Object[] { studentID }, Integer.class);
        log.debug("Executed sql to take count of rows {} in the timetable.students with student id {}", countOfRows,
                studentID);
        if (countOfRows != 0) {
            result = true;
            log.info("Student with id {} exists in the timetable.students", studentID);
        } else {
            log.info("Student with id {} doesn't exist in the timetable.students", studentID);
        }
        return result;
    }
}
