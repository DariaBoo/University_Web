package ua.foxminded.university.dao.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.dao.implementation.mappers.LessonMapper;
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
    private final String UPDATE_LESSON = "UPDATE timetable.lessons SET lesson_name = ?, description = ? WHERE lesson_id = ? AND NOT EXISTS (SELECT lesson_name FROM timetable.lessons WHERE lesson_name = ?) AND EXISTS (SELECT lesson_id FROM timetable.lessons);";
    private final String FIND_BY_TEACHER_ID = "SELECT tl.* FROM timetable.lessons AS tl LEFT JOIN timetable.lessons_teachers AS tlt ON tlt.lesson_id = tl.lesson_id WHERE tlt.teacher_id = ?;";
    private final String FIND_BY_GROUP_ID = "SELECT tl.* FROM timetable.lessons AS tl LEFT JOIN timetable.groups_lessons AS tgl ON tgl.lesson_id = tl.lesson_id WHERE tgl.group_id = ?;";
    private final String LESSON_NAME_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('lessons') AND UPPER (column_name) = UPPER ('lesson_name');";
    private final String DESCRIPTION_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('lessons') AND UPPER (column_name) = UPPER ('description');";
    private static final Logger log = LoggerFactory.getLogger(LessonDAOImpl.class.getName());
    private final String debugMessage = "Return count of rows otherwise returns zero. The result is {}";
    List<Lesson> resultList = new ArrayList<>();
    private int result;

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
        log.trace("Add new lesson to timetable.lessons");
        result = jdbcTemplate.update(ADD_LESSON, lesson.getName(), lesson.getDescription(), lesson.getName());
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLesson(int lessonID) {
        log.trace("Delete lesson from the database");
        result = jdbcTemplate.update(DELETE_LESSON, lessonID);
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateLesson(Lesson lesson) {
        log.trace("Update lesson name and description");
        result = jdbcTemplate.update(UPDATE_LESSON, lesson.getName(), lesson.getDescription(), lesson.getId(),
                lesson.getName());
        log.debug("Took a result {}, if the result equals 1 lesson was updated, if 0 - not updated", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Lesson> findByID(int lessonID) {
        log.trace("Find lesson by id {} from the timetable.lessons", lessonID);
        Optional<Lesson> resultLesson = jdbcTemplate.query(FIND_BY_ID, new Object[] { lessonID }, new LessonMapper())
                .stream().findFirst();
        log.debug("Return optional lesson {}", result);
        return resultLesson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> findAllLessons() {
        log.trace("Find all lessons from the timetable.lessons");
        resultList = jdbcTemplate.query(FIND_ALL_LESSONS, new LessonMapper());
        log.debug("Return optional list of lessons {}", result);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> findLessonsByTeacherId(int teacherID) {
        log.trace("Find teachers lessons by teacher id - {} from the timetable.lessons", teacherID);
        resultList = jdbcTemplate.query(FIND_BY_TEACHER_ID, new Object[] { teacherID }, new LessonMapper());
        log.debug("Return list of lessons {}", result);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> findLessonsByGroupId(int groupID) {
        log.trace("Find groups lessons by group id - {} from the timetable.lessons", groupID);
        resultList = jdbcTemplate.query(FIND_BY_GROUP_ID, new Object[] { groupID }, new LessonMapper());
        log.debug("Return list of lessons {}", result);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLessonNameMaxSize() {
        return jdbcTemplate.queryForObject(LESSON_NAME_MAX_SIZE, Integer.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDescriptionMaxSize() {
        return jdbcTemplate.queryForObject(DESCRIPTION_MAX_SIZE, Integer.class);
    }
}
