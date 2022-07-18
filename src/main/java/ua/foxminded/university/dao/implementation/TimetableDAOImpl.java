package ua.foxminded.university.dao.implementation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.TeacherDAO;
import ua.foxminded.university.dao.TimetableDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.dao.exception.UniqueConstraintViolationException;
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
@Repository
public class TimetableDAOImpl implements TimetableDAO {

    private static final String debugMessage = "Get current session - {}";
    private static final String dateStart = "dateStart";
    private static final String dateEnd = "dateEnd";
    private static final Logger log = LoggerFactory.getLogger(TimetableDAOImpl.class.getName());
    private static final String uniqueGroupDateTime = "unique_group_date_time";
    private static final String uniqueTeacherDateTime = "unique_teacher_date_time";
    private static final String uniqueRoomDateTime = "unique_room_date_time";

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private TeacherDAO teacherDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public int scheduleTimetable(Timetable timetable) throws DAOException {
        int result = 0;
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        if (!teacherDAO.checkIsAbsent(timetable.getTeacher(), timetable.getDate())) {
            try {
                result = (int) currentSession.save(timetable);
                log.debug("Schedule timetable - {} and return timeatable id - {}", timetable, result);
            } catch (ConstraintViolationException exception) {
                SQLException sqlException = exception.getSQLException();
                throwCorrectException(sqlException, timetable);
                log.error("Error occured: {}, message: {}", exception, exception.getMessage());
                throw new DAOException("Error occured: " + exception + " message: " + exception.getMessage());
            }
        } else {
            log.error("The teacher is absent - {}", timetable.getDate());
            throw new NoSuchElementException("The teacher is absent - " + timetable.getDate());
        }
        return result;
    }

    private void throwCorrectException(SQLException sqlException, Timetable timetable) {
        if (sqlException != null) {
            if (sqlException.toString().contains(uniqueGroupDateTime)) {
                log.error(
                        "ConstraintViolationException: date - {}, lesson time period - {}, group id - {} violate the unique primary keys condition - {}",
                        timetable.getDate(), timetable.getLessonTimePeriod(), timetable.getGroup().getId(),
                        uniqueGroupDateTime);
                throw new UniqueConstraintViolationException(
                        "The group with id: " + timetable.getGroup().getId() + " is already scheduled ("
                                + timetable.getDate() + ", " + timetable.getLessonTimePeriod() + ")!",
                        sqlException);
            } else if (sqlException.toString().contains(uniqueTeacherDateTime)) {
                log.error(
                        "ConstraintViolationException: date - {}, lesson time period - {}, teacher id - {} violate the unique primary keys condition - {}",
                        timetable.getDate(), timetable.getLessonTimePeriod(), timetable.getTeacher().getId(),
                        uniqueTeacherDateTime);
                throw new UniqueConstraintViolationException(
                        "The teacher with id: " + timetable.getTeacher().getId() + " is already scheduled ("
                                + timetable.getDate() + ", " + timetable.getLessonTimePeriod() + ")!",
                        sqlException);
            } else if (sqlException.toString().contains(uniqueRoomDateTime)) {
                log.error(
                        "ConstraintViolationException: date - {}, lesson time period - {}, room id - {} violate the unique primary keys condition - {}",
                        timetable.getDate(), timetable.getLessonTimePeriod(), timetable.getRoom().getNumber(),
                        uniqueRoomDateTime);
                throw new UniqueConstraintViolationException(
                        "The room number: " + timetable.getRoom().getNumber() + " is already scheduled ("
                                + timetable.getDate() + ", " + timetable.getLessonTimePeriod() + ")!",
                        sqlException);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteTimetable(int timetableID) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Timetable> timetableToDelete = Optional
                .ofNullable(currentSession.byId(Timetable.class).load(timetableID));
        if (timetableToDelete.isPresent() && timetableToDelete.get().getDate().isAfter(LocalDate.now())) {
            currentSession.delete(timetableToDelete.get());
            log.debug("Delete timetable by id - {}", timetableID);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Timetable>> getTeacherTimetable(Day day, Teacher teacher) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<List<Timetable>> resultList = Optional.ofNullable(currentSession
                .createNamedQuery("Timetable_FindByTeacherAndDate", Timetable.class).setParameter("teacher", teacher)
                .setParameter(dateStart, day.getDateOne()).setParameter(dateEnd, day.getDateTwo()).getResultList());
        log.debug("Take a timetable for teacher - {} to day - {}", teacher, day);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Timetable>> getStudentTimetable(Day day, Student student) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<List<Timetable>> resultList = Optional
                .ofNullable(currentSession.createNamedQuery("Timetable_FindByGroupAndDate", Timetable.class)
                        .setParameter("group", student.getGroup()).setParameter(dateStart, day.getDateOne())
                        .setParameter(dateEnd, day.getDateTwo()).getResultList());
        log.debug("Take a timetable for student - {} to day - {}", student, day);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Timetable>> showTimetable(Day day) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<List<Timetable>> resultList = Optional.ofNullable(currentSession
                .createNamedQuery("Timetable_FindByPeriod", Timetable.class).setParameter(dateStart, day.getDateOne())
                .setParameter(dateEnd, day.getDateTwo()).getResultList());
        log.debug("Take a timetable for day - {}", day);
        return resultList;
    }
}
