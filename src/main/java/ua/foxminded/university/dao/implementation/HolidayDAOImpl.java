package ua.foxminded.university.dao.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.HolidayDAO;
import ua.foxminded.university.dao.implementation.mappers.HolidayMapper;
import ua.foxminded.university.service.pojo.Holiday;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Repository
public class HolidayDAOImpl implements HolidayDAO {
    private final JdbcTemplate jdbcTemplate;
    private final String ADD_HOLIDAY = "INSERT INTO timetable.holidays (date, holiday) SELECT ?, ?;";
    private final String FIND_ALL_HOLIDAYS = "SELECT * FROM timetable.holidays ORDER BY id;";
    private final String DELETE_HOLIDAY = "DELETE FROM timetable.holidays WHERE id = ? AND date > CURRENT_DATE;";
    private final String HOLIDAY_NAME_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('holidays') AND UPPER (column_name) = UPPER ('holiday');";
    private static final Logger log = LoggerFactory.getLogger(HolidayDAOImpl.class.getName());
    private final String debugMessage = "Return count of rows otherwise returns zero. The result is {}";
    private int result;
    
    /**
     * Returns instance of the class
     * 
     * @param jdbcTemplate
     */
    @Autowired
    public HolidayDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Holiday>> findAllHolidays() {
        log.trace("Find all holidays from the database");
        Optional<List<Holiday>> result = Optional.of(jdbcTemplate.query(FIND_ALL_HOLIDAYS, new HolidayMapper()));
        log.debug("Return optional list of holidays {}", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addHoliday(Holiday holiday) {
        log.trace("Add new holiday to the database");
        result = jdbcTemplate.update(ADD_HOLIDAY, holiday.getDate(), holiday.getHolidayName());
        log.debug(debugMessage, result);
        return result;
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteHoliday(int holidayID) {
        log.trace("Delete holiday by id - {} if date not before current date", holidayID);
        result = jdbcTemplate.update(DELETE_HOLIDAY, holidayID);
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHolidayNameMaxSize() {
        return jdbcTemplate.queryForObject(HOLIDAY_NAME_MAX_SIZE, Integer.class);
    }
}
