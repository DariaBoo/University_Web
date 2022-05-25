package ua.foxminded.university.dao.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.dao.implementation.mapper.LessonMapper;
import ua.foxminded.university.service.pojo.Lesson;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Repository
public class LessonDAOImpl implements LessonDAO {
    private final JdbcTemplate jdbcTemplate;
    private final String ADD_LESSON = "INSERT INTO timetable.lessons (lesson_name, description, isActive) SELECT ?, ?, 'true' "
            + "WHERE NOT EXISTS (SELECT lesson_name FROM timetable.lessons WHERE lesson_name = ?)";
    private final String DELETE_LESSON = "UPDATE timetable.lessons SET isActive = false WHERE lesson_id = ?";
    private final String FIND_BY_ID = "SELECT * FROM timetable.lessons WHERE lesson_id = ?;";
    private final String FIND_ALL_LESSONS = "SELECT * FROM timetable.lessons WHERE isActive = true ORDER BY lesson_id;";
    private final String SELECT_LESSON_NAME = "SELECT lesson_name FROM timetable.lessons WHERE lesson_id = ?;";
    private final String SELECT_LESSON_DESCRIPTION = "SELECT description FROM timetable.lessons WHERE lesson_id = ?;";
    private final String UPDATE_LESSON = "UPDATE timetable.lessons SET lesson_name = ?, description = ? WHERE lesson_id = ? AND NOT EXISTS (SELECT lesson_name FROM timetable.lessons WHERE lesson_name = ?) AND EXISTS (SELECT lesson_id FROM timetable.lessons);";
    private final String LESSON_NAME_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('lessons') AND UPPER (column_name) = UPPER ('lesson_name');";
    private final String DESCRIPTION_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('lessons') AND UPPER (column_name) = UPPER ('description');";
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
    public int addLesson(Lesson lesson) {
        log.info("Add new lesson to timetable.lessons and returns count of added rows otherwise returns zero");
        return jdbcTemplate.update(ADD_LESSON, lesson.getName(), lesson.getDescription(), lesson.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLesson(int lessonID) {
        log.info("Delete lesson from the database and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_LESSON, lessonID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Lesson> findByID(int lessonID) {
        log.debug("Find lesson by id {} from the timetable.lessons", lessonID);
        return jdbcTemplate.query(FIND_BY_ID, new Object[] { lessonID }, new LessonMapper()).stream().findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Lesson>> findAllLessons() {
        log.debug("Find all lessons from the timetable.lessons and return optional list of lessons");
        return Optional.of(jdbcTemplate.query(FIND_ALL_LESSONS, new LessonMapper()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateLesson(Lesson lesson) {
        log.trace("Update lesson name and description");
        int result = 0;
        String lessonName = lesson.getName() == null
                ? jdbcTemplate.queryForObject(SELECT_LESSON_NAME, new Object[] { lesson.getId() }, String.class)
                : lesson.getName();
        log.debug(
                "Took lesson's name {} from the lesson, if equals null took previous value from the timetable.lessons",
                lessonName);
        String description = lesson.getDescription() == null
                ? jdbcTemplate.queryForObject(SELECT_LESSON_DESCRIPTION, new Object[] { lesson.getId() }, String.class)
                : lesson.getDescription();
        log.debug(
                "Took lesson's description {} from the lesson, if equals null took previous value from the timetable.lessons",
                description);
        result = jdbcTemplate.update(UPDATE_LESSON, lessonName, description, lesson.getId(), lesson.getName());
        log.debug("Took a result {}, if the result equals 1 lesson was updated, if 0 - not updated", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLessonNameMaxSize() {
        log.trace("Get max size of column lesson_name");
        return jdbcTemplate.queryForObject(LESSON_NAME_MAX_SIZE, Integer.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDescriptionMaxSize() {
        log.trace("Get max size of column description");
        return jdbcTemplate.queryForObject(DESCRIPTION_MAX_SIZE, Integer.class);
    }

}
