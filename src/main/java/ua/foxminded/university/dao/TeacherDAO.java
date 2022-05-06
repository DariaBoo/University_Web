package ua.foxminded.university.dao;

import java.sql.SQLException;

import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.Teacher;

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
     * @throws SQLException
     */
    int addTeacher(Teacher teacher) throws SQLException;

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
     * The method lets change teacher's position in the timetable.teachers
     * 
     * @param teacherID existed teacher id
     * @param position
     * @return count of updated rows otherwise -1
     */
    int changePosition(int teacherID, String position);

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
     * @return count of deleted rows otherwise zero
     */
    int deleteTeahcerAbsent(int teacherID, Day day);
}
