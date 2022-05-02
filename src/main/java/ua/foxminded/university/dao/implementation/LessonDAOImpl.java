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
    private final String DATABASE_NAME = "university";
    private final String ADD_LESSON = "INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES (?, ?, true) RETURNING group_id";
    private final String ADD_LESSON_TEST = "INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES (?, ?, true)";
    private final String DELETE_LESSON = "UPDATE timetable.lessons SET isActive = false WHERE lesson_id = ?";
    private final String IS_EXISTS = "SELECT COUNT(*) FROM timetable.lessons WHERE lesson_id = ? AND isActive = true";
    private final String IS_NAME_UNIQUE = "SELECT COUNT(*) FROM timetable.lessons WHERE lesson_name = ?";
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
        log.trace("Start lesson adding methos");
        KeyHolder key = new GeneratedKeyHolder();
        int result = -1;
        log.debug(
                "Choose correct sql query depends on the database: if work database = ADD_LESSON, if test database = ADD_LESSON_TEST");
        String sql = jdbcTemplate.getDataSource().getConnection().getCatalog().equals(DATABASE_NAME) ? ADD_LESSON
                : ADD_LESSON_TEST;
        if (isNameUnique(lesson.getName())) {
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(sql,
                        java.sql.Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, lesson.getName());
                statement.setString(2, lesson.getDescription());
                return statement;
            }, key);
            log.info("Executed sql query to add lesson to the database");
            result = key.getKey().intValue();
            log.debug("Took added lesson id {}", result);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLesson(int lessonID) {
        log.debug(
                "Execute sql to delete lesson from the database and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_LESSON, lessonID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessonExists(int lessonID) {
        log.trace("Check if lesson id {} is exists in the timetable.groups", lessonID);
        boolean result = false;
        int countOfRows = jdbcTemplate.queryForObject(IS_EXISTS, new Object[] { lessonID }, Integer.class);
        log.debug("Executed sql to take count of rows {} in the timetable.groups with group id {}", countOfRows,
                lessonID);
        if (countOfRows != 0) {
            result = true;
            log.info("Lesson with id {} exists in the timetable.lessons", lessonID);
        } else {
            log.info("Lesson with id {} doesn't exist in the timetable.lessons", lessonID);
        }
        return result;
    }

    private boolean isNameUnique(String lessonName) {
        log.trace("Check if lesson name is unique in the timetable.lessons");
        boolean result = true;
        int countOfRows = jdbcTemplate.queryForObject(IS_NAME_UNIQUE, new Object[] { lessonName }, Integer.class);
        log.debug("Executed sql to take count of rows {} in the timetable.lessons with lesson name {}", countOfRows,
                lessonName);
        if (countOfRows != 0) {
            result = false;
            log.info("Lesson with name {} is not unique and already exists in the timetable.lessons", lessonName);
        } else {
            log.info("The group with name {} is unique and not exists in the database", lessonName);
        }
        return result;
    }

}
