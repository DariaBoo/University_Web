package ua.foxminded.university.service.implementation;

import java.util.List;
import java.util.Optional;

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
public class GroupServiceImpl implements GroupService{
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
        if(group.getName().length() > groupDAOImpl.getGroupNameMaxSize()) {
            log.error("Group name - {} is out of bound", group.getName());
            throw new StringIndexOutOfBoundsException("Group name is out of bound");
        }
        log.trace("Check if group name - {} matches the pattern {}", group.getName(), groupNamePattern);
        if(!group.getName().matches(groupNamePattern)) {
            log.error("Group name - {} is not matches the pattern {}", group.getName(), groupNamePattern);
            throw new ServiceException("Group name should contain two letters, dash and two digits");
        }        
        return groupDAOImpl.addGroup(group);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteGroup(int groupID) {
        log.trace("Delete group by id {}", groupID);
        return groupDAOImpl.deleteGroup(groupID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int assignLessonToGroup(int groupID, int lessonID) {
        log.trace("Assign lesson with id {} to group with id {}", lessonID, groupID);
        return groupDAOImpl.assignLessonToGroup(groupID, lessonID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLessonFromGroup(int groupID, int lessonID) {
        log.trace("Delete lesson with id {} from group with id {}", lessonID, groupID);
        return groupDAOImpl.deleteLessonFromGroup(groupID, lessonID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Group>> findAllGroups() {
        log.trace("Find all groups");
        return groupDAOImpl.findAllGroups();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Group>> findGroupsByDepartment(int departmentID) {
        log.trace("Find groups by department with id {}", departmentID);
        return groupDAOImpl.findGroupsByDepartment(departmentID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateGroup(Group group) {
        log.trace("Update existed group");
        log.trace("Check if group name - {} is not out of bound", group.getName());
        if(group.getName().length() > groupDAOImpl.getGroupNameMaxSize()) {
            log.error("Group name - {} is out of bound", group.getName());
            throw new StringIndexOutOfBoundsException("Group name is out of bound");
        }
        log.trace("Check if group name - {} matches the pattern {}", group.getName(), groupNamePattern);
        if(!group.getName().matches(groupNamePattern)) {
            log.error("Group name - {} is not matches the pattern {}", group.getName(), groupNamePattern);
            throw new ServiceException("Group name should contain two letters, dash and two digits");
        }    
        return groupDAOImpl.updateGroup(group);
    }
}
