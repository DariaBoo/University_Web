package ua.foxminded.university.service.implementation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.HolidayDAO;
import ua.foxminded.university.dao.TimetableDAO;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.TimetableService;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;
import ua.foxminded.university.service.exception.ServiceException;
import ua.foxminded.university.service.validator.EntityValidator;

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

    private EntityValidator validator;

    private static final String illegalArgumentExceptionMessage = "No timetable for ";
    private static final String uniqueGroupDateTime = "unique_group_date_time";
    private static final String uniqueTeacherDateTime = "unique_teacher_date_time";
    private static final String uniqueRoomDateTime = "unique_room_date_time";

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String scheduleTimetable(Timetable timetable) {        
        validateTimetable(timetable);
        try {
            if (!validator.hasErrors()) {
                timetableDAO.saveAndFlush(timetable);
                log.debug("Timetable [date::{}, time::{}] is scheduled", timetable.getDate(),
                        timetable.getLessonTimePeriod());
            } else {
                return validator.getErrors();
            }
        } catch (DataIntegrityViolationException exception) {
            validateUniqueConstraint(exception, timetable);
            throw new ServiceException(validator.getErrors(), exception);
        }
        return "Timetable was scheduled!!!";
    }

    private void recordError(String error) {        
        log.error(error);
        validator.addError(error);
    }

    private void validateTimetable(Timetable timetable) {
        validator = new EntityValidator();
        if (timetable.getGroup().getId() < 0) {
            recordError("Group is missing");
        }
        if (timetable.getLesson() == null) {
            recordError("Lesson is missing");
        }        
        validateDate(timetable);
        
        if (timetable.getLessonTimePeriod().isEmpty() || timetable.getLessonTimePeriod() == null) {
            recordError("Lesson time is missing");
        }        
        validateTeacher(timetable);
        
        if (timetable.getRoom().getNumber() == 0) {
            recordError("Room is missing");
        }
    }

    private void validateDate(Timetable timetable) {
        if (timetable.getDate() == null) {
            recordError("Date is missing");
            return;
        }
        LocalDate day = timetable.getDate();
        if (isWeekend(day)) {
            recordError(day + " is a weekend. Can't schedule timetable!");
        }
        if (isHoliday(day)) {
            recordError(day + " is a holiday. Can't schedule timetable!");
        }
    }

    private void validateTeacher(Timetable timetable) {
        int teacherId = timetable.getTeacher().getId();
        if (teacherId == 0) {
            recordError("Teacher is missing");
        } else if (teacherService.checkIsAbsent(timetable.getDate(), teacherId)) {
            recordError("Teacher [id::" + timetable.getTeacher().getId() + "] is absent [date::" + timetable.getDate()
                    + "].");
        }
    }

    private void validateUniqueConstraint(DataIntegrityViolationException exception, Timetable timetable) {
        validator = new EntityValidator();
        String message = exception.getMostSpecificCause().getMessage();
        if (message.contains(uniqueGroupDateTime)) {
            recordError("Group with id: " + timetable.getGroup().getId() + " is already scheduled (date:"
                    + timetable.getDate() + ", time:" + timetable.getLessonTimePeriod() + ")!");
        } else if (message.contains(uniqueTeacherDateTime)) {
            recordError("Teacher with id: " + timetable.getTeacher().getId() + " is already scheduled ("
                    + timetable.getDate() + ", " + timetable.getLessonTimePeriod() + ")!");
        } else if (message.contains(uniqueRoomDateTime)) {
            recordError("Room number: " + timetable.getRoom().getNumber() + " is already scheduled ("
                    + timetable.getDate() + ", " + timetable.getLessonTimePeriod() + ")!");
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
