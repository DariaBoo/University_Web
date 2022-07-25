package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.config.HibernateConfigTest;
import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Student;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateConfigTest.class }, loader = AnnotationConfigContextLoader.class)
class StudentDAOImplTest {

    @Autowired
    private StudentDAO studentDAO;

    private Student student;
    private List<Student> students = new ArrayList<>();
    private final Group group = new Group();

    @BeforeEach
    void init() throws SQLException {
        new EmbeddedDatabaseBuilder().setName("test").setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:tablesTest.sql").build();
        group.setId(1);
    }

    @Test
    @Transactional
    void addStudent_shouldReturnAddedStudentId_whenInputNewStudent() {
        student = Student.builder().firstName("Test").lastName("Test").group(group).idCard("A0")
                .password("1234").build();
        int countOfStudents = studentDAO.findAllStudents().get().size();
        assertEquals(countOfStudents + 1, studentDAO.addStudent(student));
    }

    @Test
    @Transactional
    void addStudent_shouldThrowConstraintViolationException_whenInputNewStudentWithWrongGroupID() {
        group.setId(15);
        student = Student.builder().firstName("Aaaaa").lastName("Bbbbb").idCard("A0")
                .group(group).password("1234").build();
        assertThrows(DAOException.class, () -> studentDAO.addStudent(student));
    }

    @Test
    @Transactional
    void addStudent_shouldThrowDataException_whenStudentNameIsOutOfBound() {
        student = Student.builder().firstName("Aaaaaiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii")
                .lastName("Bbbbb").idCard("A0").group(group).password("1234").build();
        assertThrows(org.hibernate.exception.DataException.class, () -> studentDAO.addStudent(student));
    }

    @Test
    @Transactional
    void addStudent_shouldThrowConstraintViolationException_whenInputExistedStudent() {
        group.setId(1);
        student = Student.builder().firstName("Test").lastName("Test").group(group).idCard("A0")
                .password("1234").build();
        studentDAO.addStudent(student);
        group.setId(2);      
        Student student2 = Student.builder().firstName("Test").lastName("Test").group(group)
                .idCard("A0").password("1234").build();

        assertThrows(DAOException.class, () -> studentDAO.addStudent(student2));
    }

    @Test
    @Transactional
    void addStudent_shouldThrowConstraintViolationException_whenInputNotUniqueIdCard() {
        student = Student.builder().firstName("Test").lastName("Test").group(group).idCard("A0")
                .password("1234").build();
        Student student2 = Student.builder().firstName("Test2").lastName("Test2").group(group)
                .idCard("A0").password("1234").build();
        studentDAO.addStudent(student);
        assertThrows(DAOException.class, () -> studentDAO.addStudent(student2));
    }

    @Test
    @Transactional
    void deleteStudent_shouldDeleteAStudent_whenInputID() {
        student = Student.builder().firstName("Test").lastName("Test").group(group).idCard("A0")
                .password("1234").build();
        int studentID = studentDAO.addStudent(student);
        int sizeBeforeDeletingAStudent = studentDAO.findAllStudents().get().size();
        studentDAO.deleteStudent(studentID);
        int sizeAfterDeletingAStudent = studentDAO.findAllStudents().get().size();
        assertEquals(sizeBeforeDeletingAStudent - 1, sizeAfterDeletingAStudent);
    }

    @ParameterizedTest(name = "{index}. When input not existed student id or negative number or zero will return false.")
    @Transactional
    @ValueSource(ints = { 1000, -1, 0 })
    void deleteStudent_shouldReturnFalse_whenInputIncorrectStudentID(int studentID) {
        assertFalse(studentDAO.deleteStudent(studentID));
    }

    @Test
    @Transactional
    void findByID_shouldReturnStudent_whenInputExistedID() {
        student = Student.builder().firstName("Test").lastName("Test").group(group).idCard("A0")
                .password("1234").build();
        int studentID = studentDAO.addStudent(student);
        assertEquals(Optional.of(student), studentDAO.findByID(studentID));
    }

    @ParameterizedTest(name = "{index}. When input not existed student id or negative number or zero will return false.")
    @Transactional
    @ValueSource(ints = { 1000, -1, 0 })
    void findByID_shouldReturnEmptyOptional_whenInputIncorrectStudentID(int studentID) {
        assertEquals(Optional.empty(), studentDAO.findByID(studentID));
    }

