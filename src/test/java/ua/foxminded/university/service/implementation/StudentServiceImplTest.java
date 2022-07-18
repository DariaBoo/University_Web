package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.config.HibernateConfigTest;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.exception.ServiceException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateConfigTest.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
class StudentServiceImplTest {
    @Autowired
    private StudentService studentService;

    private Student student;
    private Exception exception;
    private String expectedMessage;
    private String actualMessage;
    private final Group group = new Group();
    
    @BeforeEach
    void init() throws SQLException {
        new EmbeddedDatabaseBuilder().setName("test").setType(EmbeddedDatabaseType.H2)
        .addScript("classpath:tablesTest.sql").build();
        group.setId(1);
    }
    
    @Test
    void addStudent_shouldReturnResult_whenInputCorrectData() {
        student = new Student.StudentBuilder().setFirstName("Test").setLastName("Test").setGroup(group).setIdCard("A0")
                .setPassword("1234").build();
        int countOfStudents = studentService.findAllStudents().size();
        assertEquals(countOfStudents + 1, studentService.addStudent(student));
    }

    @Test
    void updateStudent_shouldReturnResult_whenInputNotUniqueStudent() {
        student = new Student.StudentBuilder().setFirstName("Test").setLastName("Test").setGroup(group)
              .setIdCard("W2").setPassword("1234").build();
        studentService.addStudent(student);
        studentService.addStudent(student);
      Student student2 = new Student.StudentBuilder().setFirstName("Test").setLastName("Test").setGroup(group)
              .setIdCard("W2").setPassword("1234").build();     
      exception = assertThrows(ServiceException.class, () -> studentService.addStudent(student2));
      expectedMessage = "Student with name Test, surname Test and id card W2 already exists or student id card is not unique!";
      actualMessage = exception.getMessage();
      assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void changePassword_shouldReturnResutl_whenInputCorrectData() {
        assertTrue(studentService.changePassword(1, "5555"));
    }
}
