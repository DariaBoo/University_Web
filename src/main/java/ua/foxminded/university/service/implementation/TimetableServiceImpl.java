package ua.foxminded.university.service.implementation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.university.dao.implementation.HolidayDAOImpl;
import ua.foxminded.university.dao.implementation.TimetableDAOImpl;
import ua.foxminded.university.service.TimetableService;
import ua.foxminded.university.service.exception.ServiceException;
import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.DayTimetable;
import ua.foxminded.university.service.pojo.User;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Service
public class TimetableServiceImpl implements TimetableService {
    private final TimetableDAOImpl timetableDAOImpl;
    private final HolidayDAOImpl holidayDAOImpl;
    private static final Logger log = LoggerFactory.getLogger(TimetableServiceImpl.class.getName());

    /**
     * Returns instance of the class
     * @param timetableDAOImpl
     * @param holidayDAOImpl
     */
    @Autowired
    public TimetableServiceImpl(TimetableDAOImpl timetableDAOImpl, HolidayDAOImpl holidayDAOImpl) {
        this.timetableDAOImpl = timetableDAOImpl;
        this.holidayDAOImpl = holidayDAOImpl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int scheduleTimetable(DayTimetable timetable) throws ServiceException {
        log.trace("Schedule day timetable");
        LocalDate day = timetable.getDay().getDateOne();
        log.trace("Check if a day - {} is not a weekend", day);
        if (isWeekend(timetable.getDay().getDateOne())) {
            throw new ServiceException("Can't schedule timetable for weedend");
        }
        log.debug("Take a list of holidays from the database");
        if (holidayDAOImpl.findAllHolidays().isPresent()) {
            log.trace("Check if a day - {} is not a holiday", day);
            if (holidayDAOImpl.findAllHolidays().get().stream().anyMatch(holiday -> holiday.getDate().isEqual(day))) {
                throw new ServiceException("Can't schedule timetable for holiday");
            }
        }
        log.info("Schedule timetable");
        return timetableDAOImpl.scheduleTimetable(timetable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTimetable(int timetableID) {//TODO delete by day and time_period
        log.trace("Delete timetable by id");
        return timetableDAOImpl.deleteTimetable(timetableID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DayTimetable> getDayTimetable(LocalDate date, User user) {
        log.trace("Get day timetable for user {}", user);
        return timetableDAOImpl.getDayTimetable(date, user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Optional<DayTimetable>> getMonthTimetable(Day day, User user) {
        log.trace("Get month timetable for user {}", user);
        return timetableDAOImpl.getMonthTimetable(day, user);
    }

    
    private boolean isWeekend(LocalDate date) {
        log.trace("Check is input date - {] is weekend", date);
        DayOfWeek dayOfWeek = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
        switch (dayOfWeek) {
        case SATURDAY:
            log.trace("Input date - {} is saturday", date);
            return true;
        case SUNDAY:
            log.trace("Input date - {} is sunday", date);
            return true;
        }
        return false;
    }
}
