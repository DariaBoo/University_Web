package ua.foxminded.university.service.implementation;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.service.exception.ServiceException;
import ua.foxminded.university.service.pojo.Student;

class StudentServiceImplTest {
    private StudentServiceImpl studentServiceImpl;
    private AnnotationConfigApplicationContext context;
    private Student student;
    private final int maxFirstNameSize = 30;
    private final int maxLastNameSize = 30;
    private final int maxIdCardSize = 5;
    private final int maxPasswordSize = 10;
    
    @BeforeEach
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        studentServiceImpl = context.getBean("studentServiceImpl", StudentServiceImpl.class);
    }
    
    @Test
    void addStudent_shouldThrowServiceException_whenInputIncorrectStudentData() {
        student = new Student.StudentBuilder().setFirstName(createCountOfSymbols(maxFirstNameSize + 1)).setLastName(createCountOfSymbols(maxLastNameSize + 1)).setIdCard(createCountOfSymbols(maxIdCardSize + 1)).build();
        assertThrows(StringIndexOutOfBoundsException.class, () -> studentServiceImpl.addStudent(student));
    }
    @Test
    void addStudent_shouldThrowServiceException_whenInputIncorrectStudentName() {
        student = new Student.StudentBuilder().setFirstName(createCountOfSymbols(maxFirstNameSize + 1)).setLastName("Potter").setIdCard("12345").build();
        assertThrows(StringIndexOutOfBoundsException.class, () -> studentServiceImpl.addStudent(student));
    }
    @Test
    void addStudent_shouldThrowServiceException_whenInputIncorrectStudentSurname() {
        student = new Student.StudentBuilder().setFirstName("Harry").setLastName(createCountOfSymbols(maxLastNameSize + 1)).setIdCard("12345").build();
        assertThrows(StringIndexOutOfBoundsException.class, () -> studentServiceImpl.addStudent(student));
    }
    @Test
    void addStudent_shouldThrowServiceException_whenInputIncorrectStudentIdCard() {
        student = new Student.StudentBuilder().setFirstName("Harry").setLastName("Potter").setIdCard(createCountOfSymbols(maxIdCardSize + 1)).build();
        assertThrows(StringIndexOutOfBoundsException.class, () -> studentServiceImpl.addStudent(student));
    }
    @Test
    void addStudent_shouldReturnResult_whenInputCorrectData() throws ServiceException {
        student = new Student.StudentBuilder().setFirstName("Marry").setLastName("Hotter").setGroupID(1).setPassword("2322").setIdCard("5555").build();
        assertEquals(1, studentServiceImpl.addStudent(student));
    }
//    @Test
//    void updateStudent_shouldThrowServiceException_whenInputIncorrectStudentData() {
//        student = new Student.StudentBuilder().setID(1).setFirstName(createCountOfSymbols(maxFirstNameSize + 1)).setLastName(createCountOfSymbols(maxLastNameSize + 1)).build();
//        assertThrows(StringIndexOutOfBoundsException.class, () -> studentServiceImpl.updateStudent(student));
//    }
//    @Test
//    void updateStudent_shouldThrowServiceException_whenInputIncorrectStudentName() {
//        student = new Student.StudentBuilder().setID(1).setFirstName(createCountOfSymbols(maxFirstNameSize + 1)).setLastName("Potter").build();
//        assertThrows(StringIndexOutOfBoundsException.class, () -> studentServiceImpl.updateStudent(student));
//    }
//    @Test
//    void updateStudent_shouldThrowServiceException_whenInputIncorrectStudentSurname() {
//        student = new Student.StudentBuilder().setID(1).setFirstName("Harry").setLastName(createCountOfSymbols(maxLastNameSize + 1)).build();
//        assertThrows(StringIndexOutOfBoundsException.class, () -> studentServiceImpl.updateStudent(student));
//    }
    @Test
    void updateStudent_shouldReturnResult_whenInputCorrectStudentData() throws ServiceException {
        student = new Student.StudentBuilder().setID(1).setFirstName("Harry").setLastName("Potter").setGroupID(1).setIdCard("8NJ").build();
        assertEquals(1, studentServiceImpl.updateStudent(student));       
    }
    @Test
    void changePasswod_shouldThrowServiceException_whenInputIncorrectPassword() {
        assertThrows(StringIndexOutOfBoundsException.class, () -> studentServiceImpl.changePassword(1, createCountOfSymbols(maxPasswordSize + 1)));
    }
    @Test
    void changePassword_shouldReturnResutl_whenInputCorrectData() throws ServiceException {
        assertEquals(1, studentServiceImpl.changePassword(1, "5555"));
    }
    private String createCountOfSymbols(int count) {
        return Stream.generate(() -> "a")
                .limit(count)
                .collect(Collectors.joining());
    }
}
