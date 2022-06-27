package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.service.exception.ServiceException;
import ua.foxminded.university.service.pojo.Teacher;

@TestInstance(Lifecycle.PER_CLASS)
class TeacherServiceImplTest {
    private TeacherServiceImpl teacherServiceImpl;
    private AnnotationConfigApplicationContext context;
    private Teacher teacher;
    private Exception exception;
    private String expectedMessage;
    private String actualMessage;
    private final int maxFirstNameSize = 30;
    private final int maxLastNameSize = 30;
    private final int maxPositionSize = 30;
    private final int maxPasswordSize = 10;

    @BeforeAll
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        teacherServiceImpl = context.getBean("teacherServiceImpl", TeacherServiceImpl.class);        
    }

    @Test
    void addTeacher_shouldThrowServiceException_whenInputIncorrectTeacherData() {
        teacher = new Teacher.TeacherBuidler().setFirstName(createCountOfSymbols(maxFirstNameSize + 1)).setLastName(createCountOfSymbols(maxLastNameSize + 1))
                .setPosition(createCountOfSymbols(maxPositionSize + 1)).setPassword(createCountOfSymbols(maxPasswordSize)).build();
        assertThrows(StringIndexOutOfBoundsException.class, () -> teacherServiceImpl.addTeacher(teacher));
    }
    
    @Test
    void addTeacher_shouldReturnResult_whenInputCorrectTeacherData() throws ServiceException {
        teacher = new Teacher.TeacherBuidler().setFirstName("Lord").setLastName("Voldemort")
                .setPosition("professor of evil").setPassword("555").build();
        assertEquals(1, teacherServiceImpl.addTeacher(teacher));
    }
    
    @Test
    void addTeacher_shouldThrowServiceExceptionMessage_whenInputIncorrectTeacherName() throws ServiceException {
        teacher = new Teacher.TeacherBuidler().setFirstName(createCountOfSymbols(maxFirstNameSize + 1)).setLastName("Voldemort")
                .setPosition("professor of evil").setPassword("555").build();
        exception = assertThrows(StringIndexOutOfBoundsException.class, () -> teacherServiceImpl.addTeacher(teacher));
        expectedMessage = "Teacher's first name is out of bound.";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    void addTeacher_shouldThrowServiceExceptionMessage_whenInputIncorrectTeacherSurname() throws ServiceException {
        teacher = new Teacher.TeacherBuidler().setFirstName("Lord").setLastName(createCountOfSymbols(maxLastNameSize + 1))
                .setPosition("professor of evil").setPassword("555").build();
        exception = assertThrows(StringIndexOutOfBoundsException.class, () -> teacherServiceImpl.addTeacher(teacher));
        expectedMessage = "Teacher's last name is out of bound.";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    void addTeacher_shouldThrowServiceExceptionMessage_whenInputIncorrectTeacherPosition() throws ServiceException {
        teacher = new Teacher.TeacherBuidler().setFirstName("Lord").setLastName("Voldemort")
                .setPosition(createCountOfSymbols(maxPositionSize + 1)).setPassword("555").build();
        exception = assertThrows(StringIndexOutOfBoundsException.class, () -> teacherServiceImpl.addTeacher(teacher));
        expectedMessage = "Teacher's position is out of bound.";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    void addTeacher_shouldThrowServiceExceptionMessage_whenInputIncorrectTeacherPassword() throws ServiceException {
        teacher = new Teacher.TeacherBuidler().setFirstName("Lord").setLastName("Voldemort")
                .setPosition("professor of evil").setPassword(createCountOfSymbols(maxPasswordSize + 1)).build();
        exception = assertThrows(StringIndexOutOfBoundsException.class, () -> teacherServiceImpl.addTeacher(teacher));
        expectedMessage = "Teacher's password is out of bound.";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    void changePassword_shouldThrowServiceException_whenInputIncorrectPassword() {
        assertThrows(StringIndexOutOfBoundsException.class, () -> teacherServiceImpl.changePassword(1, createCountOfSymbols(maxPasswordSize + 1)));
    }
    
    @Test
    void changePassword_shouldReturnResult_whenInputCorrecrData() throws ServiceException {
        assertEquals(1, teacherServiceImpl.changePassword(1, "5555"));
    }
    
    @Test
    void updateTeacher_shouldReturnResult_whenInputCorrectData() throws ServiceException {
        teacher = new Teacher.TeacherBuidler().setID(1).setFirstName("Vlad").setLastName("Cepish").setPosition("professor").build();
        assertEquals(1, teacherServiceImpl.updateTeacher(teacher));
    }
    
    @Test
    void updateTeacher_shouldThrowServiceExceptionMessage_whenInputIncorrectData() {
        teacher = new Teacher.TeacherBuidler().setID(1).setFirstName(createCountOfSymbols(maxFirstNameSize + 1)).setLastName("Voldemort").build();
        exception = assertThrows(StringIndexOutOfBoundsException.class, () -> teacherServiceImpl.addTeacher(teacher));
        expectedMessage = "Teacher's first name is out of bound.";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    void updateTeacher_shouldThrowServiceExceptionMessage_whenInputIncorrectTeacherSurname() throws ServiceException {
        teacher = new Teacher.TeacherBuidler().setID(1).setFirstName("Lord").setLastName(createCountOfSymbols(maxLastNameSize + 1)).build();
        exception = assertThrows(StringIndexOutOfBoundsException.class, () -> teacherServiceImpl.addTeacher(teacher));
        expectedMessage = "Teacher's last name is out of bound.";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    private String createCountOfSymbols(int count) {
        return Stream.generate(() -> "a")
                .limit(count)
                .collect(Collectors.joining());
    }
}
