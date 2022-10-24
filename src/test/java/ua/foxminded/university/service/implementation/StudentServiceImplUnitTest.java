package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.dao.exceptions.UniqueConstraintViolationException;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.User;
import ua.foxminded.university.service.exception.UserNotFoundException;

@ExtendWith(SpringExtension.class)
class StudentServiceImplUnitTest {

    @Mock
    private StudentDAO studentDao;
    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private User user;
    private Group group;

    @BeforeEach
    void setup() {
        user = User.builder().id(1).firstName("name").lastName("surname").username("username").build();
        group = Group.builder().id(1).name("name").departmentId(1).build();
        student = Student.builder().id(1).user(user).group(group).idCard("AA-1").build();
    }

    @Test
    void addStudent_shouldThrowUniqueConstraintViolationException_whenInputNotUniqueName() {
        given(studentDao.findByUserUsername(student.getUser().getUsername())).willReturn(student);
        assertThrows(UniqueConstraintViolationException.class, () -> studentService.addStudent(student));
        verify(studentDao, times(0)).save(any(Student.class));
    }

    @Test
    void updateStudent() {
        String newUsername = "newUsername";
        String newFirstName = "newFirstName";
        given(studentDao.save(student)).willReturn(student);
        student.getUser().setUsername(newUsername);
        student.getUser().setFirstName(newFirstName);
        Student updatedStudent = studentService.updateStudent(student);

        assertEquals(updatedStudent.getUser().getUsername(), newUsername);
        assertEquals(updatedStudent.getUser().getFirstName(), newFirstName);
    }

    @Test
    void updateStudent_shouldThrowUniqueConstraintViolationException_whenInputNotUniqueName() {
        given(studentDao.findByUserUsername(student.getUser().getUsername())).willReturn(student);
        assertThrows(UniqueConstraintViolationException.class, () -> studentService.updateStudent(student));
        verify(studentDao, times(0)).save(any(Student.class));
    }

    @Test
    void deleteStudent_whenDeleteStudent() {
        int studentId = 1;
        willDoNothing().given(studentDao).deleteById(studentId);
        studentService.deleteStudent(studentId);
        verify(studentDao, times(1)).deleteById(studentId);
    }

    @Test
    void deleteStudent_shouldReturnTrue_whenDeleteStudent() {
        int studentId = 1;
        willDoNothing().given(studentDao).deleteById(studentId);
        given(studentDao.existsById(studentId)).willReturn(false);
        assertTrue(studentService.deleteStudent(studentId));
    }

    @Test
    void findAllStudents_shouldReturnListOfStudents_whenGetAllStudent() {
        User user2 = User.builder().id(2).firstName("name2").lastName("surname2").username("username2").build();
        Student student2 = Student.builder().id(2).user(user2).group(group).idCard("AA-2").build();
        List<Student> students = Stream.of(student, student2).collect(Collectors.toList());
        given(studentDao.findAll()).willReturn(students);

        List<Student> studentList = studentService.findAllStudents();
        assertNotNull(studentList);
        assertEquals(studentList.size(), students.size());
    }

    @Test
    void findAllStudents_shouldReturnEmptyList_whenGetAllStudent() {
        given(studentDao.findAll()).willReturn(Collections.emptyList());

        List<Student> studentList = studentService.findAllStudents();
        assertEquals(new ArrayList<Student>(), studentList);
        assertEquals(0, studentList.size());
    }

    @Test
    void findById_shouldReturnStudent_whenInputExistedStudentId() {
        given(studentDao.findById(1)).willReturn(Optional.of(student));
        Student savedStudent = studentService.findById(student.getId());
        assertNotNull(savedStudent);
        assertEquals(student, savedStudent);
    }

    @Test
    void findById_shouldThrowIllegalArgumentException_whenInputNotExistedStudentId() {
        given(studentDao.findById(1)).willReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> studentService.findById(1));
    }

    @Test
    void findByUsername_shouldReturnStudent_whenInputExistedUsername() {
        String existedUsername = "username";
        given(studentDao.findByUserUsername(existedUsername)).willReturn(student);
        assertNotNull(student);
        assertEquals(student, studentService.findByUsername(existedUsername));
    }

    @Test
    void findByUsername_shouldThrowUserNotFoundException_whenInputNotExistedUsername() {
        String notExistedUsername = "none";
        given(studentDao.findByUserUsername(notExistedUsername)).willReturn(null);
        assertThrows(UserNotFoundException.class, () -> studentService.findByUsername(notExistedUsername));
    }
}
