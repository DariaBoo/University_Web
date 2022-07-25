package ua.foxminded.university.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import ua.foxminded.university.service.entities.Lesson;

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
     */
    int addLesson(Lesson lesson);

    /**
     * Deletes lesson from the timetable.lessons
     * 
     * @param lessonID
     * @return true is deleted otherwise false
     */
    boolean deleteLesson(int lessonID);
    
    /**
     * The method finds a lesson by id and returns optional lesson
     * @param lessonID
     * @return optional lesson
     */
    Optional<Lesson> findByID(int lessonID);

    /**
     * The method finds all lessons and returns optional list of lessons
     * @return optional list of lessons
     */
    Optional<List<Lesson>> findAllLessons();
    
    /**
     * The method finds teachers lessons by teacher id
     * @return list of lessons
     */
    Optional<Set<Lesson>> findLessonsByTeacherId(int teacherID);
    
    /**
     * The method finds groups lessons by group id
     * @return list of lessons
     */
    Optional<Set<Lesson>> findLessonsByGroupId(int groupID);
    
    /**
     * The method lets update lesson's name and description and returns count of updated rows otherwise zero
     * @param lesson
     * @return count of updated rows otherwise zero
     */
    void updateLesson(Lesson lesson);
}
