package ua.foxminded.university.dao.implementation.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.Timetable;
import ua.foxminded.university.service.pojo.Group;
import ua.foxminded.university.service.pojo.Lesson;
import ua.foxminded.university.service.pojo.Teacher;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public class TimetableMapper implements RowMapper<Timetable> {

    private static final Logger log = LoggerFactory.getLogger(TimetableMapper.class.getName());

    /**
     * Returns rowMapper for DayTimetable
     */
    @Override
    public Timetable mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        log.trace("Create rowMapper for DayTimetable");
        Day day = new Day();
        day.setDateOne(resultSet.getDate("date").toLocalDate());
        day.setLessonTimePeriod(resultSet.getString("time_period"));
        return new Timetable.TimetableBuilder().setId(resultSet.getInt("id")).setDay(day)
                .setTeacher(new Teacher.TeacherBuidler().setFirstName(resultSet.getString("teacher_name"))
                        .setLastName(resultSet.getString("teacher_surname")).build())
                .setLesson(new Lesson.LessonBuilder().setName(resultSet.getString("lesson_name")).build())
                .setGroup(new Group.GroupBuilder().setName(resultSet.getString("group_name")).build())
                .setRoomNumber(resultSet.getInt("room_id")).build();
    }
}
