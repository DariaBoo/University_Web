package ua.foxminded.university.service;

import java.util.List;

import ua.foxminded.university.service.pojo.Student;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
public interface StudentService {
    
    /**
     * Add new student to the timetable.students, if student was added returns 1 otherwise returns 0
     * @param student
     * @return 1 or 0
     */
    int addStudent(Student student) ;
    
    /**
     * Delete existed student from the timetable.students, if student was deleted returns 1 otherwise returns 0
     * @param studentID
     * @return 1 or 0
     */
    int deleteStudent(int studentID);
    
    /**
     * The method finds a student by student id
     * @param studentID existed student id
     * @return optional student
     */
    Student findByID(int studentID);
    
    /**
     * The method finds all students from the timetable.students
     * @return optional list of students
     */
    List<Student> findAllStudents();
    
    /**
     * The method finds students by group id
     * @return optional list of students
     */
    List<Student> findStudentsByGroup(int groupID);
    
    /**
     * The method changes password by student id
     * @param studentID
     * @param newPassword
     * @return count of updated rows otherwise 0
     */
    int changePassword(int studentID, String newPassword) ;
    
    /**
     * The method updates student name and surname
     * @param student
     * @return count of updated rows otherwise 0
     */
    int updateStudent(Student student);

}
