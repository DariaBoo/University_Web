package ua.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Teacher;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public interface TeacherDAO {
    /**
     * The method adds new teacher to the timetable.teachers
     * 
     * @param teacher
     * @return count of added rows otherwise -1
     */
    int addTeacher(Teacher teacher);

    /**
     * Deletes teacher from the timetable.teachers
     * 
     * @param teacherID
     * @return true is deleted otherwise false
     */
    boolean deleteTeacher(int teacherID);

    /**
     * Assigns lesson to teacher to the timetable.lessons_teachers
     * 
     * @param lessonID  existed lesson id
     * @param teacherID existed teacher id
     * @return true is assigned otherwise false
     */
    boolean assignLessonToTeacher(int lessonID, int teacherID);

    /**
     * Deletes lesson from teacher from the timetable.lessons_teachers
     * 
     * @param lessonID  existed lesson id
     * @param teacherID existed teacher id
     * @return true is deleted otherwise false
     */
    boolean deleteLessonFromTeacher(int lessonID, int teacherID);

    /**
     * The method set date start and date end of teacher's absent
     * 
     * @param teacherID existed teacher id
     * @param day
     * @return count of updated rows otherwise -1
     */
    int setTeahcerAbsent(int teacherID, Day day);

    /**
     * Deletes teacher absent from the timetable.teacherabsent
     * 
     * @param teacherID existed teacher id
     * @param day
     * @return true is deleted otherwise false
     */
    boolean deleteTeahcerAbsent(int teacherID, Day day);
    
    /**
     * The method finds a teacher by id from the timetable.teachers
     * @param teacherID
     * @return optional teacher
     */
    Optional<Teacher> findByID(int teacherID);
    
    /**
     * The method finds all teachers and returns optional list of teachers
     * @return optional list of teachers
     */
    Optional<List<Teacher>> findAllTeachers();
    
    /**
     * The method finds all teachers by lesson id and returns optional list of teachers
     * @param lessonID
     * @return optional list of teachers
     */
    Optional<Set<Teacher>> findTeachersByLessonId(int lessonID);
    
   /**
    * The method finds all teachers absent days by teacher id
    * @param teacherID
    * @return optional list of teachers
    */
    Optional<List<Day>> showTeacherAbsent(int teacherID);
    
    /**
     * The method changes password by teacher id 
     * @param teacherID
     * @param newPassword
     * @return count of updated rows otherwise 0
     */
    int changePassword(int teacherID, String newPassword);
    
    /**
     * The method lets update teacher's first name and last name and returns count of updated rows otherwise zero
     * @param teacher
     * @return count of updated rows otherwise zero
     */
    void updateTeacher(Teacher teacher);

    /**
     * The method checks if teacher is absent
     * @param teacher
     * @param checkedDate
     * @return true is absent otherwise false
     */
    boolean checkIsAbsent(Teacher teacher, LocalDate checkedDate);
}