    @Test
    @Transactional
    void findByID_shouldReturnFalse_whenInputIncorrectStudentID() {
        assertFalse(studentDAO.findByID(-1).isPresent());
    }

    @Test
    @Transactional
    void findByID_shouldReturnEmpty_whenInputLongValue() {
        assertEquals(Optional.empty(), studentDAO.findByID((Integer.MAX_VALUE + 1)));
    }

    @Test
    @Transactional
    void findAllStudents_shouldReturnFirstThreeStudents_whenCallTheMethodWithLimitThree() {
        students.add(Student.builder().id(1).firstName("Harry").lastName("Potter").group(group)
                .idCard("1IE").password("1234").build());
        students.add(Student.builder().id(2).firstName("Mandy").lastName("Finch-Fletchley")
                .group(group).idCard("2WC").password("1234").build());
        students.add(Student.builder().id(3).firstName("Pansy").lastName("Brocklehurst")
                .group(group).idCard("3SN").password("1234").build());
        assertEquals(students, studentDAO.findAllStudents().get().stream().limit(3).collect(Collectors.toList()));
    }

    @Test
    @Transactional
    void findAllStudents_shouldReturnCountOfStudents() {
        assertEquals(108, studentDAO.findAllStudents().get().stream().count());
    }

    @Test
    @Transactional
    void findStudentsByGroup_shouldReturnCountOfStudents_whenInputExistedGroupID() {
        assertEquals(17, studentDAO.findStudentsByGroup(2).get().stream().count());
    }

    @Test
    @Transactional
    void findStudentsByGroup_shouldReturnZero_whenInputNotExistedGroupID() {
        assertEquals(0, studentDAO.findStudentsByGroup(20).get().stream().count());
    }

    @Test
    @Transactional
    void findStudentsByGroup_shouldReturnEmptyOptionalList_whenInputNotExistedGroupID() {
        assertEquals(Optional.of(new ArrayList<Student>()), studentDAO.findStudentsByGroup(20));
    }

    @Test
    @Transactional
    void findStudentsByGroup_shouldReturnZero_whenInputNegativeNumber() {
        assertEquals(0, studentDAO.findStudentsByGroup(-2).get().stream().count());
    }

    @Test
    @Transactional
    void findStudentsByGroup_shouldReturnZero_whenInputLongNumber() {
        assertEquals(0, studentDAO.findStudentsByGroup(Integer.MAX_VALUE + 1).get().stream().count());
    }

    @Test
    @Transactional
    void changePassword_shouldReturnOne_whenInputExistedStudentID() {
        assertTrue(studentDAO.changePassword(1, "5555"));
    }

    @ParameterizedTest(name = "{index}. When input not existed student id or negative number or zero will return false.")
    @Transactional
    @ValueSource(ints = { 1000, -1, 0 })
    void changePassword_shouldThrowIllegalArgumentException_whenInputNotExistedStudentID(int studentID) {
        assertFalse(studentDAO.changePassword(studentID, "5555"));
    }

    @Test
    @Transactional
    void updateStudent_shouldUpdateStudent_whenInputCorrectStudent() {
        student = Student.builder().firstName("Test").lastName("Test").group(group).idCard("W2")
                .password("1234").build();
        int studentID = studentDAO.addStudent(student);
        student.setFirstName("Test2");
        studentDAO.updateStudent(student);
        assertEquals("Test2", studentDAO.findByID(studentID).get().getFirstName());
    }

//    @Test
//    @Transactional
//    void updateStudent_shouldReturnOne_whenInputUpdatedStudentData() {
//        student = Student.builder().setFirstName("Test").setLastName("Test").group(group)
//                .idCard("W2").setPassword("1234").build();
//        int id = studentDAO.addStudent(student);
//        Student student2 = Student.builder().setFirstName("Test").setLastName("Test").group(group)
//                .idCard("A0").setPassword("1234").build();        
//        int id2 = studentDAO.addStudent(student2);
//        student2.idCard("W2");
//        assertThrows(DAOException.class, () -> studentDAO.updateStudent(student2));
//    }
}
