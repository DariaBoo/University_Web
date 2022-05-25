package ua.foxminded.university.dao.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.TeacherDAO;
import ua.foxminded.university.dao.implementation.mapper.TeacherMapper;
import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.Teacher;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Repository
public class TeacherDAOImpl implements TeacherDAO {
    private final JdbcTemplate jdbcTemplate;
    private final String ADD_TEACHER = "INSERT INTO timetable.teachers (first_name, last_name, position, password, department_id, isActive) SELECT ?, ?, ?, 555, ?, true "
            + "WHERE NOT EXISTS (SELECT first_name, last_name FROM timetable.teachers WHERE first_name = ? AND last_name = ?)";
    private final String DELETE_TEACHER = "UPDATE timetable.teachers SET isActive = false WHERE teacher_id = ?;";
    private final String ASSIGN_LESSON_TO_TEACHER = "INSERT INTO timetable.lessons_teachers (lesson_id, teacher_id) SELECT ?, ? "
            + "WHERE NOT EXISTS (SELECT lesson_id, teacher_id FROM timetable.lessons_teachers WHERE lesson_id = ? AND teacher_id = ?) "
            + "AND EXISTS (SELECT lesson_id FROM timetable.lessons WHERE lesson_id = ?) "
            + " AND EXISTS (SELECT teacher_id FROM timetable.teachers WHERE teacher_id = ?)";
    private final String DELETE_LESSON_FROM_TEACHER = "DELETE FROM timetable.lessons_teachers WHERE lesson_id = ? AND teacher_id = ?";
    private final String CHANGE_POSITION = "UPDATE timetable.teachers SET position = ? WHERE teacher_id = ? "
            + "AND EXISTS (SELECT teacher_id FROM timetable.teachers WHERE teacher_id = ?)";
    private final String SET_TEACHER_ABSENT = "INSERT INTO timetable.teacherAbsent (teacher_id, date_start, date_end) SELECT ?, ?, ? "
            + "WHERE EXISTS (SELECT teacher_id FROM timetable.teachers WHERE teacher_id = ?)";
    private final String DELETE_TEACHER_ABSENT = "DELETE FROM timetable.teacherAbsent WHERE teacher_id = ? AND date_start = ? AND date_end = ?";
    private final String FIND_ALL_TEACHERS = "SELECT * FROM timetable.teachers WHERE isActive = true ORDER BY teacher_id;";
    private final String FIND_TEACHER_BY_ID = "SELECT * FROM timetable.teachers WHERE teacher_id = ?;";
    private final String FIND_TEACHERS_BY_DEPARTMENT = "SELECT * FROM timetable.teachers WHERE department_id = ? AND isActive = true ORDER BY teacher_id;";
    private final String UPDATE_TEACHER = "UPDATE timetable.teachers SET first_name = ?, last_name = ? WHERE teacher_id = ? AND EXISTS (SELECT teacher_id FROM timetable.teachers);";
    private final String SELECT_FIRST_NAME = "SELECT first_name FROM timetable.teachers WHERE teacher_id = ?;";
    private final String SELECT_LAST_NAME = "SELECT last_name FROM timetable.teachers WHERE teacher_id = ?;";
    private final String COUNT_OF_TEACHERS = "SELECT COUNT(*) FROM timetable.teachers;";
    private final String CHANGE_PASSWORD = "UPDATE timetable.teachers SET password = ? WHERE teacher_id = ?;";
    private final String FIRST_NAME_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('teachers') AND UPPER (column_name) = UPPER ('first_name');";
    private final String LAST_NAME_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('teachers') AND UPPER (column_name) = UPPER ('last_name');"; 
    private final String POSITION_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('teachers') AND UPPER (column_name) = UPPER ('position');";
    private final String PASSWORD_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('teachers') AND UPPER (column_name) = UPPER ('password');";
    private static final Logger log = LoggerFactory.getLogger(TeacherDAOImpl.class.getName());

