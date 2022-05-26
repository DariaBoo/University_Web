package ua.foxminded.university.dao.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.dao.implementation.mapper.StudentMapper;
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
    private final String CHANGE_GROUP = "UPDATE timetable.students SET group_id = ? WHERE student_id = ? "
            + "AND EXISTS (SELECT group_id FROM timetable.groups WHERE group_id = ?) "
            + "AND EXISTS (SELECT student_id FROM timetable.students WHERE student_id = ?)";
    private final String UPDATE_STUDENT = "UPDATE timetable.students SET first_name = ?, last_name = ? WHERE student_id = ? AND EXISTS (SELECT student_id FROM timetable.students);";
    private final String FIND_STUDENT_BY_ID = "SELECT * FROM timetable.students WHERE student_id = ?;";
    private final String FIND_ALL_STUDENTS = "SELECT * FROM timetable.students WHERE isActive = true ORDER BY student_id;";
    private final String FIND_STUDENTS_BY_GROUP = "SELECT * FROM timetable.students WHERE group_id = ? AND isActive = true ORDER BY student_id;";
    private final String CHANGE_PASSWORD = "UPDATE timetable.students SET password = ? WHERE student_id = ?;";
    private final String SELECT_FIRST_NAME = "SELECT first_name FROM timetable.students WHERE student_id = ?;";
    private final String SELECT_LAST_NAME = "SELECT last_name FROM timetable.students WHERE student_id = ?;";
    private final String COUNT_OF_STUDENTS = "SELECT COUNT(*) FROM timetable.students;";
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
    public int changeGroup(int studentID, int groupID) {
        log.trace("Change group id in the timetable.students");
        result = jdbcTemplate.update(CHANGE_GROUP, groupID, studentID, groupID, studentID);
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Student> findByID(int studentID) {
        log.trace("Find a student by id");
        Optional<Student> result = jdbcTemplate.query(FIND_STUDENT_BY_ID, new Object[] { studentID }, new StudentMapper()).stream()
                .findFirst();
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
        Optional<List<Student>> result = Optional.of(jdbcTemplate.query(FIND_STUDENTS_BY_GROUP, new Object[] { groupID }, new StudentMapper()));
        log.debug("Return optional list of students {}", result);
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
    public int updateStudent(Student student) {
        log.trace("Change student's name and surname and return count of updated rows otherwise zero");
        int countOfStudents = jdbcTemplate.queryForObject(COUNT_OF_STUDENTS, Integer.class);
        log.debug("Took count of students {} from the timetable.students", countOfStudents);
        result = 0;
        log.trace("Check if student id - {} is not out of bound", student.getId());
        if(student.getId() <= countOfStudents) {
        String firstName = student.getFirstName() == null
                ? jdbcTemplate.queryForObject(SELECT_FIRST_NAME, new Object[] { student.getId() }, String.class)
                : student.getFirstName();
        log.debug("Took student's first name {} from input student, if equals null took from the timetable.students old value", firstName);
        String lastName = student.getLastName() == null
                ? jdbcTemplate.queryForObject(SELECT_LAST_NAME, new Object[] { student.getId() }, String.class)
                : student.getLastName();
        log.debug("Took student's last name {} from input student, if equals null took from the timetable.students old value", lastName);
        result = jdbcTemplate.update(UPDATE_STUDENT, firstName, lastName, student.getId());
        log.debug("Took a result {}, if the result equals 1 student was updated, if 0 - not updated", result);
        }
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getFirstNameMaxSize() {
        log.trace("Get the column 'fist_name' size");
        result = jdbcTemplate.queryForObject(FIRST_NAME_MAX_SIZE, Integer.class);
        log.debug(debugMessage, result);
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getLastNameMaxSize() {
        log.trace("Get the column 'last_name' size");
        result = jdbcTemplate.queryForObject(LAST_NAME_MAX_SIZE, Integer.class);
        log.debug(debugMessage, result);
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getIdCardMaxSize() {
        log.trace("Get the column 'id_card' size");
        result = jdbcTemplate.queryForObject(ID_CARD_MAX_SIZE, Integer.class);
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPasswordMaxSize() {
        log.trace("Get the column 'password' size");
        result = jdbcTemplate.queryForObject(PASSWORD_MAX_SIZE, Integer.class);
        log.debug(debugMessage, result);
        return result;
    }
}
