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

import ua.foxminded.university.dao.TeacherDAO;
import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.Teacher;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Component
public class TeacherDAOImpl implements TeacherDAO {
    private final JdbcTemplate jdbcTemplate;
    private final LessonDAOImpl lessonDAOImpl;
    private final String DATABASE_NAME = "university";
    private final String ADD_TEACHER = "INSERT INTO timetable.teachers (first_name, last_name, position, password, department_id, isActive) VALUES (?, ?, ?, 555, ?, true) RETURNING group_id";
    private final String ADD_TEACHER_TEST = "INSERT INTO timetable.teachers (first_name, last_name, position, password, department_id, isActive) VALUES (?, ?, ?, 555, ?, true)";
    private final String IS_TEACHER_UNIQUE = "SELECT COUNT(*) FROM timetable.teachers WHERE first_name = ? AND last_name = ? AND isActive = true";
    private final String DELETE_TEACHER = "UPDATE timetable.teachers SET isActive = false WHERE teacher_id = ?;";
    private final String IS_TEACHER_EXISTS = "SELECT COUNT(*) FROM timetable.teachers WHERE teacher_id = ? AND isActive = true";
    private final String ADD_LESSON = "INSERT INTO timetable.lessons_teachers (lesson_id, teacher_id) VALUES (?, ?)";
    private final String DELETE_LESSON = "DELETE FROM timetable.lessons_teachers WHERE lesson_id = ? AND teacher_id = ?";
    private final String IS_TEACHER_HAS_LESSON = "SELECT COUNT(*) FROM timetable.lessons_teachers WHERE lesson_id = ? AND teacher_id = ?";
    private final String CHANGE_POSITION = "UPDATE timetable.teachers SET position = ? WHERE teacher_id = ?;";
    private final String SET_TEACHER_ABSENT = "INSERT INTO timetable.teacherAbsent (teacher_id, date_start, date_end) VALUES (?, ?, ?);";
    private final String DELETE_TEACHER_ABSENT = "DELETE FROM timetable.teacherAbsent WHERE teacher_id = ? AND date_start = ? AND date_end = ?;";
    private static final Logger log = LoggerFactory.getLogger(TeacherDAOImpl.class.getName());

