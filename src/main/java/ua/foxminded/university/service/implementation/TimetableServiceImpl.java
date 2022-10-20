package ua.foxminded.university.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.TimetableDAO;
import ua.foxminded.university.dao.exception.UniqueConstraintViolationException;
import ua.foxminded.university.service.HolidayService;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.TimetableService;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;
import ua.foxminded.university.service.validator.Notification;
import ua.foxminded.university.service.validator.TimetableValidator;

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
    private HolidayService holidayService;
    @Autowired
    private TeacherService teacherService;

    private TimetableValidator validator;
    private static final String illegalArgumentExceptionMessage = "No timetable for ";

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String scheduleTimetable(Timetable timetable) {
        validator = new TimetableValidator(holidayService, teacherService);
        Notification notification = new Notification();
        try {
            notification = validator.validateTimetable(timetable);
            if (!notification.hasErrors()) {
                timetableDAO.saveAndFlush(timetable);
                log.debug("Timetable [date::{}, time::{}] is scheduled", timetable.getDate(),
                        timetable.getLessonTimePeriod());
            } else {
                return notification.getErrors();
            }
        } catch (DataIntegrityViolationException exception) {
            notification = validator.validateUniqueConstraint(exception, timetable);
            throw new UniqueConstraintViolationException(notification.getErrors(), exception);
        }
        return "Timetable was scheduled!!!";
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
}
