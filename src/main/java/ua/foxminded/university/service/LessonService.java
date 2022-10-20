package ua.foxminded.university.service;

import java.util.List;

import ua.foxminded.university.service.entities.Lesson;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface LessonService {
    
    /**
     * The method adds lesson to the database
     * 
     * @param lesson
     * @return true or false
     */
    boolean addLesson(Lesson lesson);

    /**
     * The method lets update lesson's name and description
     * @param lesson
     */
    void updateLesson(Lesson lesson) ;
    
    /**
     * Deletes lesson from the database
     * 
     * @param lessonId
     * @return true or false
     */
    boolean deleteLesson(int lessonId);
    
    /**
     * The method finds a lesson by id and returns lesson
     * @param lessonId
     * @return lesson
     */
    Lesson findById(int lessonId);

    /**
     * The method finds all lessons and returns optional list of lessons
     * @return  list of lessons
     */
    List<Lesson> findAllLessons();
}