    /**
     * Returns instance of the class
     * 
     * @param jdbcTemplate
     * @param lessonDAOImpl
     */
    @Autowired
    public TeacherDAOImpl(JdbcTemplate jdbcTemplate, LessonDAOImpl lessonDAOImpl) {
        this.jdbcTemplate = jdbcTemplate;
        this.lessonDAOImpl = lessonDAOImpl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addTeacher(Teacher teacher) throws SQLException {
        log.trace("Start teacher adding method");
        KeyHolder key = new GeneratedKeyHolder();
        int result = -1;
        log.debug(
                "Choose correct sql query depends on the database: if work database = ADD_TEACHER, if test database = ADD_TEACHER_TEST");
        String sql = jdbcTemplate.getDataSource().getConnection().getCatalog().equals(DATABASE_NAME) ? ADD_TEACHER
                : ADD_TEACHER_TEST;
        log.info("Check if teacher first and last name is unique in the timetable.teachers table");
        if (isTeacherUnique(teacher)) {
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(sql,
                        java.sql.Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, teacher.getFirstName());
                statement.setString(2, teacher.getLastName());
                statement.setString(3, teacher.getPosition());
                statement.setInt(4, teacher.getDepartmentID());
                return statement;
            }, key);
            log.info("Executed sql query to add teacher to the database");
            result = key.getKey().intValue();
            log.debug("Took added teacher id {}", result);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTeacher(int teacherID) {
        log.debug(
                "Execute sql to delete teacher from the database and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_TEACHER, teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int assignLessonToTeacher(int lessonID, int teacherID) {
        int result = -1;
        log.info(
                "Check if teacher and lesson are exist in the timetable.teachers and timetable.lessons and check if the teacher has the lesson");
        if (isTeacherExists(teacherID) && lessonDAOImpl.isLessonExists(lessonID)
                && !isTeacherHasLesson(lessonID, teacherID)) {
            result = jdbcTemplate.update(ADD_LESSON, lessonID, teacherID);
            log.debug("Assign lesson to teacher to the timetable.lessons_teachers and took the result {}", result);
        } else {
            log.info(
                    "Teacher or lesson is not exists in the tables timetable.teachers and timetable.lessons or this teacher {} doesn't have this lesson {}",
                    teacherID, lessonID);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLessonFromTeacher(int lessonID, int teacherID) {
        int result = -1;
        log.info(
                "Check if teacher and lesson are exist in the timetable.teachers and timetable.lessons and check if the teacher has the lesson");
        if (isTeacherExists(teacherID) && lessonDAOImpl.isLessonExists(lessonID)
                && isTeacherHasLesson(lessonID, teacherID)) {
            result = jdbcTemplate.update(DELETE_LESSON, lessonID, teacherID);
            log.debug("Delete lesson from teacher from the timetable.lessons_teachers and took the result {}", result);
        } else {
            log.info(
                    "Teacher or lesson is not exists in the tables timetable.teachers and timetable.lessons or this teacher {} doesn't have this lesson {}",
                    teacherID, lessonID);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int changePosition(int teacherID, String position) {
        int result = -1;
        log.info("Check is teacher with id {} is exists in the timetable.teachers", teacherID);
        if (isTeacherExists(teacherID)) {
            result = jdbcTemplate.update(CHANGE_POSITION, position, teacherID);
            log.debug("Executed sql to change position in the timetable.teachers and took the result {}", result);
        } else {
            log.info("Teacher wiht id {} is not exists", teacherID);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int setTeahcerAbsent(int teacherID, Day day) { 
        int result = -1;
        log.info("Check is teacher with id {} is exists in the timetable.teachers", teacherID);
        if (isTeacherExists(teacherID)) {
            result = jdbcTemplate.update(SET_TEACHER_ABSENT, teacherID, day.getDateOne(), day.getDateTwo());
            log.debug(
                    "Executed sql to set teacher absent with start date {} and end date {} in the timetable.teacherabsent and took the result {}",
                    day.getDateOne(), day.getDateTwo(), result);
        } else {
            log.info("Teacher wiht id {} is not exists", teacherID);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTeahcerAbsent(int teacherID, Day day) {
        log.debug(
                "Execute sql to delete teacher absent from the the timetable.teacherabsent and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_TEACHER_ABSENT, teacherID, day.getDateOne(), day.getDateTwo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTeacherExists(int teacherID) {
        log.trace("Check if teacher id is exists in the timetable.teachers");
        boolean result = false;
        int countOfRows = jdbcTemplate.queryForObject(IS_TEACHER_EXISTS, new Object[] { teacherID }, Integer.class);
        log.debug("Executed sql to take count of rows {} from the timetable.teachers with teacher id {}", countOfRows,
                teacherID);
        if (countOfRows != 0) {
            result = true;
            log.info("Teacher with id {} exists in the timetable.teachers", teacherID);
        } else {
            log.info("Teacher with id {} doesn't exist in the timetable.teachers", teacherID);
        }
        return result;
    }

    private boolean isTeacherUnique(Teacher teacher) {
        log.trace("Check if teacher first and last name are unique in the timetable.teachers");
        boolean result = true;
        int countOfRows = jdbcTemplate.queryForObject(IS_TEACHER_UNIQUE,
                new Object[] { teacher.getFirstName(), teacher.getLastName() }, Integer.class);
        log.debug(
                "Executed sql to take count of rows {} in the timetable.teachers with teacher first name {} and last name {}",
                countOfRows, teacher.getFirstName(), teacher.getLastName());
        if (countOfRows != 0) {
            result = false;
            log.info("Teacher with first name {} and last name {} is not unique and exists in the timetable.teachers",
                    teacher.getFirstName(), teacher.getLastName());
        } else {
            log.info("Teacher is unique");
        }
        return result;
    }

    private boolean isTeacherHasLesson(int lessonID, int teacherID) {
        log.trace("Check if teacher with id {} has lesson with id {} in the timetable.lessons_teachers", teacherID,
                lessonID);
        boolean result = false;
        int countOfRows = jdbcTemplate.queryForObject(IS_TEACHER_HAS_LESSON, new Object[] { lessonID, teacherID },
                Integer.class);
        log.debug("Executed sql to take count of existed rows {} in the timetable.lessons_teachers", countOfRows);
        if (countOfRows != 0) {
            result = true;
            log.info("Teacher with id {} has lesson with id {} in the timetable.lessons_teachers", teacherID, lessonID);
        } else {
            log.info("This teacher {} doesn't have this lesson {}", teacherID, lessonID);
        }
        return result;
    }
}
