package ua.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.entities.Holiday;

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
     * @throws DAOException 
     */
    int addHoliday(Holiday holiday) throws  DAOException;
    
    /**
     * The method deletes existed holiday by id and returns count of deleted rows
     * @param holidayID
     * @return 1 or 0
     */
    boolean deleteHoliday(int holidayID);
}
