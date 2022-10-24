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

import ua.foxminded.university.dao.TeacherDAO;
import ua.foxminded.university.dao.exceptions.UniqueConstraintViolationException;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.User;
import ua.foxminded.university.service.exception.UserNotFoundException;

@ExtendWith(SpringExtension.class)
class TeacherServiceImplUnitTest {

    @Mock
    private TeacherDAO teacherDao;
    @InjectMocks
    private TeacherServiceImpl teacherService;

    private Teacher teacher;
    private User user;

    @BeforeEach
    void setup() {
        user = User.builder().id(1).firstName("name").lastName("surname").username("username").build();
        teacher = Teacher.builder().id(1).departmentId(1).position("position").user(user).build();
    }

    @Test
    void addTeacher_shouldThrowUniqueConstraintViolationException_whenInputNotUniqueName() {
        given(teacherDao.findByUserUsername(teacher.getUser().getUsername())).willReturn(teacher);
        assertThrows(UniqueConstraintViolationException.class, () -> teacherService.addTeacher(teacher));
        verify(teacherDao, times(0)).save(any(Teacher.class));
    }

    @Test
    void updateTeacher() {
        String newUsername = "newUsername";
        String newFirstName = "newFirstName";
        given(teacherDao.save(teacher)).willReturn(teacher);
        teacher.getUser().setUsername(newUsername);
        teacher.getUser().setFirstName(newFirstName);
        Teacher updatedTeacher = teacherService.updateTeacher(teacher);

        assertEquals(updatedTeacher.getUser().getUsername(), newUsername);
        assertEquals(updatedTeacher.getUser().getFirstName(), newFirstName);
    }

    @Test
    void updateTeacher_shouldThrowUniqueConstraintViolationException_whenInputNotUniqueName() {
        given(teacherDao.findByUserUsername(teacher.getUser().getUsername())).willReturn(teacher);
        assertThrows(UniqueConstraintViolationException.class, () -> teacherService.updateTeacher(teacher));
        verify(teacherDao, times(0)).save(any(Teacher.class));
    }

    @Test
    void deleteTeacher_whenDeleteTeacher() {
        int teacherId = 1;
        willDoNothing().given(teacherDao).deleteById(teacherId);
        teacherService.deleteTeacher(teacherId);
        verify(teacherDao, times(1)).deleteById(teacherId);
    }

    @Test
    void deleteTeacher_shouldReturnTrue_whenDeleteTeacher() {
        int teacherId = 1;
        willDoNothing().given(teacherDao).deleteById(teacherId);
        given(teacherDao.existsById(teacherId)).willReturn(false);
        assertTrue(teacherService.deleteTeacher(teacherId));
    }

    @Test
    void findAllTeachers_shouldReturnListOfTeachers_whenGetAllTeacher() {
        User user2 = User.builder().id(2).firstName("name2").lastName("surname2").username("username2").build();
        Teacher teacher2 = Teacher.builder().id(2).departmentId(1).position("position").user(user2).build();
        List<Teacher> teachers = Stream.of(teacher, teacher2).collect(Collectors.toList());
        given(teacherDao.findAll()).willReturn(teachers);

        List<Teacher> teacherList = teacherService.findAllTeachers();
        assertNotNull(teacherList);
        assertEquals(teacherList.size(), teachers.size());
    }

    @Test
    void findAllTeachers_shouldReturnEmptyList_whenGetAllTeacher() {
        given(teacherDao.findAll()).willReturn(Collections.emptyList());

        List<Teacher> teachersList = teacherService.findAllTeachers();
        assertEquals(new ArrayList<Teacher>(), teachersList);
        assertEquals(0, teachersList.size());
    }

    @Test
    void findById_shouldReturnTeacher_whenInputExistedTeacherId() {
        given(teacherDao.findById(1)).willReturn(Optional.of(teacher));
        Teacher savedTeacher = teacherService.findById(teacher.getId());
        assertNotNull(savedTeacher);
        assertEquals(teacher, savedTeacher);
    }

    @Test
    void findById_shouldThrowIllegalArgumentException_whenInputNotExistedTeacherId() {
        given(teacherDao.findById(1)).willReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> teacherService.findById(1));
    }

    @Test
    void findByUsername_shouldReturnTeacher_whenInputExistedUsername() {
        String existedUsername = "username";
        given(teacherDao.findByUserUsername(existedUsername)).willReturn(teacher);
        assertNotNull(teacher);
        assertEquals(teacher, teacherService.findByUsername(existedUsername));
    }

    @Test
    void findByUsername_shouldThrowUserNotFoundException_whenInputNotExistedUsername() {
        String notExistedUsername = "none";
        given(teacherDao.findByUserUsername(notExistedUsername)).willReturn(null);
        assertThrows(UserNotFoundException.class, () -> teacherService.findByUsername(notExistedUsername));
    }
}
