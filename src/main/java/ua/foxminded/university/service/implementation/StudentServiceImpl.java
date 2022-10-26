package ua.foxminded.university.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.service.RoleService;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.service.entities.Role;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.exception.UniqueConstraintViolationException;
import ua.foxminded.university.service.exception.UserNotFoundException;

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
    private RoleService roleService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final String defaultPassword = "1234";
    private final String defaultRole = "STUDENT";
    private final List<Role> roles = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Student addStudent(Student student) {
        Student savedStudent = studentDAO.findByUserUsername(student.getUser().getUsername());
        if (savedStudent == null) {
            savedStudent = studentDAO.save(setDefaultData(student));
            log.info("Added a new student with id::{}", student.getId());
        } else {
            log.warn("Student already exist with id [{}]", savedStudent.getId());
            throw new UniqueConstraintViolationException(
                    "Student with username - [" + student.getUser().getUsername() + "] already exists!");
        }
        return savedStudent; 
    }

    private Student setDefaultData(Student student) {
        roles.add(roleService.findByName(defaultRole));
        student.getUser().setRoles(roles);
        student.getUser().setPassword(passwordEncoder.encode(defaultPassword));
        return student;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Student updateStudent(Student student) {
        Student updatedStudent = studentDAO.findByUserUsername(student.getUser().getUsername());
        if (updatedStudent == null) {
            updatedStudent = studentDAO.save(student);
            log.info("Update student with id :: {}", student.getId());
        } else {
            log.warn("Student already exist with id [{}]", updatedStudent.getId());
            throw new UniqueConstraintViolationException(
                    "Student with username - [" + student.getUser().getUsername() + "] already exists!");
        }
        return updatedStudent;
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
        student.getUser().setPassword(passwordEncoder.encode(newPassword));
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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Student findByUsername(String username) {
        Student student = studentDAO.findByUserUsername(username);
        if (student == null) {
            throw new UserNotFoundException("Student with username " + username + " is not exist");
        }
        return student;
    }
}
