package ua.foxminded.university.dao.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.dao.implementation.mappers.StudentMapper;
import ua.foxminded.university.service.pojo.Student;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Repository
public class StudentDAOImpl implements StudentDAO {

    private final JdbcTemplate jdbcTemplate;
    private final String ADD_STUDENT = "INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) SELECT ?, ?, ?, 1234, ?, true "
            + "WHERE NOT EXISTS (SELECT first_name, last_name FROM timetable.students WHERE first_name = ? AND last_name = ?) "
            + "AND EXISTS (SELECT group_id FROM timetable.groups WHERE group_id = ?)";
    private final String DELETE_STUDENT = "UPDATE timetable.students SET isActive = false WHERE student_id = ?;";
    private final String UPDATE_STUDENT = "UPDATE timetable.students SET first_name = ?, last_name = ?, group_id = ?, id_card = ? WHERE student_id = ?;";
    private final String FIND_STUDENT_BY_ID = "SELECT * FROM timetable.students WHERE student_id = ? AND isActive = true;";
    private final String FIND_ALL_STUDENTS = "SELECT * FROM timetable.students WHERE isActive = true ORDER BY student_id;";
    private final String FIND_STUDENTS_BY_GROUP = "SELECT * FROM timetable.students WHERE group_id = ? AND isActive = true ORDER BY student_id;";
    private final String CHANGE_PASSWORD = "UPDATE timetable.students SET password = ? WHERE student_id = ?;";
    private final String FIRST_NAME_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('students') AND UPPER (column_name) = UPPER ('first_name');";
    private final String LAST_NAME_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('students') AND UPPER (column_name) = UPPER ('last_name');";
    private final String ID_CARD_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('students') AND UPPER (column_name) = UPPER ('id_card');";
    private final String PASSWORD_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('students') AND UPPER (column_name) = UPPER ('password');";
    private static final Logger log = LoggerFactory.getLogger(StudentDAOImpl.class.getName());
    private final String debugMessage = "Return count of rows otherwise returns zero. The result is {}";
    private int result;

    /**
     * Returns instance of the class
     * 
     * @param jdbcTemplate
     */
    @Autowired
    public StudentDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addStudent(Student student) {
        log.trace("Add new student to timetable.students");
        result = jdbcTemplate.update(ADD_STUDENT, student.getFirstName(), student.getLastName(), student.getGroupID(),
                student.getIdCard(), student.getFirstName(), student.getLastName(), student.getGroupID());
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateStudent(Student student) {
        log.trace("Change student's name and surname and return count of updated rows otherwise zero");
        result = jdbcTemplate.update(UPDATE_STUDENT, student.getFirstName(), student.getLastName(),
                student.getGroupID(), student.getIdCard(), student.getId());
        log.debug("Took a result - {}, if the result equals 1 student was updated, if 0 - not updated", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteStudent(int studentID) {
        log.trace("Delete student from the database");
        result = jdbcTemplate.update(DELETE_STUDENT, studentID);
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int changePassword(int studentID, String newPassword) {
        log.trace("Change password in the timetable.students for student {}", studentID);
        result = jdbcTemplate.update(CHANGE_PASSWORD, newPassword, studentID);
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Student> findByID(int studentID) {
        log.trace("Find a student by id");
        Optional<Student> result = jdbcTemplate
                .query(FIND_STUDENT_BY_ID, new Object[] { studentID }, new StudentMapper()).stream().findFirst();
        log.debug("Return optional student {}", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Student>> findAllStudents() {
        log.trace("Find all students and return optional list of students");
        Optional<List<Student>> result = Optional.of(jdbcTemplate.query(FIND_ALL_STUDENTS, new StudentMapper()));
        log.debug("Return optional list of students {}", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Student>> findStudentsByGroup(int groupID) {
        log.trace("Find all students by group id");
        Optional<List<Student>> result = Optional
                .of(jdbcTemplate.query(FIND_STUDENTS_BY_GROUP, new Object[] { groupID }, new StudentMapper()));
        log.debug("Return optional list of students {}", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFirstNameMaxSize() {
        return jdbcTemplate.queryForObject(FIRST_NAME_MAX_SIZE, Integer.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLastNameMaxSize() {
        return jdbcTemplate.queryForObject(LAST_NAME_MAX_SIZE, Integer.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIdCardMaxSize() {
        return jdbcTemplate.queryForObject(ID_CARD_MAX_SIZE, Integer.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPasswordMaxSize() {
        return jdbcTemplate.queryForObject(PASSWORD_MAX_SIZE, Integer.class);
    }
}
