package ua.foxminded.university.dao.implementation.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.DayTimetable;
import ua.foxminded.university.service.pojo.Group;
import ua.foxminded.university.service.pojo.Lesson;
import ua.foxminded.university.service.pojo.Teacher;

public class DayTimetableMapper implements RowMapper<DayTimetable> {

    @Override
    public DayTimetable mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Day day = new Day();
        day.setDateOne(resultSet.getDate("date").toLocalDate());
        day.setLessonTimePeriod(resultSet.getString("time_period"));
        return new DayTimetable.TimetableBuilder().setDay(day)
                .setTeacher(new Teacher.TeacherBuidler().setFirstName(resultSet.getString("teacher_name"))
                        .setLastName(resultSet.getString("teacher_surname")).build())
                .setLesson(new Lesson.LessonBuilder().setName(resultSet.getString("lesson_name")).build())
                .setGroup(new Group.GroupBuilder().setName(resultSet.getString("group_name")).build())
                .setRoomNumber(resultSet.getInt("room_id")).build();
    }
}
