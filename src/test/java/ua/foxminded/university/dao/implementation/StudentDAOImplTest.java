package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.*;

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
    void addStudent_shouldReturnZero_whenInputExistedStudent() {
        assertEquals(0, studentDAOImpl
                .addStudent(new Student.StudentBuilder().setFirstName("Harry").setLastName("Potter").build()));
    }

    @Test
    void addStudent_shouldReturnZero_whenInputNewStudentWithWrongGroupID() {
        assertEquals(0, studentDAOImpl.addStudent(
                new Student.StudentBuilder().setFirstName("Aaaaa").setLastName("Bbbbb").setGroupID(11).build()));
    }

    @Test
    void addStudent_shouldReturnCountOfAddedRows_whenInputNewStudent() {
        assertEquals(1, studentDAOImpl.addStudent(
                new Student.StudentBuilder().setFirstName("Aaaaa").setLastName("Bbbbb").setGroupID(2).build()));
    }

    @Test
    void deleteStudent_shouldReturnCountOfDeletedRows_whenInputID()  {
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

    @ParameterizedTest(name = "{index}. When input not existed or incorrect studentID {0} and not existed or incorrect groupID {1} will return zero {2}.")
    @CsvSource({ "1, 100, 0", "200, 1, 0", "100, 100, 0" })
    void changeGroup_shouldReturnZero_whenInputNotExistedGroupID(int studentID, int groupID, int result) {
        assertEquals(result, studentDAOImpl.changeGroup(studentID, groupID));
    }
}
