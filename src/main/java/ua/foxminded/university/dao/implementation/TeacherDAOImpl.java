package ua.foxminded.university.dao.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.TeacherDAO;
import ua.foxminded.university.dao.implementation.mappers.TeacherAbsentMapper;
import ua.foxminded.university.dao.implementation.mappers.TeacherMapper;
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
    private final String SET_TEACHER_ABSENT = "INSERT INTO timetable.teacherAbsent (teacher_id, date_start, date_end) SELECT ?, ?, ? "
            + "WHERE EXISTS (SELECT teacher_id FROM timetable.teachers WHERE teacher_id = ?)";
    private final String DELETE_TEACHER_ABSENT = "DELETE FROM timetable.teacherAbsent WHERE teacher_id = ? AND date_start = ? AND date_end = ? AND date_start > CURRENT_DATE";
    private final String SHOW_TEACHER_ABSENT = "SELECT * FROM timetable.teacherabsent WHERE teacher_id = ?";
    private final String FIND_ALL_TEACHERS = "SELECT * FROM timetable.teachers WHERE isActive = true ORDER BY teacher_id;";
    private final String FIND_TEACHER_BY_ID = "SELECT * FROM timetable.teachers WHERE teacher_id = ?;";
    private final String FIND_TEACHERS_BY_DEPARTMENT = "SELECT * FROM timetable.teachers WHERE department_id = ? AND isActive = true ORDER BY teacher_id;";
    private final String FIND_TEACHERS_BY_LESSON_ID = "SELECT tt.* FROM timetable.teachers AS tt, timetable.lessons_teachers AS tlt WHERE tt.teacher_id = tlt.teacher_id  AND tlt.lesson_id = ?;";
    private final String UPDATE_TEACHER = "UPDATE timetable.teachers SET first_name = ?, last_name = ?, position = ? WHERE teacher_id = ? AND EXISTS (SELECT teacher_id FROM timetable.teachers);";
    private final String CHANGE_PASSWORD = "UPDATE timetable.teachers SET password = ? WHERE teacher_id = ?;";
    private final String FIRST_NAME_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('teachers') AND UPPER (column_name) = UPPER ('first_name');";
    private final String LAST_NAME_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('teachers') AND UPPER (column_name) = UPPER ('last_name');";
    private final String POSITION_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('teachers') AND UPPER (column_name) = UPPER ('position');";
    private final String PASSWORD_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('teachers') AND UPPER (column_name) = UPPER ('password');";
    private static final Logger log = LoggerFactory.getLogger(TeacherDAOImpl.class.getName());
    private final String debugMessage = "Return count of rows otherwise returns zero. The result is {}";
    private int result;

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
        result = jdbcTemplate.update(ADD_TEACHER, teacher.getFirstName(), teacher.getLastName(), teacher.getPosition(),
                teacher.getDepartmentID(), teacher.getFirstName(), teacher.getLastName());
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateTeacher(Teacher teacher) {
        log.trace("Change teacher's name and surname and return count of updated rows otherwise zero");
        result = jdbcTemplate.update(UPDATE_TEACHER, teacher.getFirstName(), teacher.getLastName(),
                teacher.getPosition(), teacher.getId());
        log.debug("Took a result {}, if the result equals 1 teacher was updated, if 0 - not updated", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTeacher(int teacherID) {
        log.trace("Deletes teacher from the database and returns count of deleted rows otherwise returns zero");
        result = jdbcTemplate.update(DELETE_TEACHER, teacherID);
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int assignLessonToTeacher(int lessonID, int teacherID) {
        log.trace("Assigns lesson to teacher to the timetable.lessons_teachers");
        result = jdbcTemplate.update(ASSIGN_LESSON_TO_TEACHER, lessonID, teacherID, lessonID, teacherID, lessonID,
                teacherID);
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLessonFromTeacher(int lessonID, int teacherID) {
        log.trace("Delete lesson from teacher from the timetable.lessons_teachers");
        result = jdbcTemplate.update(DELETE_LESSON_FROM_TEACHER, lessonID, teacherID);
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int setTeahcerAbsent(int teacherID, Day day) {
        log.trace("Sets dates when teacher is absent and returns count of added rows otherwise returns zero");
        result = jdbcTemplate.update(SET_TEACHER_ABSENT, teacherID, day.getDateOne(), day.getDateTwo(), teacherID);
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTeahcerAbsent(int teacherID, Day day) {
        log.trace("Execute sql to delete teacher absent from the timetable.teacherabsent");
        result = jdbcTemplate.update(DELETE_TEACHER_ABSENT, teacherID, day.getDateOne(), day.getDateTwo());
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Teacher>> showTeacherAbsent(int teacherID) {
        log.trace("Find all teacher absent days");
        Optional<List<Teacher>> resultList = Optional
                .of(jdbcTemplate.query(SHOW_TEACHER_ABSENT, new Object[] { teacherID }, new TeacherAbsentMapper()));
        log.debug("Returns optional list of teachers {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Teacher> findByID(int teacherID) {
        log.trace("Find teacher by id {}", teacherID);
        Optional<Teacher> resultList = jdbcTemplate
                .query(FIND_TEACHER_BY_ID, new Object[] { teacherID }, new TeacherMapper()).stream().findFirst();
        log.debug("Return optional teacher {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Teacher>> findAllTeachers() {
        log.trace("Find all teachers from the timetable.teachers");
        Optional<List<Teacher>> resultList = Optional.of(jdbcTemplate.query(FIND_ALL_TEACHERS, new TeacherMapper()));
        log.debug("Returns optional list of teachers {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Teacher>> findTeachersByDepartment(int departmentID) {
        log.trace("Find all teachers by department from the timetable.teachers");
        Optional<List<Teacher>> resultList = Optional.of(
                jdbcTemplate.query(FIND_TEACHERS_BY_DEPARTMENT, new Object[] { departmentID }, new TeacherMapper()));
        log.debug("Returns optional list of teachers {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Teacher>> findTeachersByLessonId(int lessonID) {
        log.trace("Find all teachers by lesson id");
        Optional<List<Teacher>> resultList = Optional
                .of(jdbcTemplate.query(FIND_TEACHERS_BY_LESSON_ID, new Object[] { lessonID }, new TeacherMapper()));
        log.debug("Returns optional list of teachers {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int changePassword(int teacherID, String newPassword) {
        log.trace("Change password in the timetable.teachers and return count of updated rows otherwise returns zero");
        result = jdbcTemplate.update(CHANGE_PASSWORD, newPassword, teacherID);
        log.debug(debugMessage, result);
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
    public int getPositionMaxSize() {
        return jdbcTemplate.queryForObject(POSITION_MAX_SIZE, Integer.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPasswordMaxSize() {
        return jdbcTemplate.queryForObject(PASSWORD_MAX_SIZE, Integer.class);
    }
}
