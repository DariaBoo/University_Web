package ua.foxminded.university.service.implementation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.HolidayDAO;
import ua.foxminded.university.dao.TimetableDAO;
import ua.foxminded.university.dao.exception.UniqueConstraintViolationException;
import ua.foxminded.university.service.TeacherService;
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
    @Autowired
    private TeacherService teacherService;

    private final String illegalArgumentExceptionMessage = "No timetable for ";
    private static final String uniqueGroupDateTime = "unique_group_date_time";
    private static final String uniqueTeacherDateTime = "unique_teacher_date_time";
    private static final String uniqueRoomDateTime = "unique_room_date_time";

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Timetable scheduleTimetable(Timetable timetable) {
        LocalDate day = timetable.getDate();
        Timetable savedTimetable = new Timetable();
        int teacherId = timetable.getTeacher().getId();
        if (isWeekend(day)) {
            log.error("Can't schedule timetable for weekend. Attempt to schedule {}.", day);
            throw new ServiceException("Can't schedule timetable for weekend! Attempt to schedule (" + day + ")");
        }
        if (isHoliday(day)) {
            log.error("Can't schedule timetable for holiday. Attempt to schedule {}", day);
            throw new ServiceException("Can't schedule timetable for holiday! Attempt to schedule (" + day + ")");
        }
        if (teacherId != 0 && teacherService.checkIsAbsent(timetable.getDate(), teacherId)) {
            log.error("Teacher [id::{}] is absent [date::{}]", timetable.getTeacher().getId(), timetable.getDate());
            throw new NoSuchElementException("Teacher [id::" + timetable.getTeacher().getId() + "] is absent [date::"
                    + timetable.getDate() + "]. Try again.");
        }
        try {
            if(timetable.getGroup().getId() != 0) {
            savedTimetable = timetableDAO.saveAndFlush(timetable);
            log.debug("Timetable [date::{}, time::{}] is scheduled", savedTimetable.getDate(),
                    savedTimetable.getLessonTimePeriod());
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throwCorrectException(e, timetable);
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
                if (violation != null) {
                    log.error(violation.getMessageTemplate());
                    throw new ServiceException(violation.getMessageTemplate());
                }
            }
        }
        return savedTimetable;
    }

    private void throwCorrectException(DataIntegrityViolationException exception, Timetable timetable) {
        if (exception.getMostSpecificCause().getMessage().contains(uniqueGroupDateTime)) {
            log.error(
                    "ConstraintViolationException: date - {}, lesson time period - {}, group id - {} violate the unique primary keys condition - {}",
                    timetable.getDate(), timetable.getLessonTimePeriod(), timetable.getGroup().getId(),
                    uniqueGroupDateTime);
            throw new UniqueConstraintViolationException(
                    "Group with id: " + timetable.getGroup().getId() + " is already scheduled (date:"
                            + timetable.getDate() + ", time:" + timetable.getLessonTimePeriod() + ")!",
                    exception);
        } else if (exception.getMostSpecificCause().getMessage().contains(uniqueTeacherDateTime)) {
            log.error(
                    "ConstraintViolationException: date - {}, lesson time period - {}, teacher id - {} violate the unique primary keys condition - {}",
                    timetable.getDate(), timetable.getLessonTimePeriod(), timetable.getTeacher().getId(),
                    uniqueTeacherDateTime);
            throw new UniqueConstraintViolationException("Teacher with id: " + timetable.getTeacher().getId()
                    + " is already scheduled (" + timetable.getDate() + ", " + timetable.getLessonTimePeriod() + ")!",
                    exception);
        } else if (exception.getMostSpecificCause().getMessage().contains(uniqueRoomDateTime)) {
            log.error(
                    "ConstraintViolationException: date - {}, lesson time period - {}, room id - {} violate the unique primary keys condition - {}",
                    timetable.getDate(), timetable.getLessonTimePeriod(), timetable.getRoom().getNumber(),
                    uniqueRoomDateTime);
            throw new UniqueConstraintViolationException("Room number: " + timetable.getRoom().getNumber()
                    + " is already scheduled (" + timetable.getDate() + ", " + timetable.getLessonTimePeriod() + ")!",
                    exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteTimetable(int timetableId) {
        timetableDAO.deleteById(timetableId);
        return timetableDAO.existsById(timetableId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Timetable> getTeacherTimetable(Day day, Teacher teacher) {
        List<Timetable> resultList = timetableDAO.findByDateAndTeacher(day.getDateOne(), day.getDateTwo(), teacher)
                .orElseThrow(() -> new IllegalArgumentException(
                        illegalArgumentExceptionMessage + day.getDateOne() + " - " + day.getDateTwo()));
        log.debug("Found timetable [count::{}] for user [id::{}]", resultList.size(), teacher.getId());
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Timetable> getStudentTimetable(Day day, Student student) {
        List<Timetable> resultList = timetableDAO
                .findByDateAndGroup(day.getDateOne(), day.getDateTwo(), student.getGroup())
                .orElseThrow(() -> new IllegalArgumentException(
                        illegalArgumentExceptionMessage + day.getDateOne() + " - " + day.getDateTwo()));
        log.debug("Found timetable [count::{}] for student [id::{}]", resultList.size(), student.getId());
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<Timetable> showTimetable(Day day) {
        List<Timetable> resultList = timetableDAO.findByDate(day.getDateOne(), day.getDateTwo())
                .orElseThrow(() -> new IllegalArgumentException(
                        illegalArgumentExceptionMessage + day.getDateOne() + " - " + day.getDateTwo()));
        log.debug("Found timetable [count::{}]", resultList.size());
        return resultList;
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
        switch (dayOfWeek) {
        case SATURDAY:
            log.info("Inputed date [{}] is saturday", date);
            return true;
        case SUNDAY:
            log.info("Inputed date [{}] is sunday", date);
            return true;
        default:
            log.info("Inputed date [{}] is week day", date);
        }
        return false;
    }

    private boolean isHoliday(LocalDate day) {
        boolean result = false;
        if (!holidayDAO.findAll().isEmpty()) {
            result = holidayDAO.findAll().stream().anyMatch(holiday -> holiday.getDate().isEqual(day));
        }
        return result;
    }
}
