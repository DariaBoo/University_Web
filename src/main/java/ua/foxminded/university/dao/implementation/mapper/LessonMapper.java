package ua.foxminded.university.dao.implementation.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ua.foxminded.university.service.pojo.Lesson;

public class LessonMapper implements RowMapper<Lesson> {

    @Override
    public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(rs.getInt("lesson_id"));
        lesson.setName(rs.getString("lesson_name"));
        lesson.setDescription(rs.getString("description"));
        return lesson;
    }
}
