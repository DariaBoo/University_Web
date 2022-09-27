package ua.foxminded.university.service;

import java.time.LocalDate;
import java.util.List;

import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Teacher;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface TeacherService {
    /**
     * The method adds Teacher to the timetable.teachers
     * 
     * @param teacher
     * @return true or false
     */
    boolean addTeacher(Teacher teacher);

    /**
     * The method lets update teacher's first name and last name and returns count
     * of updated rows otherwise zero
     * 
     * @param teacher
     * @return count of updated rows otherwise zero
     */
    void updateTeacher(Teacher teacher);
    
    /**
     * Deletes teacher from the timetable.teachers
     * 
     * @param teacherId
     * @return true or false
     */
    boolean deleteTeacher(int teacherId);

    /**
     * Assigns lesson to teacher to the timetable.lessons_teachers
     * 
     * @param lessonId  existed lesson id
     * @param teacherId existed teacher id
     * @return true or false
     */
    boolean assignLessonToTeacher(int lessonId, int teacherId);

    /**
     * Deletes lesson from teacher from the timetable.lessons_teachers
     * 
     * @param lessonId  existed lesson id
     * @param teacherId existed teacher id
     * @return true or false
     */
    boolean deleteLessonFromTeacher(int lessonId, int teacherId);

    /**
     * The method set date start and date end of teacher's absent
     * 
     * @param teacherId existed teacher id
     * @param day
     * @return count of updated rows otherwise -1
     */
    boolean setTeacherAbsent(int teacherId, Day day);

    /**
     * Deletes teacher absent from the timetable.teacherabsent
     * 
     * @param teacherId existed teacher id
     * @param day
     * @return count of deleted rows otherwise zero
     */
    boolean deleteTeacherAbsent(int teacherId, Day day);

    /**
     * The method finds a teacher by id from the timetable.teachers
     * 
     * @param teacherId
     * @return teacher
     */
    Teacher findById(int teacherId);

    /**
     * The method finds all teachers and returns optional list of teachers
     * 
     * @return list of teachers
     */
    List<Teacher> findAllTeachers();

    /**
     * The method changes password by teacher id
     * 
     * @param teacherId
     * @param newPassword
     * @return count of updated rows otherwise 0
     */
    void changePassword(int teacherId, String newPassword);
    
    /**
     * The method checks is teacher absent 
     * @param date
     * @param teacher
     * @return true or false
     */
    boolean checkIsAbsent(LocalDate date, int teacherId);
    
    Teacher findByUsername(String username);
}
