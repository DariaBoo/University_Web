package ua.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.Timetable;
import ua.foxminded.university.service.pojo.User;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public interface TimetableDAO {

    /**
     * The method schedules lesson, group, teacher and room for the day and lesson
     * time period
     * 
     * @param timetable includes lesson id, group id, date and lesson time period
     * @return count of added rows otherwise -1
     * @throws DAOException 
     */
    int scheduleTimetable(Timetable timetable) throws DAOException;

    /**
     * Deletes scheduled timetable by timetable id
     * 
     * @param timetableID
     * @return count of deleted rows otherwise zero
     */
    int deleteTimetable(int timetableID);

    /**
     * Returns list of Optional DayTimetable by date and user
     * 
     * @param day
     * @param user
     * @return list of dayTimetable if this user has lessons at this date otherwise
     *         List of Optional.empty
     */
    Optional<List<Timetable>> getUserTimetable(Day day, User user);
    
    /**
     * Returns list of DayTimetable for all groups and teachers by period of dates
     * @param day
     * @return list of dayTimetable
     */
    Optional<List<Timetable>> showTimetable(Day day);
}
