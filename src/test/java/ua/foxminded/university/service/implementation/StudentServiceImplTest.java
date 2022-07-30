package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.exception.ServiceException;
import ua.foxminded.university.springboot.AppSpringBoot;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppSpringBoot.class)
//@Sql({"/schema.sql", "/data.sql"})
@ActiveProfiles("test")
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
        student = Student.builder().firstName("Test").lastName("Test").group(group).idCard("A0")
                .password("1234").build();
        int countOfStudents = studentService.findAllStudents().size();
        assertEquals(countOfStudents + 1, studentService.addStudent(student));
    }

    @Test
    void updateStudent_shouldReturnResult_whenInputNotUniqueStudent() {
        student = Student.builder().firstName("Test").lastName("Test").group(group)
              .idCard("W2").password("1234").build();
        studentService.addStudent(student);
        studentService.addStudent(student);
      Student student2 = Student.builder().firstName("Test").lastName("Test").group(group)
              .idCard("W2").password("1234").build();     
      exception = assertThrows(ServiceException.class, () -> studentService.addStudent(student2));
      expectedMessage = "Student with name Test, surname Test and id card W2 already exists or student id card is not unique!";
      actualMessage = exception.getMessage();
      assertEquals(expectedMessage, actualMessage);
    }
}
