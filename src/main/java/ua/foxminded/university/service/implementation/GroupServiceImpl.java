package ua.foxminded.university.service.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.university.dao.implementation.GroupDAOImpl;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.exception.ServiceException;
import ua.foxminded.university.service.pojo.Group;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Service
public class GroupServiceImpl implements GroupService {

    private static final String groupNamePattern = "(\\S+)-(\\d+)";
    private final GroupDAOImpl groupDAOImpl;
    private static final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class.getName());

    /**
     * Returns instance of the class
     * 
     * @param groupDAOImpl
     */
    @Autowired
    public GroupServiceImpl(GroupDAOImpl groupDAOImpl) {
        this.groupDAOImpl = groupDAOImpl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addGroup(Group group) {
        log.trace("Add new group");
        log.trace("Check if group name - {} is not out of bound", group.getName());
        if (group.getName().length() > groupDAOImpl.getGroupNameMaxSize()) {
            log.error("Group name - {} is out of bound", group.getName());
            throw new StringIndexOutOfBoundsException("Group name is out of bound");
        }
        log.trace("Check if group name - {} matches the pattern {}", group.getName(), groupNamePattern);
        if (!group.getName().matches(groupNamePattern)) {
            log.error("Group name - {} is not matches the pattern {}", group.getName(), groupNamePattern);
            throw new ServiceException("Group name should contain two letters, dash and two digits");
        }
        int result = groupDAOImpl.addGroup(group);
        log.debug("Add new group - {} and take a result - {}", group, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteGroup(int groupID) {
        int result = groupDAOImpl.deleteGroup(groupID);
        log.debug("Delete group with id - {}, and take a result - {}", groupID, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int assignLessonToGroup(int groupID, int lessonID) {
        int result = groupDAOImpl.assignLessonToGroup(groupID, lessonID);
        log.debug("Assign lesson with id - {} to group with id - {}) and take a result - {}", lessonID, groupID, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLessonFromGroup(int groupID, int lessonID) {
        int result = groupDAOImpl.deleteLessonFromGroup(groupID, lessonID);
        log.debug("Delete lesson with id - {} from group with id - {} and take a result - {}", lessonID, groupID, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> findAllGroups() {
        List<Group> resultList = groupDAOImpl.findAllGroups().orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Find all groups and return a list of groups - {}, otherwise return IllegalArgumentException", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group findById(int groupID) {
        Group resultGroup = groupDAOImpl.findById(groupID).orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Find group by id - {} and return group - {}, otherwise return IllegalArgumentException", groupID, resultGroup);
        return resultGroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> findGroupsByLessonId(int lessonID) {
        List<Group> resultList = groupDAOImpl.findGroupsByLessonId(lessonID)
                .orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Find groups by lesson id - {} and return a list of groups - {}, otherwise return IllegalArgumentException", lessonID, resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> findGroupsByDepartment(int departmentID) {
        List<Group> resultList = groupDAOImpl.findGroupsByDepartment(departmentID)
                .orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Find groups by department with id - {} and return a list of groups - {}, otherwise return IllegalArgumentException", departmentID, resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> findGroupsByTeacherId(int teacherID) {
        List<Group> resultList = groupDAOImpl.findGroupsByTeacherId(teacherID)
                .orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Find groups by teacher id - {} and return a list of groups - {}, otherwise return IllegalArgumentException", teacherID, resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateGroup(Group group) {
        int result = groupDAOImpl.updateGroup(group);
        log.debug("Update existed group - {}", group);
        return result;
    }
}
