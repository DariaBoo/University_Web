package ua.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;

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
     * @return count of added rows otherwise 0
     */
    int scheduleTimetable(Timetable timetable);

    /**
     * Deletes scheduled timetable by timetable id
     * 
     * @param timetableID
     * @return count of deleted rows otherwise zero
     */
    boolean deleteTimetable(int timetableID);
    
    /**
     * Returns list of DayTimetable for all groups and teachers by period of dates
     * @param day
     * @return list of dayTimetable
     */
    Optional<List<Timetable>> showTimetable(Day day);
    
    /**
     * Returns list of Optional DayTimetable by date and teacher
     * 
     * @param day
     * @param teacher
     * @return list of dayTimetable if this user has lessons at this date otherwise
     *         List of Optional.empty
     */
    Optional<List<Timetable>> getTeacherTimetable(Day day, Teacher teacher);

    /**
     * Returns list of Optional DayTimetable by date and student
     * 
     * @param day
     * @param student
     * @return list of dayTimetable if this user has lessons at this date otherwise
     *         List of Optional.empty
     */
    Optional<List<Timetable>> getStudentTimetable(Day day, Student student);
}
