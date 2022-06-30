package ua.foxminded.university.service;

import java.util.List;

import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.Teacher;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface TeacherService {
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
     * @return count of deleted rows otherwise zero
     */
    int deleteTeacher(int teacherID);

    /**
     * Assigns lesson to teacher to the timetable.lessons_teachers
     * 
     * @param lessonID  existed lesson id
     * @param teacherID existed teacher id
     * @return count of updated rows otherwise -1
     */
    int assignLessonToTeacher(int lessonID, int teacherID);

    /**
     * Deletes lesson from teacher from the timetable.lessons_teachers
     * 
     * @param lessonID  existed lesson id
     * @param teacherID existed teacher id
     * @return count of deleted rows otherwise zero
     */
    int deleteLessonFromTeacher(int lessonID, int teacherID);

    /**
     * The method set date start and date end of teacher's absent
     * 
     * @param teacherID existed teacher id
     * @param day
     * @return count of updated rows otherwise -1
     */
    int setTeacherAbsent(int teacherID, Day day);

    /**
     * Deletes teacher absent from the timetable.teacherabsent
     * 
     * @param teacherID existed teacher id
     * @param day
     * @return count of deleted rows otherwise zero
     */
    int deleteTeacherAbsent(int teacherID, Day day);

    /**
     * The method finds all teachers absent days by teacher id
     * 
     * @param teacherID
     * @return list of teachers
     */
    List<Teacher> showTeacherAbsent(int teacherID);

    /**
     * The method finds a teacher by id from the timetable.teachers
     * 
     * @param teacherID
     * @return teacher
     */
    Teacher findByID(int teacherID);

    /**
     * The method finds all teachers and returns optional list of teachers
     * 
     * @return list of teachers
     */
    List<Teacher> findAllTeachers();

    /**
     * The method finds all teachers by department id and returns optional list of
     * teachers
     * 
     * @param departmentID
     * @return list of teachers
     */
    List<Teacher> findTeachersByDepartment(int departmentID);

    /**
     * The method finds all teachers by lesson id and returns optional list of
     * teachers
     * 
     * @param lessonID
     * @return optional list of teachers
     */
    List<Teacher> findTeachersByLessonId(int lessonID);

    /**
     * The method changes password by teacher id
     * 
     * @param teacherID
     * @param newPassword
     * @return count of updated rows otherwise 0
     */
    int changePassword(int teacherID, String newPassword);

    /**
     * The method lets update teacher's first name and last name and returns count
     * of updated rows otherwise zero
     * 
     * @param teacher
     * @return count of updated rows otherwise zero
     */
    int updateTeacher(Teacher teacher);
}
