package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.User;
import ua.foxminded.university.service.exception.ServiceException;
import ua.foxminded.university.springboot.AppSpringBoot;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppSpringBoot.class)
//@Sql({"/schema.sql", "/data.sql"})
@ActiveProfiles("test")
@Transactional
class TeacherServiceImplTest {
    
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Teacher teacher;
    private Exception exception;
    private String expectedMessage;
    private String actualMessage;
    private final User user = new User();

    @BeforeEach
    void init() {
        new EmbeddedDatabaseBuilder().setName("test").setType(EmbeddedDatabaseType.H2)
        .addScript("classpath:tablesTest.sql").build(); 
        user.setFirstName("test");
        user.setLastName("test");
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("555"));
    }

    @Test
    void addTeacher_shouldReturnResult_whenInputCorrectTeacherData() {
        teacher = Teacher.builder().user(user).position("test").build();
        assertEquals(11, teacherService.addTeacher(teacher));
    }
    
    @Test
    void updateTeacher_shouldReturnResult_whenInputCorrectData() {
        assertTrue(teacherService.addTeacher(teacher));
    }
    
    @Test
    void updateTeacher_shouldThrowServiceExceptionMessage_whenInputIncorrectTeacherData() {
        teacher = Teacher.builder().id(1).user(user).position("test").departmentId(1).build();
        teacherService.addTeacher(teacher);
        Teacher teacher2 = Teacher.builder().id(1).user(user).position("test").departmentId(1).build();
        exception = assertThrows(ServiceException.class, () -> teacherService.addTeacher(teacher2));
        expectedMessage = "Teacher with name test, surname test already exists!";
        actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
