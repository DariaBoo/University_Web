package ua.foxminded.university.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.exception.ServiceException;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDAO studentDAO;

    int result = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int addStudent(Student student) {
        try {
            result = studentDAO.addStudent(student);
        } catch (DAOException e) {
            log.error(e.getMessage(), e.getCause());
            throw new ServiceException(e.getMessage());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateStudent(Student student) {
        try {
            studentDAO.updateStudent(student);
        } catch (DAOException e) {
            log.error(e.getMessage(), e.getCause());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteStudent(int studentID) {
        return studentDAO.deleteStudent(studentID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean changePassword(int studentID, String newPassword) {
        return studentDAO.changePassword(studentID, newPassword);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Student findByID(int studentID) {
        Student resultStudent = studentDAO.findByID(studentID).orElseThrow(() -> new IllegalArgumentException(
                "Student with id " + studentID + " is not exist or student id is incorrect"));
        log.debug("Find student by id {} and return student - {}", studentID, resultStudent);
        return resultStudent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Student> findAllStudents() {
        List<Student> resultList = studentDAO.findAllStudents()
                .orElseThrow(() -> new IllegalArgumentException("Error occured while searching all students"));
        log.debug("Return list of students - {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Student> findStudentsByGroup(int groupID) {
        List<Student> resultList = studentDAO.findStudentsByGroup(groupID).orElseThrow(() -> new IllegalArgumentException("Incorrect group id " + groupID));
        log.debug("Find students by group id - {} and return list of students -{}", groupID, resultList);
        return resultList;
    }
}
