package ua.foxminded.university.dao.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityGraph;
import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Teacher;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Repository
public class GroupDAOImpl implements GroupDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private static final Logger log = LoggerFactory.getLogger(GroupDAOImpl.class.getName());
    private static final String debugMessage = "Get current session - {}";
    private int result = 0;

    /**
     * {@inheritDoc}
     * 
     * @throws DAOException
     */
    @Override
    public int addGroup(Group group) throws DAOException {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        try {
            result = (int) currentSession.save(group);
            log.debug("Save a group - {} to the database", group);
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            log.error(
                    "ConstraintViolationException while adding new group - {} (name - {} violates the unique primary keys condition",
                    group, group.getName());
            throw new DAOException("Group with name " + group.getName() + " already exists!", e);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws DAOException
     */
    @Override
    public void updateGroup(Group group) throws DAOException {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        try {
            currentSession.update(group);
            log.debug("Update the group in the database");
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            log.error(
                    "ConstraintViolationException while updating group - {} (name - {} violates the unique primary keys condition",
                    group, group.getName());
            throw new DAOException("Group with name " + group.getName() + " already exists!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteGroup(int groupID) {
        boolean isDeleted = false;
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Group> groupToDelete = Optional.ofNullable(currentSession.byId(Group.class).load(groupID));
        log.debug("Take a group from th database by group id - {}", groupID);
        if(groupToDelete.isPresent()) {
        currentSession.delete(groupToDelete.get());
        isDeleted = true;
        log.debug("Delete the group - {} from the database", groupToDelete.get());
        }
        return isDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean assignLessonToGroup(int groupID, int lessonID) {
        boolean isAssigned = false;
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Group> group = getGroupWithLessons(groupID);
        Optional<Lesson> lesson = getLessonWithGroups(lessonID);
        if (group.isPresent() && lesson.isPresent()) {
            group.get().getLessons().add(lesson.get());
            lesson.get().getGroups().add(group.get());
            currentSession.update(group.get());
            currentSession.update(lesson.get());
            isAssigned = true;
            log.debug("Assign lesson - {} to group - {}", lessonID, groupID);
        }
        return isAssigned;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteLessonFromGroup(int groupID, int lessonID) {
        boolean isDeleted = false;
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Group> group = Optional.ofNullable(currentSession.load(Group.class, groupID));
        log.debug("Take a group - {} from the database by group id - {}", group, groupID);
        Optional<Lesson> lesson = Optional.ofNullable(currentSession.load(Lesson.class, lessonID));
        log.debug("Take a leeson - {} from the database by lesson id - {}", lesson, lessonID);
        if (group.isPresent() && lesson.isPresent()) {
            group.get().getLessons().remove(lesson.get());
            lesson.get().getGroups().remove(group.get());
            currentSession.update(group.get());
            currentSession.update(lesson.get());
            isDeleted = true;
            log.debug("Delete lesson from group from the database");
        }
        return isDeleted;
    }
    
    private Optional<Group> getGroupWithLessons(int groupID) {
        Optional<Group> group = Optional.empty();
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        EntityGraph<?> entityGraph = currentSession.createEntityGraph("graph.groupStudents");
        if(findById(groupID).isPresent()) {
        group = Optional.ofNullable(currentSession
                .createQuery("SELECT g FROM Group g WHERE g.id = :id", Group.class).setParameter("id", groupID)
                .setHint("javax.persistence.fetchgraph", entityGraph).getSingleResult());
        log.debug("Take an optional group - {} from the database by group id - {}", group, groupID);
        } 
        return group;
    }

    private Optional<Lesson> getLessonWithGroups(int lessonID) {
        Optional<Lesson> lesson;
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        EntityGraph<?> entityGraph = currentSession.createEntityGraph("graph.lesson-groups-teachers");
        try {
        lesson = Optional.of(currentSession
                .createQuery("SELECT l FROM Lesson l WHERE l.id = :id", Lesson.class).setParameter("id", lessonID)
                .setHint("javax.persistence.fetchgraph", entityGraph).getSingleResult());
        log.debug("Take a lesson - {} from the database by lesson id - {}", lesson, lessonID);
        } catch (NoResultException e) {
            log.error("Lesson id - {} is incorrect", lessonID);
            lesson = Optional.empty();
        }
        return lesson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Group>> findAllGroups() {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<List<Group>> resultList = Optional
                .ofNullable(currentSession.createQuery("from Group", Group.class).getResultList());
        log.debug("Take optional list of groups {} from the database", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Set<Group>> findGroupsByLessonId(int lessonID) {
        Optional<Set<Group>> resultSet = Optional.empty();
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Lesson> lesson = this.getLessonWithGroups(lessonID);
        if (lesson.isPresent()) {
            resultSet = Optional.ofNullable(lesson.get().getGroups());
            log.debug("Take an optional list of groups {} assign to the lesson with id - {} from the database",
                    resultSet, lessonID);
        }
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Group>> findGroupsByTeacherId(int teacherID) {
        List<Group> groups = new ArrayList<>();
        Optional<List<Group>> resultList = Optional.empty();

        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Teacher> teacher = Optional.ofNullable(currentSession.get(Teacher.class, teacherID));
        log.debug("Take a teacher - {} from the database by teacher id - {}", teacher, teacherID);
        if (teacher.isPresent()) {
            for (Lesson lesson : teacher.get().getLessons()) {
                groups.addAll(lesson.getGroups());
            }
            resultList = Optional.ofNullable(groups.stream().distinct().collect(Collectors.toList()));
            log.debug("Take an optional list of groups - {} by teacher id - {}", resultList, teacherID);
        }
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Group> findById(int groupID) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Group> resultGroup = Optional.ofNullable(currentSession.get(Group.class, groupID));
        log.debug("Take an optional group {} from the database by group id - {}", resultGroup, groupID);
        return resultGroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCountOfStudents(int groupID) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);        
        Optional<Group> group = getGroupWithLessons(groupID);
        if (group.isPresent()) {
            result = group.get().getStudents().size();
        }
        return result;
    }
}
