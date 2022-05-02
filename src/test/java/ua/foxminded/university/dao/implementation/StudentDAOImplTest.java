package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.service.pojo.Student;

@Component
@TestInstance(Lifecycle.PER_CLASS)
class StudentDAOImplTest {
    private StudentDAOImpl studentDAOImpl;
    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        studentDAOImpl = context.getBean("studentDAOImpl", StudentDAOImpl.class);
    }

    @Test
    void addStudent_shouldReturnNegativeNumber_whenInputExistedStudent() throws SQLException {
        assertEquals(-1, studentDAOImpl
                .addStudent(new Student.StudentBuilder().setFirstName("Harry").setLastName("Potter").build()));
    }

    @Test
    void addStudent_shouldReturnNegativeNumber_whenInputNewStudentWithWrongGroupID() throws SQLException {
        assertEquals(-1, studentDAOImpl.addStudent(
                new Student.StudentBuilder().setFirstName("Aaaaa").setLastName("Bbbbb").setGroupID(11).build()));
    }

    @Test
    void addStudent_shouldReturnAddedStudentID_whenInputNewStudent() throws SQLException {
        assertEquals(109, studentDAOImpl.addStudent(
                new Student.StudentBuilder().setFirstName("Aaaaa").setLastName("Bbbbb").setGroupID(2).build()));
    }

    @Test
    void deleteStudent_shouldReturnCountOfDeletedRows_whenInputID() throws SQLException {
        int id = studentDAOImpl.addStudent(
                new Student.StudentBuilder().setFirstName("Aaaaa").setLastName("Bbbbb").setGroupID(2).build());
        assertEquals(1, studentDAOImpl.deleteStudent(id));
    }

    @ParameterizedTest(name = "{index}. When input not existed group id or negative number or zero will return false.")
    @ValueSource(ints = { 1000, -1, 0 })
    void deleteStudent_shouldReturnZero_whenInputIncorrectStudentID(int studentID) {
        assertEquals(0, studentDAOImpl.deleteStudent(studentID));
    }

    @Test
    void changeGroup_shouldReturnCountOfChangedGroups_whenInputExistedGroupIDAndStudentID() {
        assertEquals(1, studentDAOImpl.changeGroup(4, 1));
    }

    @ParameterizedTest(name = "{index}. When input not existed or incorrect lessonID {0} and not existed or incorrect groupID {1} will return negative number {2}.")
    @CsvSource({ "1, 100, -1", "100, 1, -1", "100, 100, -1" })
    void changeGroup_shouldReturnNegativeNumber_whenInputNotExistedGroupID() {
        assertEquals(-1, studentDAOImpl.changeGroup(8, 1));
    }
}
