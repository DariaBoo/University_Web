package ua.foxminded.university.dao.implementation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.TimetableDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.dao.implementation.mappers.TimetableMapper;
import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.Timetable;
import ua.foxminded.university.service.pojo.Student;
import ua.foxminded.university.service.pojo.Teacher;
import ua.foxminded.university.service.pojo.User;

/**
 * @version 1.0
 * @author Bogush Daria
 * @param <K>
 *
 *
 */
@Repository
public class TimetableDAOImpl implements TimetableDAO {
    private final JdbcTemplate jdbcTemplate;
    private final String SCHEDULE_TIMETABLE = "INSERT INTO timetable.timetable (date, time_period, lesson_id, group_id, teacher_id, room_id) SELECT ?, ?, ?, ?, ?, ? \n"
            + "WHERE NOT EXISTS (SELECT lesson_id, group_id FROM timetable.timetable WHERE date = ? AND time_period = ? AND lesson_id = ? AND group_id = ?) \n"
            + "AND EXISTS (SELECT group_id, lesson_id FROM timetable.groups_lessons WHERE group_id = ? AND lesson_id = ?) \n"
            + "AND EXISTS (SELECT lesson_id FROM timetable.lessons) \n"
            + "AND EXISTS (SELECT group_id FROM timetable.groups)";
    private final String SELECT_SUITABLE_ROOM = "SELECT room_id FROM timetable.rooms WHERE capacity >= (SELECT COUNT(*) FROM timetable.students WHERE group_id = ?) \n"
            + "AND NOT EXISTS (SELECT room_id FROM timetable.timetable WHERE date = ? AND time_period = ? AND room_id = timetable.rooms.room_id) ORDER BY room_id LIMIT 1";
    private final String SELECT_AVAILABLE_TEACHER = "SELECT lt.teacher_id  FROM timetable.lessons_teachers lt "
            + "LEFT JOIN timetable.teacherabsent ta ON ta.teacher_id = lt.teacher_id AND date_start <= ? AND date_end >= ? "
            + "WHERE NOT EXISTS (SELECT teacher_id FROM timetable.timetable t WHERE t.date = ? AND t.time_period = ? AND t.teacher_id = lt.teacher_id) AND lt.lesson_id = ?  "
            + "AND EXISTS (SELECT lesson_id FROM timetable.lessons WHERE lesson_id = lt.lesson_id) ORDER BY teacher_id LIMIT 1;";
    private final String DELETE_TIMETABLE = "DELETE FROM timetable.timetable WHERE id = ? AND date > CURRENT_DATE";
    private final String GET_TEACHER_DAY_TIMETABLE = "SELECT timetable.id, timetable.date, timetable.time_period, teachers.first_name AS teacher_name, teachers.last_name AS teacher_surname, "
            + "lessons.lesson_name, groups.group_name, timetable.room_id FROM timetable.timetable AS timetable\n"
            + "INNER JOIN timetable.teachers AS teachers ON teachers.teacher_id = timetable.teacher_id\n"
            + "INNER JOIN timetable.lessons AS lessons ON lessons.lesson_id = timetable.lesson_id\n"
            + "INNER JOIN timetable.groups AS groups ON groups.group_id = timetable.group_id\n"
            + "WHERE date = ? AND timetable.teacher_id = ?";
    private final String GET_STUDENT_DAY_TIMETABLE = "SELECT timetable.id, timetable.date, timetable.time_period, teachers.first_name AS teacher_name, teachers.last_name AS teacher_surname, "
            + "lessons.lesson_name, groups.group_name, timetable.room_id from timetable.timetable AS timetable\n"
            + "INNER JOIN timetable.teachers AS teachers ON teachers.teacher_id = timetable.teacher_id\n"
            + "INNER JOIN timetable.lessons AS lessons ON lessons.lesson_id = timetable.lesson_id\n"
            + "INNER JOIN timetable.groups AS groups ON groups.group_id = timetable.group_id\n"
            + "WHERE date = ? AND timetable.group_id IN (SELECT group_id FROM timetable.students WHERE student_id = ?)";
    private final String SHOW_TIMETABLE = "SELECT timetable.id, timetable.date, timetable.time_period, teachers.first_name AS teacher_name, teachers.last_name AS teacher_surname,\n"
            + "            lessons.lesson_name, groups.group_name, timetable.room_id from timetable.timetable AS timetable\n"
            + "            INNER JOIN timetable.teachers AS teachers ON teachers.teacher_id = timetable.teacher_id\n"
            + "            INNER JOIN timetable.lessons AS lessons ON lessons.lesson_id = timetable.lesson_id\n"
            + "            INNER JOIN timetable.groups AS groups ON groups.group_id = timetable.group_id\n"
            + "            WHERE date = ? ORDER BY timetable.group_id, timetable.time_period;";
    private int result = 0;
    private static final Logger log = LoggerFactory.getLogger(TimetableDAOImpl.class.getName());

