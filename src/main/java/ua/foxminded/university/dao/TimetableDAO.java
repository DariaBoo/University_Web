package ua.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.DayTimetable;
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
     */
    int scheduleTimetable(DayTimetable timetable);

    /**
     * Deletes scheduled timetable by timetable id
     * 
     * @param timetableID
     * @return count of deleted rows otherwise zero
     */
    int deleteTimetable(int timetableID);

    /**
     * Returns Optional DayTimetable by date and user
     * 
     * @param date
     * @param user
     * @return dayTimetable if this user has lessons at this date otherwise
     *         Optional.empty
     */
    Optional<DayTimetable> getDayTimetable(LocalDate date, User user);

    /**
     * Returns list of Optional DayTimetable by date and user
     * 
     * @param day
     * @param user
     * @return list of dayTimetable if this user has lessons at this date otherwise
     *         List of Optional.empty
     */
    List<Optional<DayTimetable>> getMonthTimetable(Day day, User user);

}
