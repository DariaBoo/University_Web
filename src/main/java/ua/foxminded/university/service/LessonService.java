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
     * @return saved lesson
     */
    Lesson addLesson(Lesson lesson);

    /**
     * The method lets update lesson's name and description
     * 
     * @param lesson
     * @return updated lesson
     */
    Lesson updateLesson(Lesson lesson);

    /**
     * Deletes lesson from the database
     * 
     * @param lessonId
     */
    void deleteLesson(int lessonId);

    /**
     * The method finds a lesson by id and returns lesson
     * 
     * @param lessonId
     * @return lesson
     */
    Lesson findById(int lessonId);

    /**
     * The method finds all lessons and returns optional list of lessons
     * 
     * @return list of lessons
     */
    List<Lesson> findAllLessons();
}
