package ua.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public interface TimetableDAO extends JpaRepository<Timetable, Integer> {

    
    /**
     * Returns list of DayTimetable for all groups and teachers by period of dates
     * @param day
     * @return list of dayTimetable
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("SELECT t FROM Timetable t JOIN FETCH t.group JOIN FETCH t.room JOIN FETCH t.teacher JOIN FETCH t.lesson WHERE t.date >= ?1 AND t.date <= ?2")
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
