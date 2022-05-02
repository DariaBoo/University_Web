package ua.foxminded.university.dao;

import java.sql.SQLException;

import ua.foxminded.university.service.pojo.Student;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface StudentDAO {

    /**
     * The method adds new student to the timetable.studetns
     * 
     * @param student
     * @return count of added rows otherwise -1
     * @throws SQLException
     */
    int addStudent(Student student) throws SQLException;

    /**
     * Deletes student from the timetable.students
     * 
     * @param studentID existed student id
     * @return count of deleted rows otherwise zero
     */
    int deleteStudent(int studentID);

    /**
     * The methods let change group id in the timetable.students
     * 
     * @param groupID   existed group id
     * @param studentID existed student id
     * @return count of updated rows otherwise -1
     */
    int changeGroup(int groupID, int studentID);
}
