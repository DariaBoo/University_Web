package ua.foxminded.university.dao.implementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import javax.persistence.EntityGraph;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.TeacherDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Teacher;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Repository
public class TeacherDAOImpl implements TeacherDAO {

    private static final Logger log = LoggerFactory.getLogger(TeacherDAOImpl.class.getName());
    private static final String debugMessage = "Get current session - {}";
    private int result;

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public int addTeacher(Teacher teacher) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        try {
            result = (int) currentSession.save(teacher);
            log.debug("Add a new teacher to the timetable.teachers and returns id - {}", result);
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            log.error(
                    "ConstraintViolationException while adding student - {} (name - {}, surname - {}  violate the unique primary keys condition",
                    teacher, teacher.getFirstName(), teacher.getLastName());
            throw new DAOException("Teacher with name " + teacher.getFirstName() + ", surname " + teacher.getLastName()
                    + " already exists!");
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTeacher(Teacher teacher) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        try {
            currentSession.update(teacher);
            log.debug("Update the teacher");
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            log.error(
                    "ConstraintViolationException while updating teacher - {} (name - {}, surname - {} violate the unique primary keys condition",
                    teacher, teacher.getFirstName(), teacher.getLastName());
            throw new DAOException("Teacher with name " + teacher.getFirstName() + ", surname " + teacher.getLastName()
                    + " already exists!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteTeacher(int teacherID) {
        boolean isDeleted = false;
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Teacher> teacherToDelete = Optional.ofNullable(currentSession.byId(Teacher.class).load(teacherID));
        if(teacherToDelete.isPresent()) {
        currentSession.delete(teacherToDelete.get());
        isDeleted = true;
        log.debug("Delete teacher from the database by teacher id - {}, is deleted: {}", teacherID, isDeleted);
        }
        return isDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean assignLessonToTeacher(int lessonID, int teacherID) {
        boolean isAssigned = false;
        log.trace("Assign lesson to teacher to the timetable.lessons_teachers");
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Lesson> lesson = Optional.ofNullable(currentSession.load(Lesson.class, lessonID));
        Optional<Teacher> teacher = Optional.ofNullable(currentSession.load(Teacher.class, teacherID));
        if (lesson.isPresent() && teacher.isPresent()) {
            lesson.get().getTeachers().add(teacher.get());
            teacher.get().getLessons().add(lesson.get());
            currentSession.update(teacher.get());
            currentSession.update(lesson.get());
            isAssigned = true;
            log.debug("Assign lesson with id - {} to teacher with id - {} to the timetable, is assigned: {}", lessonID, teacherID, isAssigned);
        }       
        return isAssigned;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteLessonFromTeacher(int lessonID, int teacherID) {
        boolean isDeleted = false;
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Lesson> lesson = Optional.ofNullable(currentSession.load(Lesson.class, lessonID));
        Optional<Teacher> teacher = Optional.ofNullable(currentSession.load(Teacher.class, teacherID));
        if (lesson.isPresent() && teacher.isPresent()) {
            lesson.get().getTeachers().remove(teacher.get());
            teacher.get().getLessons().remove(lesson.get());
            currentSession.update(teacher.get());
            currentSession.update(lesson.get());
            isDeleted = true;
            log.debug("Delete lesson with id - {} from teacher with id -{}, is deleted: {}", lessonID, teacherID,isDeleted);
        }       
        return isDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int setTeahcerAbsent(int teacherID, Day day) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);        
        day.setTeacher(new Teacher.TeacherBuidler().setID(teacherID).build());
        result = (int) currentSession.save(day);
        log.debug("Set dates when teacher is absent and returns count of added rows - {} otherwise returns zero", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteTeahcerAbsent(int teacherID, Day day) {
        boolean isDeleted = false;
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        day.setTeacher(new Teacher.TeacherBuidler().setID(teacherID).build());
        if(findByID(teacherID).isPresent()) {
        currentSession.delete(day);
        isDeleted = true;
        log.debug("Delete teacher absent from the timetable.teacherabsent, is deleted: {}", isDeleted);
        }
        return isDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Day>> showTeacherAbsent(int teacherID) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
        CriteriaQuery<Day> criteriaQuery = criteriaBuilder.createQuery(Day.class);
        Root<Day> root = criteriaQuery.from(Day.class);
        Teacher teacher = new Teacher();
        teacher.setId(teacherID);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("teacher"), teacher));
        Query query = currentSession.createQuery(criteriaQuery);
        Optional<List<Day>> resultList = Optional.ofNullable(query.getResultList());
        log.debug("Returns optional list of teachers {}", resultList);
        return resultList;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkIsAbsent(Teacher teacher, LocalDate checkedDate) {
        boolean isAbsent = false;
        Predicate<Day> isBefore = day ->  day.getDateOne().minusDays(1).isBefore(checkedDate);
        Predicate<Day> isAfter = day ->  day.getDateTwo().plusDays(1).isAfter(checkedDate);
        Optional<List<Day>> absentDays = showTeacherAbsent(teacher.getId());
        log.debug("Take optional list of absent days - {} by teacher id: {}", absentDays, teacher.getId());
        if(absentDays.isPresent()) {
            isAbsent = absentDays.get().stream().anyMatch(isBefore.and(isAfter));
            log.info("The teacher with id: {} is absent", teacher.getId());
        }            
        return isAbsent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Teacher> findByID(int teacherID) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Teacher> resultTeacher = Optional.ofNullable(currentSession.get(Teacher.class, teacherID));
        log.debug("Return optional teacher {}", resultTeacher);
        return resultTeacher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Teacher>> findAllTeachers() {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<List<Teacher>> resultList = Optional
                .ofNullable(currentSession.createQuery("from Teacher", Teacher.class).getResultList());
        log.debug("Returns optional list of teachers {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Set<Teacher>> findTeachersByLessonId(int lessonID) {
        Optional<Set<Teacher>> resultSet = Optional.empty();
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        EntityGraph<?> entityGraph = currentSession.createEntityGraph("graph.lesson-groups-teachers");
        Optional<Lesson> lesson = Optional.ofNullable(currentSession.createQuery("SELECT l FROM Lesson l WHERE l.id = :id", Lesson.class)
                .setParameter("id", lessonID).setHint("javax.persistence.fetchgraph", entityGraph).getSingleResult());
        if(lesson.isPresent()) {
        resultSet = Optional.ofNullable(lesson.get().getTeachers());
        log.debug("Take a list of teachers - {} from lesson", resultSet.get());
        }
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int changePassword(int teacherID, String newPassword) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        result = currentSession.createNamedQuery("Teacher_changePassword")
                .setParameter("newPassword", newPassword).setParameter("id", teacherID).executeUpdate();
        log.debug("Change password in the timetable.teachers");
        return result;
    }
}
