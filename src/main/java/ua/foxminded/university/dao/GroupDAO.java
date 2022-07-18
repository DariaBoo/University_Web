package ua.foxminded.university.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.entities.*;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface GroupDAO {

    /**
     * The method returns group id.
     * 
     * @param group
     * @return
     * @throws DAOException 
     */
    int addGroup(Group group) throws DAOException;

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
     * @return true is assigned otherwise false
     */
    boolean assignLessonToGroup(int groupID, int lessonID);

    /**
     * The method deletes lesson from group
     * 
     * @param groupID  existed group id
     * @param lessonID existed lesson id
     * @return true is deleted otherwise false
     */
    boolean deleteLessonFromGroup(int groupID, int lessonID);

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
    Optional<Set<Group>> findGroupsByLessonId(int lessonID);
    
    /**
     * The method finds all groups by teacher id and returns optional list of groups
     * @return optional list of groups
     */
    Optional<List<Group>> findGroupsByTeacherId(int teacherID);    
    
    /**
     * The method updated existed group and returns count of updated rows
     * @param group
     * @throws DAOException 
     */
    void updateGroup(Group group) throws DAOException;

    /**
     * The method return count of students by group id
     * @param groupID
     * @return count of students
     */
    int getCountOfStudents(int groupID);
}
