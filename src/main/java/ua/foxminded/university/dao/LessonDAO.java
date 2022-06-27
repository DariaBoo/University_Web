package ua.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

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
     */
    int addLesson(Lesson lesson);

    /**
     * Deletes lesson from the timetable.lessons
     * 
     * @param lessonID
     * @return count of deleted rows otherwise zero
     */
    int deleteLesson(int lessonID);
    
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
    List<Lesson> findAllLessons();
    
    /**
     * The method finds teachers lessons by teacher id
     * @return list of lessons
     */
    List<Lesson> findLessonsByTeacherId(int teacherID);
    
    /**
     * The method finds groups lessons by group id
     * @return list of lessons
     */
    List<Lesson> findLessonsByGroupId(int groupID);
    
    /**
     * The method lets update lesson's name and description and returns count of updated rows otherwise zero
     * @param lesson
     * @return count of updated rows otherwise zero
     */
    int updateLesson(Lesson lesson);
    
    /**
     * Returns a size of column 'lesson_name' from the timetable.lessons
     * @return column's size
     */
    int getLessonNameMaxSize();
    
    /**
     * Returns a size of column 'description' from the timetable.lessons
     * @return column's size
     */
    int getDescriptionMaxSize();
}
