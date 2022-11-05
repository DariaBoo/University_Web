package ua.foxminded.university.service;

import java.util.List;

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
     * @return added group
     */
    Group addGroup(Group group);

    /**
     * The method updated existed group
     * 
     * @param group
     * @return
     */
    Group updateGroup(Group group);

    /**
     * The method deletes group from the database by group id
     * 
     * @param groupId
     */
    void deleteGroup(int groupId);

    /**
     * The method assigns lesson to group to the database
     * 
     * @param groupId  existed group id
     * @param lessonId existed lesson id
     * @return true or false
     */
    boolean assignLessonToGroup(int groupId, int lessonId);

    /**
     * The method deletes lesson from group
     * 
     * @param groupId  existed group id
     * @param lessonId existed lesson id
     * @return true or false
     */
    boolean deleteLessonFromGroup(int groupId, int lessonId);

    /**
     * The method finds all groups and returns list of groups
     * 
     * @return list of groups
     */
    List<Group> findAllGroups();

    /**
     * The method finds group by id
     * 
     * @param groupId
     * @return group
     */
    Group findById(int groupId);

    /**
     * The method finds all groups by teacher id and returns list of groups
     * 
     * @return list of groups
     */
    List<Group> findGroupsByTeacherId(int teacherId);
}
