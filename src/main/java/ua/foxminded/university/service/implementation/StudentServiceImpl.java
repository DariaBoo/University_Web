package ua.foxminded.university.service.implementation;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.dao.exception.UniqueConstraintViolationException;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.service.entities.Role;
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
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final String defaultPassword = "1234";

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean addStudent(Student student) {
        try {
            student.setRole(Role.USER);
            student.setPassword(passwordEncoder.encode(defaultPassword));
            studentDAO.save(student);
            log.info("Add new student with id::{}", student.getId());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.error(e.getLocalizedMessage(), e.getCause());
            throw new UniqueConstraintViolationException(
                    "Student with first name - [" + student.getFirstName() + "], last name - [" + student.getLastName()
                            + "] and id card - [" + student.getIdCard() + "] already exists!");
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
                if (violation != null) {
                    log.error(violation.getMessageTemplate());
                    throw new ServiceException(violation.getMessageTemplate());
                }
            }
        }
        return studentDAO.existsById(student.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateStudent(Student student) {
        try {
            studentDAO.save(student);
            log.info("Update student with id :: {}", student.getId());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.error(e.getMessage(), e.getCause());
            throw new UniqueConstraintViolationException(
                    "Student with first name - [" + student.getFirstName() + "], last name - [" + student.getLastName()
                            + "] and id card - [" + student.getIdCard() + "] already exists!");
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) e.getCause()).getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
                if (violation != null) {
                    log.error(violation.getMessageTemplate());
                    throw new ServiceException(violation.getMessageTemplate());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteStudent(int studentId) {
        studentDAO.deleteById(studentId);
        log.info("Delete student with id :: {}", studentId);
        return !studentDAO.existsById(studentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void changePassword(int studentId, String newPassword) {
        Student student = findById(studentId);
        student.setPassword(passwordEncoder.encode(newPassword));
        studentDAO.save(student);
        log.debug("Password changed. Student id :: {}", studentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Student findById(int studentId) {
        Student resultStudent = studentDAO.findById(studentId).orElseThrow(() -> new IllegalArgumentException(
                "Student with id " + studentId + " is not exist or student id is incorrect"));
        log.debug("Found student by id :: {}", studentId);
        return resultStudent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable("students")
    public List<Student> findAllStudents() {
        List<Student> resultList = studentDAO.findAll();
        log.debug("Found all students, list size :: {}", resultList.size());
        return resultList;
    }
}
