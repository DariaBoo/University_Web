package ua.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import ua.foxminded.university.service.pojo.*;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface GroupDAO {

    /**
     * The method returns count of added rows or -1 if nothing was added.
     * 
     * @param group
     * @return
     */
    int addGroup(Group group);

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
     * The method finds group by id
     * @param groupID
     * @return optional group
     */
    Optional<Group> findById(int groupID);
    
    /**
     * The method finds groups by lesson id
     * @param lessonID
     * @return optional list of groups
     */
    Optional<List<Group>> findGroupsByLessonId(int lessonID);
    
    /**
     * The method finds all groups by department and returns optional list of groups
     * @return optional list of groups
     */
    Optional<List<Group>> findGroupsByDepartment(int departmentID);
    
    /**
     * The method finds all groups by teacher id and returns optional list of groups
     * @return optional list of groups
     */
    Optional<List<Group>> findGroupsByTeacherId(int teacherID);
    
    
    
    /**
     * The method updated existed group and returns count of updated rows
     * @param group
     * @return count of updated rows or 0 if nothing was updated
     */
    int updateGroup(Group group);
    
    /**
     * Returns a size of column 'group_name' from the timetable.groups
     * @return column's size
     */
    int getGroupNameMaxSize();
}