    /**
     * Returns instance of the class
     * 
     * @param jdbcTemplate
     */
    @Autowired
    public TimetableDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int scheduleTimetable(Timetable timetable) throws DAOException {
        log.trace("Start to schedule day timetable");
        int lessonID = timetable.getLesson().getId();
        log.info("Took lesson id {} from inputed DayTimetable", lessonID);
        int groupID = timetable.getGroup().getID();
        log.info("Took group id {} from inputed DayTimetable", groupID);
        Day day = timetable.getDay();
        log.info("Took date {} and lesson time period {}", day.getDateOne(), day.getLessonTimePeriod());
        int teacherID = selectAvailableTeacher(lessonID, day);
        log.info("Took teacher id {} from the method selectAvailableTeacher", teacherID);
        int roomID = selectSuitableRoom(groupID, day);
        log.info("Took room id {} from the method selectAvailableRoom", roomID);
        if (teacherID > 0 && roomID > 0) {
            result = jdbcTemplate.update(SCHEDULE_TIMETABLE, day.getDateOne(), day.getLessonTimePeriod(), lessonID,
                    groupID, teacherID, roomID, day.getDateOne(), day.getLessonTimePeriod(), lessonID, groupID, groupID,
                    lessonID);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTimetable(int timetbleID) {
        log.trace("Delete dayTimetable from the database");
        result = jdbcTemplate.update(DELETE_TIMETABLE, timetbleID);
        log.debug("Return count of deleted rows otherwise returns zero. The result is {}", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Timetable>> getUserTimetable(Day day, User user) {
        Optional<List<Timetable>> resultList = Optional.empty();
        log.trace("Check if dates ({},{}) are not null", day.getDateOne(), day.getDateTwo());
        if (day.getDateOne() != null && day.getDateTwo() != null) {
            resultList = Optional.of(Stream.iterate(day.getDateOne(), date -> date.plusDays(1))
                    .limit(ChronoUnit.DAYS.between(day.getDateOne(), day.getDateTwo().plusDays(1)))
                    .map(date -> getUserDayTimetable(date, user)).filter(Optional::isPresent).map(Optional::get)
                    .collect(Collectors.toList()).stream()
                    .collect(ArrayList<Timetable>::new, List::addAll, List::addAll));

            log.debug("Took list of timetable for the user {} with id {} and date from {} to {}", user.getClass(),
                    user.getId(), day.getDateOne(), day.getDateTwo());
        }
        return resultList;
    }

    private Optional<List<Timetable>> getUserDayTimetable(LocalDate date, User user) {
        Optional<List<Timetable>> resultList = Optional.empty();
        String sql = "";
        log.trace("Check inputed user class {} if instanceof Teacher", user.getClass().getName());
        if (date != null) {
            if (user instanceof Teacher) {
                sql = GET_TEACHER_DAY_TIMETABLE;
                
            } else if (user instanceof Student) {
                log.trace("Check inputed user class {} if instanceof Student", user.getClass().getName());
                sql = GET_STUDENT_DAY_TIMETABLE;
            }
            resultList = Optional
                    .of(jdbcTemplate.query(sql, new Object[] { date, user.getId() }, new TimetableMapper()));
            log.debug("Took timetable for the user - {} with id {} and date {} from the timetable.timetable", user.getClass().getName(),user.getId(),
                    date);
        }
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Timetable>> showTimetable(Day day) {
        Optional<List<Timetable>> resultList = Optional.empty();
        if (day.getDateOne() != null && day.getDateTwo() != null) {
            resultList = Optional.of(Stream.iterate(day.getDateOne(), date -> date.plusDays(1))
                    .limit(ChronoUnit.DAYS.between(day.getDateOne(), day.getDateTwo().plusDays(1)))
                    .map(date -> showDayTimetable(date)).filter(Optional::isPresent).map(Optional::get)
                    .collect(Collectors.toList()).stream()
                    .collect(ArrayList<Timetable>::new, List::addAll, List::addAll));
        }
        return resultList;
    }
    
    private Optional<List<Timetable>> showDayTimetable(LocalDate date) {
        Optional<List<Timetable>> resultList = Optional.empty();
        if (date != null) {
            resultList = Optional.of(jdbcTemplate.query(SHOW_TIMETABLE, new Object[] { date }, new TimetableMapper()));
        }
        return resultList;
    }

    private int selectSuitableRoom(int groupID, Day day) throws DAOException {
        log.trace("Select available room  for date {} and time {}", day.getDateOne(), day.getLessonTimePeriod());
        try {
            result = jdbcTemplate.queryForObject(SELECT_SUITABLE_ROOM,
                    new Object[] { groupID, day.getDateOne(), day.getLessonTimePeriod() }, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            log.error("No available rooms for this time {} and date {}.", day.getLessonTimePeriod(), day.getDateOne());
            throw new DAOException("No available rooms for this time and date. Can't schedule timetable.", e);
        }
        return result;
    }

    private int selectAvailableTeacher(int lessonID, Day day) throws DAOException {
        log.trace("Select available teacher for date {} and time {}", day.getDateOne(), day.getLessonTimePeriod());
        try {
            result = jdbcTemplate.queryForObject(SELECT_AVAILABLE_TEACHER, new Object[] { day.getDateOne(),
                    day.getDateOne(), day.getDateOne(), day.getLessonTimePeriod(), lessonID }, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            log.error("No available teachers for this time {} and date {}.", day.getLessonTimePeriod(),
                    day.getDateOne());
            throw new DAOException("No available teachers for this time and date. Can't schedule timetable.", e);
        }
        return result;
    }
}
