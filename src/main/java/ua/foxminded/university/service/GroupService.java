package ua.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import ua.foxminded.university.service.exception.ServiceException;
import ua.foxminded.university.service.pojo.Group;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
public interface GroupService {
    /**
     * The method returns count of added rows or -1 if nothing was added.
     * 
     * @param group
     * @return 1 or 0
     * @throws ServiceException 
     */
    int addGroup(Group group) throws ServiceException;

    /**
     * The method deletes group from the database by group id
     * 
     * @param groupID
     * @return count of deleted rows or zero if nothing was deleted
     */
    int deleteGroup(int groupID);

    /**
     * The method assigns lesson to group to the database
     * 
     * @param groupID  existed group id
     * @param lessonID existed lesson id
     * @return count of added rows or -1 if nothing was added
     */
    int assignLessonToGroup(int groupID, int lessonID);

    /**
     * The method deletes lesson from group
     * 
     * @param groupID  existed group id
     * @param lessonID existed lesson id
     * @return count of added rows or -1 if nothing was added
     */
    int deleteLessonFromGroup(int groupID, int lessonID);

    /**
     * The method finds all groups and returns optional list of groups
     * @return optional list of groups
     */
    Optional<List<Group>> findAllGroups();
    
    /**
     * The method finds all groups by department and returns optional list of groups
     * @return optional list of groups
     */
    Optional<List<Group>> findGroupsByDepartment(int departmentID);
    
    /**
     * The method updated existed group and returns count of updated rows
     * @param group
     * @return count of updated rows or 0 if nothing was updated
     * @throws ServiceException 
     */
    int updateGroup(Group group) throws ServiceException;
}
