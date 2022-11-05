package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.dao.TeacherDAO;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.User;
import ua.foxminded.university.service.exception.EntityConstraintViolationException;
import ua.foxminded.university.service.exception.UserNotFoundException;

@ExtendWith(SpringExtension.class)
class TeacherServiceImplUnitTest {

    @Mock
    private TeacherDAO teacherDao;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private LessonDAO lessonDao;
    @InjectMocks
    private TeacherServiceImpl teacherService;
    @Spy
    private TeacherServiceImpl spyTeacherService;

    private Teacher teacher;
    private Lesson lesson;
    private User user;
    private Day day = new Day();
    private LocalDate date = LocalDate.of(2022, 10, 25);

    @BeforeEach
    void setup() {
        day.setDateOne(date);
        day.setDateTwo(date);
        List<Day> absentPeriod = Stream.of(day).collect(Collectors.toList());
        user = User.builder().id(1).firstName("name").lastName("surname").username("username").build();
        teacher = Teacher.builder().id(1).departmentId(1).position("position").user(user)
                .lessons(new ArrayList<Lesson>()).absentPeriod(absentPeriod).build();
        lesson = Lesson.builder().id(1).name("lesson").description("description").build();
    }

    @Test
    void addTeacher_shouldThrowUniqueConstraintViolationException_whenInputNotUniqueName() {
        given(teacherDao.findByUserUsername(teacher.getUser().getUsername())).willReturn(teacher);
        assertThrows(EntityConstraintViolationException.class, () -> teacherService.addTeacher(teacher));
        verify(teacherDao, times(0)).save(any(Teacher.class));
    }

    @Test
    void updateTeacher() {
        String newUsername = "newUsername";
        String newFirstName = "newFirstName";
        given(teacherDao.save(teacher)).willReturn(teacher);
        given(teacherDao.existsById(teacher.getId())).willReturn(true);
        teacher.getUser().setUsername(newUsername);
        teacher.getUser().setFirstName(newFirstName);
        Teacher updatedTeacher = teacherService.updateTeacher(teacher);
        assertEquals(updatedTeacher.getUser().getUsername(), newUsername);
        assertEquals(updatedTeacher.getUser().getFirstName(), newFirstName);
    }

    @Test
    void deleteTeacher_whenDeleteTeacher() {
        int teacherId = 1;
        willDoNothing().given(teacherDao).deleteById(teacherId);
        given(teacherDao.existsById(teacherId)).willReturn(true);
        teacherService.deleteTeacher(teacherId);
        verify(teacherDao, times(1)).deleteById(teacherId);
    }

    @Test
    void deleteTeacher_shouldThrowEntityNotFoundException_whenDeleteTeacher() {
        int teacherId = 1;
        willDoNothing().given(teacherDao).deleteById(teacherId);
        given(teacherDao.existsById(teacherId)).willReturn(false);
        assertThrows(EntityNotFoundException.class, () -> teacherService.deleteTeacher(teacherId));
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

    @Test
    void checkIsAbsent_shouldReturnTrue_whenTeacherIsAbsent() {
        given(teacherDao.findById(any(Integer.class))).willReturn(Optional.of(teacher));
        doReturn(teacher).when(spyTeacherService).findById(teacher.getId());
        assertTrue(teacherService.checkIsAbsent(date, teacher.getId()));
    }

    @Test
    void checkIsAbsent_shouldReturnFalse_whenTeacherIsNotAbsent() {
        given(teacherDao.findById(any(Integer.class))).willReturn(Optional.of(teacher));
        doReturn(teacher).when(spyTeacherService).findById(teacher.getId());
        assertFalse(teacherService.checkIsAbsent(date.plusDays(1), teacher.getId()));
    }

    @Test
    void changePassword() {
        given(teacherDao.findById(any(Integer.class))).willReturn(Optional.of(teacher));
        doReturn(teacher).when(spyTeacherService).findById(teacher.getId());
        given(passwordEncoder.encode(any(String.class)))
                .willReturn("$2a$12$ImbjKUvhChTLAL6K0gc8w.Lc51/FLhSktzw/9IViBjTdaHB7Yq5CO");
        teacherService.changePassword(teacher.getId(), "newPassword");
        verify(teacherDao, times(1)).save(any(Teacher.class));
    }

    @Test
    void assignLessonToTeacher_shouldReturnTrue_whenInputExistedData() {
        given(teacherDao.findById(any(Integer.class))).willReturn(Optional.of(teacher));
        given(lessonDao.findById(any(Integer.class))).willReturn(Optional.of(lesson));
        assertTrue(teacherService.assignLessonToTeacher(lesson.getId(), teacher.getId()));
        verify(teacherDao, times(1)).save(any(Teacher.class));
    }

    @Test
    void assignLessonToTeacher_shouldReturnFalse_whenInputNotExistedData() {
        given(teacherDao.findById(any(Integer.class))).willReturn(Optional.empty());
        given(lessonDao.findById(any(Integer.class))).willReturn(Optional.of(lesson));
        assertFalse(teacherService.assignLessonToTeacher(lesson.getId(), teacher.getId()));
        verify(teacherDao, times(0)).save(any(Teacher.class));
    }

    @Test
    void deleteLessonFromTeacher_shouldReturnTrue_whenInputExistedData() {
        given(teacherDao.findById(any(Integer.class))).willReturn(Optional.of(teacher));
        given(lessonDao.findById(any(Integer.class))).willReturn(Optional.of(lesson));
        assertTrue(teacherService.deleteLessonFromTeacher(lesson.getId(), teacher.getId()));
        verify(teacherDao, times(1)).save(any(Teacher.class));
    }

    @Test
    void deleteLessonFromTeacher_shouldReturnFalse_whenInputNotExistedData() {
        given(teacherDao.findById(any(Integer.class))).willReturn(Optional.empty());
        given(lessonDao.findById(any(Integer.class))).willReturn(Optional.of(lesson));
        assertFalse(teacherService.deleteLessonFromTeacher(lesson.getId(), teacher.getId()));
        verify(teacherDao, times(0)).save(any(Teacher.class));
    }

    @Test
    void setTeacherAbsent_shouldReturnTrue_whenInputExistedData() {
        given(teacherDao.findById(any(Integer.class))).willReturn(Optional.of(teacher));
        assertTrue(teacherService.setTeacherAbsent(teacher.getId(), day));
        verify(teacherDao, times(1)).save(any(Teacher.class));
    }

    @Test
    void setTeacherAbsent_shouldReturnFalse_whenInputNotExistedData() {
        given(teacherDao.findById(any(Integer.class))).willReturn(Optional.empty());
        assertFalse(teacherService.setTeacherAbsent(teacher.getId(), day));
        verify(teacherDao, times(0)).save(any(Teacher.class));
    }

    @Test
    void deleteTeacherAbsent_shouldReturnTrue_whenInputExistedData() {
        given(teacherDao.findById(any(Integer.class))).willReturn(Optional.of(teacher));
        assertTrue(teacherService.deleteTeacherAbsent(teacher.getId(), day));
        verify(teacherDao, times(1)).save(any(Teacher.class));
    }

    @Test
    void deleteTeacherAbsent_shouldReturnFalse_whenInputNotExistedData() {
        given(teacherDao.findById(any(Integer.class))).willReturn(Optional.empty());
        assertFalse(teacherService.deleteTeacherAbsent(teacher.getId(), day));
        verify(teacherDao, times(0)).save(any(Teacher.class));
    }
}
