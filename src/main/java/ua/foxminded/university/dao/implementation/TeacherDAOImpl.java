package ua.foxminded.university.dao.implementation;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addTeacher(Teacher teacher) throws SQLException {
        log.info("Adds new teacher to the timetable.teachers and returns count of added rows otherwise returns zero");
        return jdbcTemplate.update(ADD_TEACHER, teacher.getFirstName(), teacher.getLastName(), teacher.getPosition(),
                teacher.getDepartmentID(), teacher.getFirstName(), teacher.getLastName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTeacher(int teacherID) {
        log.info("Deletes teacher from the database and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_TEACHER, teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int assignLessonToTeacher(int lessonID, int teacherID) {
        log.info(
                "Assigns lesson to teacher to the timetable.lessons_teachers and returns count of added rows otherwise returns zero");
        return jdbcTemplate.update(ASSIGN_LESSON_TO_TEACHER, lessonID, teacherID, lessonID, teacherID, lessonID,
                teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLessonFromTeacher(int lessonID, int teacherID) {
        log.info(
                "Delete lesson from teacher from the timetable.lessons_teachers and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_LESSON_FROM_TEACHER, lessonID, teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int changePosition(int teacherID, String position) {
        log.info(
                "Changes teacher's position at the timetable.teachers and returns count of updated rows otherwise returns zero");
        return jdbcTemplate.update(CHANGE_POSITION, position, teacherID, teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int setTeahcerAbsent(int teacherID, Day day) {
        log.info("Sets dates when teacher is absent and returns count of added rows otherwise returns zero");
        return jdbcTemplate.update(SET_TEACHER_ABSENT, teacherID, day.getDateOne(), day.getDateTwo(), teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTeahcerAbsent(int teacherID, Day day) {
        log.info(
                "Execute sql to delete teacher absent from the the timetable.teacherabsent and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_TEACHER_ABSENT, teacherID, day.getDateOne(), day.getDateTwo());
    }
}
