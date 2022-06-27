package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.service.pojo.Student;

class StudentDAOImplTest {
    private StudentDAOImpl studentDAOImpl;
    private AnnotationConfigApplicationContext context;
    private Student student;
    private List<Student> students = new ArrayList<>();
    private final int maxFirstNameSize = 30;
    private final int maxLastNameSize = 30;
    private final int maxIdCardSize = 5;
    private final int maxPasswordSize = 10;

    @BeforeEach
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        studentDAOImpl = context.getBean("studentDAOImpl", StudentDAOImpl.class);
    }

    @Test
    void addStudent_shouldReturnZero_whenInputExistedStudent() {
        student = new Student.StudentBuilder().setFirstName("Harry").setLastName("Potter").build();
        assertEquals(0, studentDAOImpl.addStudent(student));
    }

    @Test
    void addStudent_shouldReturnZero_whenInputNewStudentWithWrongGroupID() {
        student = new Student.StudentBuilder().setFirstName("Aaaaa").setLastName("Bbbbb").setGroupID(11).build();
        assertEquals(0, studentDAOImpl.addStudent(student));
    }

    @Test
    void addStudent_shouldReturnCountOfAddedRows_whenInputNewStudent() {
        student = new Student.StudentBuilder().setFirstName("Aaaaa").setLastName("Bbbbb").setGroupID(2)
                .setIdCard("IA-20").build();
        assertEquals(1, studentDAOImpl.addStudent(student));
    }

    @Test
    void deleteStudent_shouldReturnCountOfDeletedRows_whenInputID() {
        student = new Student.StudentBuilder().setFirstName("Aaaaa").setLastName("Bbbbb").setGroupID(2)
                .setIdCard("IA-20").build();
        int id = studentDAOImpl.addStudent(student);
        assertEquals(1, studentDAOImpl.deleteStudent(id));
    }

    @ParameterizedTest(name = "{index}. When input not existed group id or negative number or zero will return false.")
    @ValueSource(ints = { 1000, -1, 0 })
    void deleteStudent_shouldReturnZero_whenInputIncorrectStudentID(int studentID) {
        assertEquals(0, studentDAOImpl.deleteStudent(studentID));
    }

    @Test
    void findByID_shouldReturnStudent_whenInputExistedID() {
        student = new Student.StudentBuilder().setID(1).setFirstName("Harry").setLastName("Potter").setGroupID(1)
                .setIdCard("1IE").build();
        assertEquals(Optional.of(student), studentDAOImpl.findByID(1));
    }

    @Test
    void findByID_shouldReturnEmpty_whenInputNotExistedID() {
        assertEquals(Optional.empty(), studentDAOImpl.findByID(1000));
    }

    @Test
    void findByID_shouldReturnEmpty_whenInputNegativeNumber() {
        assertEquals(Optional.empty(), studentDAOImpl.findByID(-1));
    }

    @Test
    void findByID_shouldReturnEmpty_whenInputLongValue() {
        assertEquals(Optional.empty(), studentDAOImpl.findByID((Integer.MAX_VALUE + 1)));
    }

    @Test
    void findAllStudents_shouldReturnFirstThreeStudents_whenCallTheMethodWithLimitThree() {
        students.add(new Student.StudentBuilder().setID(1).setFirstName("Harry").setLastName("Potter").setGroupID(1)
                .setIdCard("1IE").build());
        students.add(new Student.StudentBuilder().setID(2).setFirstName("Mandy").setLastName("Finch-Fletchley")
                .setGroupID(1).setIdCard("2WC").build());
        students.add(new Student.StudentBuilder().setID(3).setFirstName("Pansy").setLastName("Brocklehurst")
                .setGroupID(1).setIdCard("3SN").build());
        assertEquals(students, studentDAOImpl.findAllStudents().get().stream().limit(3).collect(Collectors.toList()));
    }

    @Test
    void findAllStudents_shouldReturnCountOfStudents() {
        assertEquals(108, studentDAOImpl.findAllStudents().get().stream().count());
    }

    @Test
    void findStudentsByGroup_shouldReturnCountOfStudents_whenInputExistedGroupID() {
        assertEquals(17, studentDAOImpl.findStudentsByGroup(2).get().stream().count());
    }

    @Test
    void findStudentsByGroup_shouldReturnZero_whenInputNotExistedGroupID() {
        assertEquals(0, studentDAOImpl.findStudentsByGroup(20).get().stream().count());
    }

    @Test
    void findStudentsByGroup_shouldReturnEmptyOptionalList_whenInputNotExistedGroupID() {
        assertEquals(Optional.of(new ArrayList<Student>()), studentDAOImpl.findStudentsByGroup(20));
    }

    @Test
    void findStudentsByGroup_shouldReturnZero_whenInputNegativeNumber() {
        assertEquals(0, studentDAOImpl.findStudentsByGroup(-2).get().stream().count());
    }

    @Test
    void findStudentsByGroup_shouldReturnZero_whenInputLongNumber() {
        assertEquals(0, studentDAOImpl.findStudentsByGroup(Integer.MAX_VALUE + 1).get().stream().count());
    }

    @Test
    void changePassword_shouldReturnOne_whenInputExistedStudentID() {
        assertEquals(1, studentDAOImpl.changePassword(1, "5555"));
    }

    @Test
    void changePassword_shouldReturnZero_whenInputNotExistedStudentID() {
        assertEquals(0, studentDAOImpl.changePassword(1000, "5555"));
    }

    @Test
    void changePassword_shouldReturnZero_whenInputNegativeID() {
        assertEquals(0, studentDAOImpl.changePassword(-1, "5555"));
    }
    @Test
    void updateStudent_shouldReturnOne_whenInputUpdatedStudentData() {
        student = new Student.StudentBuilder().setID(1).setFirstName("Marry").setLastName("poter").setGroupID(2).setIdCard("W2").build();
        assertEquals(1, studentDAOImpl.updateStudent(student));
    }

    @Test
    void updateStudent_shouldReturnZero_whenInputNotExistedStudentID() {
        student = new Student.StudentBuilder().setID(200).setFirstName("Marry").build();
        assertEquals(0, studentDAOImpl.updateStudent(student));
    }    
    @Test
    void getFirstNameMaxSize_shouldReturnColumnSize() {
        assertEquals(maxFirstNameSize, studentDAOImpl.getFirstNameMaxSize());       
    }   
    @Test
    void getLastNameMaxSize_shouldReturnColumnSize() {
        assertEquals(maxLastNameSize, studentDAOImpl.getLastNameMaxSize());       
    } 
    @Test
    void getIdCardMaxSize_shouldReturnColumnSize() {
        assertEquals(maxIdCardSize, studentDAOImpl.getIdCardMaxSize());       
    } 
    @Test
    void getPasswordMaxSize_shouldReturnColumnSize() {
        assertEquals(maxPasswordSize, studentDAOImpl.getPasswordMaxSize());       
    } 
}
