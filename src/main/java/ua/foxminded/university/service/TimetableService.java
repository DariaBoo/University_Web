package ua.foxminded.university.service;

import java.util.List;

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
public interface TimetableService {
    /**
     * The method schedules lesson, group, teacher and room for the day and lesson
     * time period
     * 
     * @param timetable
     * @return result
     */
    String scheduleTimetable(Timetable timetable);

    /**
     * Returns list of DayTimetable for all groups and teachers by period of dates
     * 
     * @param day
     * @return list of dayTimetables
     */
    List<Timetable> showTimetable(Day day);

    /**
     * Deletes scheduled timetable by timetable id
     * 
     * @param timetableId
     * @return true or false
     */
    void deleteTimetable(int timetableId);

    /**
     * Returns list of Optional DayTimetable by date and teacher
     * 
     * @param day
     * @param teacher
     * @return list of dayTimetable if this user has lessons at this date otherwise
     *         List of Optional.empty
     */
    List<Timetable> getTeacherTimetable(Day day, Teacher teacher);

    /**
     * Returns list of Optional DayTimetable by date and student
     * 
     * @param day
     * @param student
     * @return list of dayTimetable if this user has lessons at this date otherwise
     *         List of Optional.empty
     */
    List<Timetable> getStudentTimetable(Day day, Student student);
}
