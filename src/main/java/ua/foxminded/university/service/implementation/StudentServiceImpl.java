package ua.foxminded.university.service.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.university.dao.implementation.StudentDAOImpl;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.service.pojo.Student;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Service
public class StudentServiceImpl implements StudentService {
    private final StudentDAOImpl studentDAOImpl;
    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class.getName());

    /**
     * Returns instance of the class
     * @param studentDAOImpl
     */
    @Autowired
    public StudentServiceImpl(StudentDAOImpl studentDAOImpl) {
        this.studentDAOImpl = studentDAOImpl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addStudent(Student student) {
        log.trace("Add new student {}", student);
        int result = 0;
        log.trace("Check if student's first name is not out of bound");
        if (student.getFirstName().length() > studentDAOImpl.getFirstNameMaxSize()) {
            log.error("Student first name - {} is out of bound.", student.getFirstName());
            throw new StringIndexOutOfBoundsException("Student first name is out of bound.");
        }
        log.trace("Check if student's last name is not out of bound");
        if (student.getLastName().length() > studentDAOImpl.getLastNameMaxSize()) {
            log.error("Student last name - {} is out of bound.", student.getLastName());
            throw new StringIndexOutOfBoundsException("Student last name is out of bound.");
        }
        log.trace("Check if student's id card is not out of bound");
        if (student.getIdCard().length() > studentDAOImpl.getIdCardMaxSize()) {
            log.error("Student id card - {} is out of bound.", student.getIdCard());
            throw new StringIndexOutOfBoundsException("Student id card is out of bound.");
        }
        result = studentDAOImpl.addStudent(student);
        log.debug("Took a result {} of adding a new student", result);
        return result; 
        }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateStudent(Student student) {
        log.trace("Update existed student {}", student);
        int result = 0;
        log.trace("Check if student's first name is not out of bound");
        result = studentDAOImpl.updateStudent(student);
        log.debug("Took a result {} of updating a student", result);
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int changePassword(int studentID, String newPassword) {
        log.trace("Change student password");
        int result = 0;
        log.trace("Check the size of password {}", newPassword);
        if (newPassword.length() <= studentDAOImpl.getPasswordMaxSize()) {
            result = studentDAOImpl.changePassword(studentID, newPassword);
            log.debug("Took the result {} of changing password", result);
        } else {
            log.error("A password can't be more than 10 symbols. Current password length {}", newPassword.length());
            throw new StringIndexOutOfBoundsException("A password can't be more than 10 symbols");
        }
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteStudent(int studentID) {
        log.trace("Delete existed student by id {}", studentID);
        int result = studentDAOImpl.deleteStudent(studentID);
        log.debug("Took the result {} of deleting student from the database", result);
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Student findByID(int studentID) {
        log.trace("Find student by id {}", studentID);
        return studentDAOImpl.findByID(studentID).orElseThrow(() -> new IllegalArgumentException("Error occured"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> findAllStudents() {
        log.trace("Find all students");
        return studentDAOImpl.findAllStudents().orElseThrow(() -> new IllegalArgumentException("Error occured"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> findStudentsByGroup(int groupID) {
        log.trace("Find students by group");
        return studentDAOImpl.findStudentsByGroup(groupID).orElseThrow(() -> new IllegalArgumentException("Error occured"));
    }
}
