package ua.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import ua.foxminded.university.service.entities.Student;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface StudentDAO {

    /**
     * The method adds new student to the timetable.studetns
     * 
     * @param student
     * @return count of added rows otherwise 0
     */
    int addStudent(Student student);

    /**
     * Deletes student from the timetable.students
     * 
     * @param studentID existed student id
     * @return true is deleted otherwise false
     */
    boolean deleteStudent(int studentID);
    
    /**
     * The method finds a student by student id
     * @param studentID existed student id
     * @return optional student
     */
    Optional<Student> findByID(int studentID);
    
    /**
     * The method finds all students from the timetable.students
     * @return optional list of students
     */
    Optional<List<Student>> findAllStudents();
    
    /**
     * The method finds students by group id
     * @return optional list of students
     */
    Optional<List<Student>> findStudentsByGroup(int groupID);
    
    /**
     * The method changes password by student id
     * @param studentID
     * @param newPassword
     * @return true is changed, otherwise false
     */
    boolean changePassword(int studentID, String newPassword);
    
    /**
     * The method updates student name and surname
     * @param student
     * @return count of updated rows otherwise 0
     */
    void updateStudent(Student student);
}
