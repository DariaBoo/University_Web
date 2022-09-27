package ua.foxminded.university.service;

import java.util.List;

import ua.foxminded.university.service.entities.Student;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
public interface StudentService {

    /**
     * Add Student to the timetable.students, if student was added returns 1
     * otherwise returns 0
     * 
     * @param student
     * @return is added true, otherwise - false
     */
    boolean addStudent(Student student);

    /**
     * Delete existed student from the timetable.students, if student was deleted
     * returns 1 otherwise returns 0
     * 
     * @param studentId
     * @return is deleted true, otherwise - false
     */
    boolean deleteStudent(int studentId);

    /**
     * The method finds a student by student id
     * 
     * @param studentId existed student id
     * @return student
     */
    Student findById(int studentId);

    /**
     * The method finds all students from the timetable.students
     * 
     * @return optional list of students
     */
    List<Student> findAllStudents();

    /**
     * The method changes password by student id
     * 
     * @param studentId
     * @param newPassword
     */
    void changePassword(int studentId, String newPassword);

    /**
     * The method updates student name and surname
     * 
     * @param student
     */
    void updateStudent(Student student);
    
    Student findByUsername(String username);
}