    /**
     * Returns instance of the class
     * 
     * @param jdbcTemplate
     */
    @Autowired
    public TeacherDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addTeacher(Teacher teacher) {
        log.trace("Adds new teacher to the timetable.teachers and returns count of added rows otherwise returns zero");
        return jdbcTemplate.update(ADD_TEACHER, teacher.getFirstName(), teacher.getLastName(), teacher.getPosition(),
                teacher.getDepartmentID(), teacher.getFirstName(), teacher.getLastName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTeacher(int teacherID) {
        log.trace("Deletes teacher from the database and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_TEACHER, teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int assignLessonToTeacher(int lessonID, int teacherID) {
        log.trace(
                "Assigns lesson to teacher to the timetable.lessons_teachers and returns count of added rows otherwise returns zero");
        return jdbcTemplate.update(ASSIGN_LESSON_TO_TEACHER, lessonID, teacherID, lessonID, teacherID, lessonID,
                teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLessonFromTeacher(int lessonID, int teacherID) {
        log.trace(
                "Delete lesson from teacher from the timetable.lessons_teachers and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_LESSON_FROM_TEACHER, lessonID, teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int changePosition(int teacherID, String position) {
        log.trace(
                "Changes teacher's position at the timetable.teachers and returns count of updated rows otherwise returns zero");
        return jdbcTemplate.update(CHANGE_POSITION, position, teacherID, teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int setTeahcerAbsent(int teacherID, Day day) {
        log.trace("Sets dates when teacher is absent and returns count of added rows otherwise returns zero");
        return jdbcTemplate.update(SET_TEACHER_ABSENT, teacherID, day.getDateOne(), day.getDateTwo(), teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTeahcerAbsent(int teacherID, Day day) {
        log.trace(
                "Execute sql to delete teacher absent from the timetable.teacherabsent and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_TEACHER_ABSENT, teacherID, day.getDateOne(), day.getDateTwo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Teacher> findByID(int teacherID) {
        log.debug("Find teacher by id {}", teacherID);
        return jdbcTemplate.query(FIND_TEACHER_BY_ID, new Object[] { teacherID }, new TeacherMapper()).stream()
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Teacher>> findAllTeachers() {
        log.debug("Find all teachers from the timetable.teachers and returns optional list of teachers");
        return Optional.of(jdbcTemplate.query(FIND_ALL_TEACHERS, new TeacherMapper()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Teacher>> findTeachersByDepartment(int departmentID) {
        log.debug("Find all teachers by department from the timetable.teachers and returns optional list of teachers");
        return Optional.of(jdbcTemplate.query(FIND_TEACHERS_BY_DEPARTMENT, new Object[] { departmentID }, new TeacherMapper()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int changePassword(int teacherID, String newPassword) {
        log.trace("Change password in the timetable.teachers and return count of updated rows otherwise returns zero");
        return jdbcTemplate.update(CHANGE_PASSWORD, newPassword, teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateTeacher(Teacher teacher) {
        log.trace("Change teacher's name and surname and return count of updated rows otherwise zero");
        int countOfTeachers = jdbcTemplate.queryForObject(COUNT_OF_TEACHERS, Integer.class);
        log.debug("Took count of teachers {} from the timetable.teachers", countOfTeachers);
        int result = 0;
        if(teacher.getId() <= countOfTeachers) {
            String firstName = teacher.getFirstName() == null
                    ? jdbcTemplate.queryForObject(SELECT_FIRST_NAME, new Object[] { teacher.getId() }, String.class)
                    : teacher.getFirstName();
            log.debug("Took teacher's first name {} from input teacher, if equals null took from the timetable.teachers old value", firstName);
            String lastName = teacher.getLastName() == null
                    ? jdbcTemplate.queryForObject(SELECT_LAST_NAME, new Object[] { teacher.getId() }, String.class)
                    : teacher.getLastName();
            log.debug("Took teacher's last name {} from input teacher, if equals null took from the timetable.teachers old value", lastName);
            result = jdbcTemplate.update(UPDATE_TEACHER, firstName, lastName, teacher.getId());
            log.debug("Took a result {}, if the result equals 1 teacher was updated, if 0 - not updated", result);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFirstNameMaxSize() {
        log.trace("Get the column 'fist_name' size");
        return jdbcTemplate.queryForObject(FIRST_NAME_MAX_SIZE, Integer.class);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getLastNameMaxSize() {
        log.trace("Get the column 'last_name' size");
        return jdbcTemplate.queryForObject(LAST_NAME_MAX_SIZE, Integer.class);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getPositionMaxSize() {
        log.trace("Get the column 'position' size");
        return jdbcTemplate.queryForObject(POSITION_MAX_SIZE, Integer.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPasswordMaxSize() {
        log.trace("Get the column 'password' size");
        return jdbcTemplate.queryForObject(PASSWORD_MAX_SIZE, Integer.class);
    }
}
