package ua.foxminded.university.service;

import java.util.List;

import ua.foxminded.university.service.pojo.Holiday;

public interface HolidayService {
    /**
     * The method finds all holidays from the database
     * @return optional list of holidays
     */
    List<Holiday> findAllHolidays();
    
    /**
     * The method adds new holiday to the database, if holiday was added returns 1 otherwise 0
     * @param holiday
     * @return 1 or 0
     */
    int addHoliday(Holiday holiday);
    
    /**
     * The method deletes existed holiday by id and returns count of deleted rows
     * @param holidayID
     * @return 1 or 0
     */
    int deleteHoliday(int holidayID);
}
