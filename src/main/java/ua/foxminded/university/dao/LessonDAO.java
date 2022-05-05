package ua.foxminded.university.dao;

import java.sql.SQLException;

import ua.foxminded.university.service.pojo.Lesson;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface LessonDAO {

    /**
     * The method adds lesson to the timetable.lessons table
     * 
     * @param lesson
     * @return count of added rows otherwise returns -1
     * @throws SQLException
     */
    int addLesson(Lesson lesson) throws SQLException;

    /**
     * Deletes lesson from the timetable.lessons
     * 
     * @param lessonID
     * @return count of deleted rows otherwise zero
     */
    int deleteLesson(int lessonID);
}
