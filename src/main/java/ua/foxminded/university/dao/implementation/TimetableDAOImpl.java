package ua.foxminded.university.dao.implementation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.foxminded.university.dao.TimetableDAO;
import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.DayTimetable;
import ua.foxminded.university.service.pojo.Student;
import ua.foxminded.university.service.pojo.Teacher;
import ua.foxminded.university.service.pojo.User;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Component("timetable")
public class TimetableDAOImpl implements TimetableDAO {
    private final JdbcTemplate jdbcTemplate;
    private final GroupDAOImpl groupDAOImpl;
    private final LessonDAOImpl lessonDAOImpl;
    private final String SET_TIMETABLE = "INSERT INTO timetable.timetable (date, time_period, lesson_id, group_id, teacher_id, room_id) VALUES (?, ?, ?, ?, ?, ?)";
    private final String IS_LESSON_GROUP_SCHEDULED = "SELECT COUNT(*) FROM timetable.timetable WHERE date = ? AND time_period = ? AND lesson_id = ? AND group_id = ?";
    private final String SELECT_SUITABLE_ROOMS = "SELECT room_id FROM timetable.rooms WHERE capacity >= (SELECT COUNT(*) FROM timetable.students WHERE group_id = ?) ORDER BY room_id";
    private final String IS_ROOM_SCHEDULED = "SELECT COUNT(*) FROM timetable.timetable WHERE date = ? AND time_period = ? AND room_id = ?";
    private final String SELECT_TEACHERS = "SELECT teacher_id  FROM timetable.lessons_teachers WHERE lesson_id = ? ORDER BY teacher_id";
    private final String IS_TEACHER_NOT_ABSENT = "SELECT COUNT(*) FROM timetable.teacherAbsent WHERE teacher_id = ? AND date_start <= ? AND date_end >= ?;";
    private final String IS_TEACHER_SCHEDULED = "SELECT COUNT(*) FROM timetable.timetable WHERE date = ? AND time_period = ? AND teacher_id = ?";
    private final String DELETE_TIMETABLE = "DELETE FROM timetable.timetable WHERE id = ? AND date >= ?";
    private final String GET_TEACHER_DAY_TIMETABLE = "SELECT timetable.date, timetable.time_period, teachers.first_name AS teacher_name, teachers.last_name AS teacher_surname, lessons.lesson_name, groups.group_name, timetable.room_id from timetable.timetable AS timetable\n"
            + "INNER JOIN timetable.teachers AS teachers ON teachers.teacher_id = timetable.teacher_id\n"
            + "INNER JOIN timetable.lessons AS lessons ON lessons.lesson_id = timetable.lesson_id\n"
            + "INNER JOIN timetable.groups AS groups ON groups.group_id = timetable.group_id\n"
            + "WHERE date = ? AND timetable.teacher_id = ?";
    private final String GET_STUDENT_DAY_TIMETABLE = "SELECT timetable.date, timetable.time_period, teachers.first_name AS teacher_name, teachers.last_name AS teacher_surname, lessons.lesson_name, groups.group_name, timetable.room_id from timetable.timetable AS timetable\n"
            + "INNER JOIN timetable.teachers AS teachers ON teachers.teacher_id = timetable.teacher_id\n"
            + "INNER JOIN timetable.lessons AS lessons ON lessons.lesson_id = timetable.lesson_id\n"
            + "INNER JOIN timetable.groups AS groups ON groups.group_id = timetable.group_id\n"
            + "WHERE date = ? AND timetable.group_id IN (SELECT group_id FROM timetable.students WHERE student_id = ?)";
    private static final Logger log = LoggerFactory.getLogger(TimetableDAOImpl.class.getName());

