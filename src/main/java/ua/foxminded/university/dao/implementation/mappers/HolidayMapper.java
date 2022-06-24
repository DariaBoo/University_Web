package ua.foxminded.university.dao.implementation.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import ua.foxminded.university.service.pojo.Holiday;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public class HolidayMapper implements RowMapper<Holiday>{
    private static final Logger log = LoggerFactory.getLogger(HolidayMapper.class.getName());

    /**
     * Returns rowMapper for Holiday
     */
    @Override
    public Holiday mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.trace("Create rowMapper for Holiday");
        Holiday holiday = new Holiday();
        holiday.setId(rs.getInt("id"));
        holiday.setDate(rs.getDate("date").toLocalDate());
        holiday.setHolidayName(rs.getString("holiday"));
        return holiday;
    }
}
