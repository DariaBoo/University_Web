package ua.foxminded.university.service.implementation.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.User;
import ua.foxminded.university.service.exception.UniqueConstraintViolationException;
import ua.foxminded.university.service.implementation.StudentServiceImpl;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/students.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class StudentServiceImplTest {

    @Autowired
    private StudentServiceImpl studentService;
    @Autowired
    private GroupDAO groupDao;

    private Student student;
    private User user;
    private Group group;

    @BeforeEach
    void setup() {
        user = User.builder().id(1).firstName("name").lastName("surname").username("username").password("password")
                .build();
        group = groupDao.findById(1).get();
        student = Student.builder().id(1).user(user).group(group).idCard("AA-1").build();
    }

    @Test
    void addStudent_shouldReturnAddedStudent_whenInputCorrectData() {
        assertEquals(student, studentService.addStudent(student));
    }

    @Test
    void addStudent_shouldThrowUniqueConstraintViolationException_whenInputExistedUser() {
        studentService.addStudent(student);
        assertThrows(UniqueConstraintViolationException.class, () -> studentService.addStudent(student));
    }
}
