package ua.foxminded.university.dao.implementation;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.service.pojo.Lesson;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Component
public class LessonDAOImpl implements LessonDAO {
    private final JdbcTemplate jdbcTemplate;
    private final String ADD_LESSON = "INSERT INTO timetable.lessons (lesson_name, description, isActive) SELECT ?, ?, 'true' "
            + "WHERE NOT EXISTS (SELECT lesson_name FROM timetable.lessons WHERE lesson_name = ?)";
    private final String DELETE_LESSON = "UPDATE timetable.lessons SET isActive = false WHERE lesson_id = ?";
    private static final Logger log = LoggerFactory.getLogger(LessonDAOImpl.class.getName());

    /**
     * Returns instance of the class
     * 
     * @param jdbcTemplate
     */
    @Autowired
    public LessonDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addLesson(Lesson lesson) throws SQLException {
        log.info("Add new lesson to timetable.lessons and returns count of added rows otherwise returns zero");
        return jdbcTemplate.update(ADD_LESSON, lesson.getName(), lesson.getDescription(), lesson.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLesson(int lessonID) {
        log.debug("Delete lesson from the database and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_LESSON, lessonID);
    }
}
