package ua.foxminded.university.service;

import java.util.List;

import ua.foxminded.university.service.entities.Holiday;

public interface HolidayService {
    /**
     * The method finds all holidays from the database
     * @return optional list of holidays
     */
    List<Holiday> findAllHolidays();
    
    /**
     * The method adds new holiday to the database, if holiday was added returns 1 otherwise 0
     * @param holiday
     * @return is added true, otherwise - false
     */
    boolean addHoliday(Holiday holiday);
    
    /**
     * The method deletes existed holiday by id and returns count of deleted rows
     * @param holidayId
     * @return is deleted true, otherwise - false
     */
    boolean deleteHoliday(int holidayId);
}
