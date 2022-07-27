package ua.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Repository
public interface TimetableDAO extends JpaRepository<Timetable, Integer> {

    
    /**
     * Returns list of DayTimetable for all groups and teachers by period of dates
     * @param day
     * @return list of dayTimetable
     */
    @Query("SELECT t FROM Timetable t WHERE t.date >= ?1 AND t.date <= ?2")
    Optional<List<Timetable>> findByDate(LocalDate dateStart, LocalDate dateEnd);
    
    /**
     * Returns list of Optional DayTimetable by date and teacher
     * 
     * @param day
     * @param teacher
     * @return list of dayTimetable if this user has lessons at this date otherwise
     *         List of Optional.empty
     */
    @Query("SELECT t FROM Timetable t WHERE t.date >= ?1 AND t.date <= ?2 AND t.teacher = ?3")
    Optional<List<Timetable>> findByDateAndTeacher(LocalDate dateStart, LocalDate dateEnd, Teacher teacher);

    /**
     * Returns list of Optional DayTimetable by date and student
     * 
     * @param day
     * @param student
     * @return list of dayTimetable if this user has lessons at this date otherwise
     *         List of Optional.empty
     */
    @Query("SELECT t FROM Timetable t WHERE t.date >= ?1 AND t.date <= ?2 AND t.group = ?3")
    Optional<List<Timetable>> findByDateAndGroup(LocalDate dateStart, LocalDate dateEnd, Group group);
}
