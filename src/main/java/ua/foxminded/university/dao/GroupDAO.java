package ua.foxminded.university.dao;

import java.sql.SQLException;

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
     * @throws SQLException
     */
    int addGroup(Group group) throws SQLException;

    /**
     * The method delete group from the database by group id
     * 
     * @param groupID
     * @return count of deleted rows or zero if nothing was deleted
     */
    int deleteGroup(int groupID);

    /**
     * The method assign lesson to group to the database
     * 
     * @param groupID  existed group id
     * @param lessonID existed lesson id
     * @return count of added rows or -1 if nothing was added
     */
    int assignLessonToGroup(int groupID, int lessonID);

    /**
     * The method delete lesson from group
     * 
     * @param groupID  existed group id
     * @param lessonID existed lesson id
     * @return count of added rows or -1 if nothing was added
     */
    int deleteLessonFromGroup(int groupID, int lessonID);

//    /**
//     * Check is group exists in the database
//     * 
//     * @param groupID
//     * @return true is group exists in the table timetable.groups otherwise false
//     */
//    boolean isGroupExists(int groupID);
//
//    /**
//     * Check is group has lesson
//     * 
//     * @param groupID  groupID existed group id
//     * @param lessonID lessonID existed lesson id
//     * @return true is group has lesson otherwise false
//     */
//    boolean isGroupHasLesson(int groupID, int lessonID);
}
