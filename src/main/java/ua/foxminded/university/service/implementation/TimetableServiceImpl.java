package ua.foxminded.university.service.implementation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.HolidayDAO;
import ua.foxminded.university.dao.TimetableDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.dao.exception.UniqueConstraintViolationException;
import ua.foxminded.university.service.TimetableService;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;
import ua.foxminded.university.service.exception.ServiceException;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Slf4j
@Service
public class TimetableServiceImpl implements TimetableService {

    @Autowired
    private TimetableDAO timetableDAO;
    @Autowired
    private HolidayDAO holidayDAO;
    
    private final String illegalArgumentExceptionMessage = "No timetable for ";

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int scheduleTimetable(Timetable timetable) {
        LocalDate day = timetable.getDate();
        int result = 0;
        log.trace("Check if a day - {} is not a weekend", day);
        if (isWeekend(day)) {
            log.error("Can't schedule timetable for weekend. Attempt to schedule {}.", day);
            throw new ServiceException("Can't schedule timetable for weekend! Attempt to schedule (" + day + ")");
        }
        log.trace("Check if a day - {} is not a holiday", day);
        if (isHoliday(day)) {
            log.error("Can't schedule timetable for holiday. Attempt to schedule {}", day);
            throw new ServiceException("Can't schedule timetable for holiday! Attempt to schedule (" + day + ")");
        }
        try {
            result = timetableDAO.scheduleTimetable(timetable);
            log.debug("Took the result - {} of shceduling timetable.", result);
        } catch (UniqueConstraintViolationException | NoSuchElementException | DAOException e) {
            log.error(e.getMessage(), e.getCause());
            throw new ServiceException(e.getMessage());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteTimetable(int timetableID) {
        return timetableDAO.deleteTimetable(timetableID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Timetable> getTeacherTimetable(Day day, Teacher teacher) {
        List<Timetable> resultList = timetableDAO.getTeacherTimetable(day, teacher).orElseThrow(() -> new IllegalArgumentException(
                illegalArgumentExceptionMessage + day.getDateOne() + " - " + day.getDateTwo()));
        log.debug("Get month timetable for user {} and return a list of timetable - {}", teacher, resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Timetable> getStudentTimetable(Day day, Student student) {
        List<Timetable> resultList = timetableDAO.getStudentTimetable(day, student).orElseThrow(() -> new IllegalArgumentException(
                illegalArgumentExceptionMessage + day.getDateOne() + " - " + day.getDateTwo()));
        log.debug("Get month timetable for user {} and return a list of timetable - {}", student, resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Timetable> showTimetable(Day day) {
        List<Timetable> resultList = timetableDAO.showTimetable(day).orElseThrow(() -> new IllegalArgumentException(
                illegalArgumentExceptionMessage + day.getDateOne() + " - " + day.getDateTwo()));
        log.debug("Return a list of timetable - {}", resultList);
        return resultList;
    }

    private boolean isWeekend(LocalDate date) {
        log.trace("Check is input date - {} is weekend", date);
        DayOfWeek dayOfWeek = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
        switch (dayOfWeek) {
        case SATURDAY:
            log.info("Input date - {} is saturday", date);
            return true;
        case SUNDAY:
            log.info("Input date - {} is sunday", date);
            return true;
        }
        return false;
    }

    private boolean isHoliday(LocalDate day) {
        boolean result = false;
        log.debug("Take a list of holidays from the database");
        if (holidayDAO.findAllHolidays().isPresent()) {
            result = holidayDAO.findAllHolidays().get().stream()
                    .anyMatch(holiday -> holiday.getDate().isEqual(day));
            log.info("Input day - {} is a holiday", day);
        }
        return result;
    }
}
