package ua.foxminded.university.service.implementation;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.exception.ServiceException;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDAO groupDAO;

    private static final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class.getName());
    int result = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int addGroup(Group group) {
        try {
            result = groupDAO.addGroup(group);
            log.debug("Add a new group - {} and take an id - {}", group, result);
        } catch (DAOException e) {
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
    public boolean deleteGroup(int groupID) {
        return groupDAO.deleteGroup(groupID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void assignLessonToGroup(int groupID, int lessonID) {
        groupDAO.assignLessonToGroup(groupID, lessonID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteLessonFromGroup(int groupID, int lessonID) {
        groupDAO.deleteLessonFromGroup(groupID, lessonID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Group> findAllGroups() {
        List<Group> resultList = groupDAO.findAllGroups()
                .orElseThrow(() -> new IllegalArgumentException("Error occured while searching all groups"));
        log.debug("Find all groups and return a list of groups - {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Group findById(int groupID) {
        Group resultGroup = groupDAO.findById(groupID).orElseThrow(
                () -> new IllegalArgumentException("Error occured while searching group by id: " + groupID));
        log.debug("Find group by id - {} and return group - {}", groupID, resultGroup);
        return resultGroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Set<Group> findGroupsByLessonId(int lessonID) {
        Set<Group> resultSet = groupDAO.findGroupsByLessonId(lessonID).orElseThrow(
                () -> new IllegalArgumentException("Error occured while searching group by lesson id " + lessonID));
        log.debug("Find groups by lesson id - {} and return a list of groups - {}", lessonID, resultSet);
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Group> findGroupsByTeacherId(int teacherID) {
        List<Group> resultList = groupDAO.findGroupsByTeacherId(teacherID)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Error occured while searching group by teacher id - {} " + teacherID));
        log.debug("Find groups by teacher id - {} and return a list of groups - {}", teacherID, resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateGroup(Group group) {
        try {
            groupDAO.updateGroup(group);
            log.debug("Update the group - {}", group);
        } catch (DAOException e) {
            log.error(e.getMessage(), e.getCause());
            throw new ServiceException(e.getMessage());
        }
    }
}
