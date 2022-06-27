package ua.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

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
     * @return count of added rows otherwise 0
     */
    int addStudent(Student student);

    /**
     * Deletes student from the timetable.students
     * 
     * @param studentID existed student id
     * @return count of deleted rows otherwise 0
     */
    int deleteStudent(int studentID);
    
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
     * @return count of updated rows otherwise 0
     */
    int changePassword(int studentID, String newPassword);
    
    /**
     * The method updates student name and surname
     * @param student
     * @return count of updated rows otherwise 0
     */
    int updateStudent(Student student);
    
    /**
     * Returns a size of column 'first_name' from the timetable.students
     * @return column's size
     */
    int getFirstNameMaxSize();
    
    /**
     * Returns a size of column 'last_name' from the timetable.students
     * @return column's size
     */
    int getLastNameMaxSize();
    
    /**
     * Returns a size of column 'id_card' from the timetable.students
     * @return column's size
     */
    int getIdCardMaxSize();
    
    /**
     * Returns a size of column 'password' from the timetable.students
     * @return column's size
     */
    int getPasswordMaxSize();
}
