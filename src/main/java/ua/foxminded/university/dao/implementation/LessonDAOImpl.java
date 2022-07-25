package ua.foxminded.university.dao.implementation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityGraph;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Teacher;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Slf4j
@Repository
public class LessonDAOImpl implements LessonDAO {

    private static final String debugMessage = "Get current session - {}";
    private int result = 0;

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public int addLesson(Lesson lesson) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        try {
            result = (int) currentSession.save(lesson);
            log.debug("Add a Lesson to the timetable.lessons and return id  - {}", result);
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            log.error(
                    "ConstraintViolationException while adding lesson - {} (name - {} violates the unique primary keys condition",
                    lesson, lesson.getName());
            throw new DAOException("Lesson with name " + lesson.getName() + " already exists!");
        }
        return result;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteLesson(int lessonID) {
        boolean isDeleted = false;
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Lesson> lessonToDelete = Optional.ofNullable(currentSession.byId(Lesson.class).load(lessonID));
        if (lessonToDelete.isPresent()) {
            currentSession.delete(lessonToDelete.get());
            isDeleted = true;
            log.debug("Delete lesson from the database, is deleted: {}", isDeleted);
        }
        return isDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLesson(Lesson lesson) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        try {
            currentSession.update(lesson);
            log.debug("Took a result {}, if the result equals 1 lesson was updated, if 0 - not updated", result);
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            log.error(
                    "ConstraintViolationException while updating student - {} (name - {} violates the unique primary keys condition",
                    lesson, lesson.getName());
            throw new DAOException("Lesson with name " + lesson.getName() + " already exists!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Lesson> findByID(int lessonID) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Lesson> resultLesson = Optional.ofNullable(currentSession.get(Lesson.class, lessonID));
        log.debug("Find lesson by id {} and return optional lesson {}", lessonID, resultLesson);
        return resultLesson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Lesson>> findAllLessons() {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<List<Lesson>> resultList = Optional
                .ofNullable(currentSession.createQuery("from Lesson", Lesson.class).getResultList());
        log.debug("Find all lessons from the timetable.lessons and return optional list of lessons {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Set<Lesson>> findLessonsByTeacherId(int teacherID) {
        Optional<Set<Lesson>> resultSet = Optional.empty();
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        EntityGraph<?> entityGraph = currentSession.createEntityGraph("graph.teacherLessons");
        Optional<Teacher> teacher = Optional.ofNullable(currentSession
                .createQuery("SELECT t FROM Teacher t WHERE t.id = :id", Teacher.class).setParameter("id", teacherID)
                .setHint("javax.persistence.fetchgraph", entityGraph).getSingleResult());
        if (teacher.isPresent()) {
            resultSet = Optional.ofNullable(teacher.get().getLessons());
            log.debug("Get all lessons - {} from teacher - {}", resultSet, teacher.get());
        }
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Set<Lesson>> findLessonsByGroupId(int groupID) {
        Optional<Set<Lesson>> resultSet = Optional.empty();
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        EntityGraph<?> entityGraph = currentSession.createEntityGraph("graph.groupLessons");
        Optional<Group> group = Optional.ofNullable(currentSession
                .createQuery("SELECT g FROM Group g WHERE g.id = :id", Group.class).setParameter("id", groupID)
                .setHint("javax.persistence.fetchgraph", entityGraph).getSingleResult());
        if (group.isPresent()) {
            resultSet = Optional.ofNullable(group.get().getLessons());
            log.debug("Get lessons - {} from group - {}", resultSet, group.get());
        }
        return resultSet;
    }
}
