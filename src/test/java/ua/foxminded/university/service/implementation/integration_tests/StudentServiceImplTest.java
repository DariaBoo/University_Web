package ua.foxminded.university.service.implementation.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.User;
import ua.foxminded.university.service.exception.EntityConstraintViolationException;
import ua.foxminded.university.service.exception.UserNotFoundException;
import ua.foxminded.university.service.implementation.StudentServiceImpl;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/students.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class StudentServiceImplTest {

    @Autowired
    private StudentServiceImpl studentService;
    @Autowired
    private GroupDAO groupDao;
    @Autowired
    private UserDAO userDao;

    private Student student;
    private Student student2;
    private User user;
    private User user2;
    private Group group;

    @BeforeEach
    void setup() {
        user = User.builder().firstName("name").lastName("surname").username("username").password("password").build();
        user2 = User.builder().firstName("name2").lastName("surname2").username("username2").password("password")
                .build();
        userDao.save(user);
        userDao.save(user2);
        group = groupDao.findById(1).get();
        student = Student.builder().user(user).group(group).idCard("AA-1").build();
        student2 = Student.builder().user(user2).group(group).idCard("AA-2").build();
    }

    @Test
    void addStudent_shouldReturnAddedStudent_whenInputCorrectData() {
        assertEquals(student, studentService.addStudent(student));
    }

    @Test
    void addStudent_shouldThrowUniqueConstraintViolationException_whenInputExistedUser() {
        studentService.addStudent(student);
        assertThrows(EntityConstraintViolationException.class, () -> studentService.addStudent(student));
    }

    @Test
    void updateStudent_shouldReturnStudent_whenInputCorrectData() {
        studentService.addStudent(student);
        String newIdCard = "BB-1";
        student.setIdCard(newIdCard);
        assertEquals(student.getIdCard(), studentService.updateStudent(student).getIdCard());
    }

    @Test
    void updateStudent_shouldReturnEmptyStudent_whenInputNotExistedStudent() {
        assertEquals(new Student(), studentService.updateStudent(student));
    }

    @Test
    void deleteStudent_shouldDeleteEntity() {
        studentService.addStudent(student);
        int size = studentService.findAllStudents().size();
        studentService.deleteStudent(student.getId());
        assertEquals(size - 1, studentService.findAllStudents().size());
    }

    @Test
    void deleteStudent_shouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> studentService.deleteStudent(1));
    }

    @Test
    void changePassword() {
        int studentId = studentService.addStudent(student).getId();
        String newPassword = "$2a$12$oLyWRUPG6zohk4buvDF3d.4VVJLtVeSBNR3fCNM1CFuRjKlLwbGGi";
        studentService.changePassword(studentId, newPassword);
        student.getUser().setPassword(newPassword);
        assertEquals(student, studentService.findById(studentId));
    }

    @Test
    void findById_shouldReturnStudent_whenInputExistedId() {
        int studentId = studentService.addStudent(student).getId();
        assertEquals(student, studentService.findById(studentId));
    }

    @Test
    void findById_shouldThrowIllegalArgumentException() {
        int notExistedId = studentService.findAllStudents().size();
        assertThrows(IllegalArgumentException.class, () -> studentService.findById(notExistedId));
    }

    @Test
    void findAllStudents_shouldReturnList() {
        studentService.addStudent(student);
        studentService.addStudent(student2);
        assertEquals(2, studentService.findAllStudents().size());
    }

    @Test
    void findByUsername_shouldReturnStudent_whenInputExistedData() {
        studentService.addStudent(student);
        assertEquals(student, studentService.findByUsername(student.getUser().getUsername()));
    }

    @Test
    void findByUsername_shouldThrowUserNotFoundException() {
        String notExistedUsername = "someUsername";
        assertThrows(UserNotFoundException.class, () -> studentService.findByUsername(notExistedUsername));
    }
}