    /**
     * Returns instance of the class
     * 
     * @param jdbcTemplate
     * @param groupDAOImpl
     * @param lessonDAOImpl
     */
    @Autowired
    public TimetableDAOImpl(JdbcTemplate jdbcTemplate, GroupDAOImpl groupDAOImpl, LessonDAOImpl lessonDAOImpl) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupDAOImpl = groupDAOImpl;
        this.lessonDAOImpl = lessonDAOImpl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int scheduleTimetable(DayTimetable timetable) {
        log.trace("Start to schedule day timetable");
        int result = -1;
        int lessonID = timetable.getLesson().getId();
        log.info("Took lesson id {} from inputed DayTimetable", lessonID);
        int groupID = timetable.getGroup().getID();
        log.info("Took group id {} from inputed DayTimetable", groupID);
        Day day = timetable.getDay();
        log.info("Check if lesson with id {} and group with id {} are exist in the database and if group has lesson",
                lessonID, groupID);
        if (lessonDAOImpl.isLessonExists(lessonID) && groupDAOImpl.isGroupExists(groupID)
                && groupDAOImpl.isGroupHasLesson(groupID, lessonID)) {
            log.info("Check if lesson and group is already scheduled in the timetable");
            boolean isLessonGroupScheduled = isLessonGroupScheduled(timetable);
            log.info("Take teacher id from the method findAvailableTeacher");
            int teacherID = findAvailableTeacher(lessonID, day);
            log.info("Take room id from the method findAvailableRoom");
            int roomID = findAvailableRoom(groupID, day);
            log.info(
                    "Check if chosen teacher id {} and gruop id {} are greater then 0 and check if lesson and group is already scheduled in the timetable",
                    teacherID, roomID);
            if (!isLessonGroupScheduled && teacherID > 0 && roomID > 0) {
                result = jdbcTemplate.update(SET_TIMETABLE, day.getDateOne(), day.getLessonTimePeriod(), lessonID,
                        groupID, teacherID, roomID);
                log.debug("Executed sql to add dayTimetable and took the result {}", result);
            } else {
                log.info(
                        "Teacher or room is not available in this day and time or this group already scheduled with this lesson");
            }
        } else {
            log.info("Lesson or group is not valid or this group doesn't have this lesson");
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTimetable(int timetbleID) {
        log.debug(
                "Execute sql to delete dayTimetable from the database and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_TIMETABLE, timetbleID, LocalDate.now());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DayTimetable> getDayTimetable(LocalDate date, User user) {
        Optional<DayTimetable> timetable = Optional.empty();
        log.info("Check inputed user class {} if instanceof Teacher", user.getClass().getName());
        if (user instanceof Teacher) {
            timetable = Optional.ofNullable(jdbcTemplate
                    .query(GET_TEACHER_DAY_TIMETABLE, new Object[] { date, user.getId() }, new DayTimetableMapper())
                    .stream().findAny().orElse(null));
            log.debug("Took timetable for the teacher {} and date {} from the timetable.timetable", user.getId(), date);
        } else if (user instanceof Student) {
            log.info("Check inputed user class {} if instanceof Student", user.getClass().getName());
            timetable = Optional.ofNullable(jdbcTemplate
                    .query(GET_STUDENT_DAY_TIMETABLE, new Object[] { date, user.getId() }, new DayTimetableMapper())
                    .stream().findAny().orElse(null));
            log.debug("Took timetable for the student {} and date {} from the timetable.timetable", user.getId(), date);
        }
        return timetable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Optional<DayTimetable>> getMonthTimetable(Day day, User user) {
        log.debug("Took list of timetable for the user {} with id {} and date from {} to {}", user.getClass(),
                user.getId(), day.getDateOne(), day.getDateTwo());
        return Stream.iterate(day.getDateOne(), date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(day.getDateOne(), day.getDateTwo().plusDays(1)))
                .map(date -> getDayTimetable(date, user)).collect(Collectors.toList());
    }

    private boolean isLessonGroupScheduled(DayTimetable timetable) {
        int lessonID = timetable.getLesson().getId();
        log.info("Took lesson id {} from inputed DayTimetable", lessonID);
        int groupID = timetable.getGroup().getID();
        log.info("Took group id {} from inputed DayTimetable", groupID);
        Day day = timetable.getDay();
        log.debug("Check is lesson and group are already scheduled at the date {} and time period {}", day.getDateOne(),
                day.getLessonTimePeriod());
        return jdbcTemplate.queryForObject(IS_LESSON_GROUP_SCHEDULED,
                new Object[] { day.getDateOne(), day.getLessonTimePeriod(), lessonID, groupID }, Boolean.class);
    }

    private int findAvailableRoom(int groupID, Day day) {
        log.debug("Chose suitable rooms by group size and select available rooms for date {} and time period {}",
                day.getDateOne(), day.getLessonTimePeriod());
        return getSuitableRooms(groupID).stream()
                .filter(id -> (jdbcTemplate.queryForObject(IS_ROOM_SCHEDULED,
                        new Object[] { day.getDateOne(), day.getLessonTimePeriod(), id }, Integer.class) == 0))
                .findFirst().orElse(-1);
    }

    private List<Integer> getSuitableRooms(int groupID) {
        log.debug("Chose suitable rooms by group size");
        return jdbcTemplate.queryForList(SELECT_SUITABLE_ROOMS, new Object[] { groupID }, Integer.class);
    }

    public int findAvailableTeacher(int lessonID, Day day) {
        List<Integer> teachers = jdbcTemplate.queryForList(SELECT_TEACHERS, new Object[] { lessonID }, Integer.class);
        log.debug("Select teachers {} by lesson", teachers);
        log.debug("Chose available teachers from the list {} for the date {} and time period {}", teachers,
                day.getDateOne(), day.getLessonTimePeriod());
        return teachers.stream().filter(id -> (!isTeacherAbsent(id, day)))
                .filter(id -> (jdbcTemplate.queryForObject(IS_TEACHER_SCHEDULED,
                        new Object[] { day.getDateOne(), day.getLessonTimePeriod(), id }, Integer.class) == 0))
                .findFirst().orElse(-1);
    }

    private boolean isTeacherAbsent(int teacherID, Day day) {
        log.trace("Check if teacher {} is not absent at the date {}", teacherID, day.getDateOne());
        boolean result = false;
        int countOfRows = jdbcTemplate.queryForObject(IS_TEACHER_NOT_ABSENT,
                new Object[] { teacherID, day.getDateOne(), day.getDateOne() }, Integer.class);
        log.debug("Executed sql to take count of rows {} in the timetable.teacherabsent with teacher id {} and date {}",
                countOfRows, teacherID, day.getDateOne());
        if (countOfRows != 0) {
            result = true;
            log.info("Teacher with id {} and for date {} is absent", teacherID, day.getDateOne());
        }
        log.info("Teacher with id {} and for date {} is not absent", teacherID, day.getDateOne());
        return result;
    }
}
