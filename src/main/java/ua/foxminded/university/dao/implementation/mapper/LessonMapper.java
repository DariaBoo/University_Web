package ua.foxminded.university.dao.implementation.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import ua.foxminded.university.service.pojo.Lesson;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public class LessonMapper implements RowMapper<Lesson> {
    private static final Logger log = LoggerFactory.getLogger(LessonMapper.class.getName());
    
    /**
     * Returns rowMapper for Lesson
     */
    @Override
    public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.trace("Create rowMapper for Lesson");
        Lesson lesson = new Lesson();
        lesson.setId(rs.getInt("lesson_id"));
        lesson.setName(rs.getString("lesson_name"));
        lesson.setDescription(rs.getString("description"));
        return lesson;
    }
}
