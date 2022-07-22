package ua.foxminded.university.service;

import java.util.List;
import java.util.Set;

import ua.foxminded.university.service.entities.Lesson;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface LessonService {
    /**
     * The method adds lesson to the timetable.lessons table
     * 
     * @param lesson
     * @return Lesson id
     */
    int addLesson(Lesson lesson);

    /**
     * The method lets update lesson's name and description and returns count of updated rows otherwise zero
     * @param lesson
     * @return count of updated rows otherwise zero
     */
    void updateLesson(Lesson lesson) ;
    
    /**
     * Deletes lesson from the timetable.lessons
     * 
     * @param lessonID
     * @return true or false
     */
    boolean deleteLesson(int lessonID);
    
    /**
     * The method finds a lesson by id and returns optional lesson
     * @param lessonID
     * @return optional lesson
     */
    Lesson findByID(int lessonID);

    /**
     * The method finds all lessons and returns optional list of lessons
     * @return optional list of lessons
     */
    List<Lesson> findAllLessons();
    
    /**
     * The method finds teachers lessons by teacher id
     * @return list of lessons
     */
    Set<Lesson> findLessonsByTeacherId(int teacherID);
    
    /**
     * The method finds groups lessons by group id
     * @return list of lessons
     */
    Set<Lesson> findLessonsByGroupId(int groupID);
}
