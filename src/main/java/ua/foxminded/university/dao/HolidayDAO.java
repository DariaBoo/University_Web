package ua.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import ua.foxminded.university.service.pojo.Holiday;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public interface HolidayDAO {

    /**
     * The method finds all holidays from the database
     * @return optional list of holidays
     */
    Optional<List<Holiday>> findAllHolidays();
    
    /**
     * The method adds new holiday to the database, if holiday was added returns 1 otherwise 0
     * @param holiday
     * @return 1 or 0
     */
    int addHoliday(Holiday holiday);
    
    /**
     * Returns column 'holiday' max size
     * @return column size
     */
    int getHolidayNameMaxSize();
}
