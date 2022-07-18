package ua.foxminded.university.service;

import java.util.List;
import java.util.Set;

import ua.foxminded.university.service.entities.Group;

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
     */
    int addGroup(Group group);

    /**
     * The method deletes group from the database by group id
     * 
     * @param groupID
     * @return count of deleted rows or zero if nothing was deleted
     */
    boolean deleteGroup(int groupID);

    /**
     * The method assigns lesson to group to the database
     * 
     * @param groupID  existed group id
     * @param lessonID existed lesson id
     * @return count of added rows or -1 if nothing was added
     */
    void assignLessonToGroup(int groupID, int lessonID);

    /**
     * The method deletes lesson from group
     * 
     * @param groupID  existed group id
     * @param lessonID existed lesson id
     * @return count of added rows or -1 if nothing was added
     */
    void deleteLessonFromGroup(int groupID, int lessonID);

    /**
     * The method finds all groups and returns optional list of groups
     * @return optional list of groups
     */
    List<Group> findAllGroups();
    
    /**
     * The method finds group by id
     * @param groupID
     * @return group
     */
    Group findById(int groupID);
 
    /**
     * The method finds groups by lesson id
     * @param lessonID
     * @return list of groups
     */
    Set<Group> findGroupsByLessonId(int lessonID);
    
    /**
     * The method finds all groups by teacher id and returns list of groups
     * @return list of groups
     */
    List<Group> findGroupsByTeacherId(int teacherID);
       
    /**
     * The method updated existed group and returns count of updated rows
     * @param group
     * @return count of updated rows or 0 if nothing was updated
     */
    void updateGroup(Group group);
}
