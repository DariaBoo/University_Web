package ua.foxminded.university.service.implementation.integration_tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.User;
import ua.foxminded.university.service.exception.EntityConstraintViolationException;
import ua.foxminded.university.service.exception.UserNotFoundException;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/teachers.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class TeacherServiceImplTest {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private UserDAO userDao;
    private Teacher teacher;
    private Teacher teacher2;
    private Lesson lesson;
    private User user;
    private User user2;
    private Day day;

    @BeforeEach
    void setup() {
        user = User.builder().firstName("name").lastName("surname").username("username").password("password").build();
        user2 = User.builder().firstName("name2").lastName("surname2").username("username2").password("password")
                .build();
        userDao.save(user);
        userDao.save(user2);

        teacher = Teacher.builder().user(user).departmentId(1).position("position").build();
        teacher2 = Teacher.builder().user(user2).departmentId(1).position("position2").build();
        lesson = Lesson.builder().name("lesson").description("description").build();
        day = new Day();
        day.setDateOne(LocalDate.of(2022, 10, 31));
        day.setDateTwo(LocalDate.of(2022, 10, 31));
    }

    @Test
    void addTeacher_shouldReturnAddedTeacher_whenInputCorrectData() {
        assertEquals(teacher, teacherService.addTeacher(teacher));
    }

    @Test
    void addTeacher_shouldThrowUniqueConstraintViolationException_whenInputExistedUser() {
        teacherService.addTeacher(teacher);
        assertThrows(EntityConstraintViolationException.class, () -> teacherService.addTeacher(teacher));
    }

    @Test
    void updateTeacher_shouldReturnTeacher_whenInputCorrectData() {
        teacherService.addTeacher(teacher);
        String newPosition = "newPosition";
        teacher.setPosition(newPosition);
        assertEquals(teacher.getPosition(), teacherService.updateTeacher(teacher).getPosition());
    }

    @Test
    void updateTeacher_shouldThrowUniqueConstraintViolationException_whenInputInCorrectData() {
        assertEquals(new Teacher(), teacherService.updateTeacher(teacher));
    }

    @Test
    void deleteTeacher_shouldDeleteEntity() {
        teacherService.addTeacher(teacher);
        int size = teacherService.findAllTeachers().size();
        teacherService.deleteTeacher(teacher.getId());
        assertEquals(size - 1, teacherService.findAllTeachers().size());
    }

    @Test
    void deleteTeacher_shouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> teacherService.deleteTeacher(1));
    }

    @Test
    void changePassword() {
        int teacherId = teacherService.addTeacher(teacher).getId();
        String newPassword = "$2a$12$oLyWRUPG6zohk4buvDF3d.4VVJLtVeSBNR3fCNM1CFuRjKlLwbGGi";
        teacherService.changePassword(teacherId, newPassword);
        teacher.getUser().setPassword(newPassword);
        assertEquals(teacher, teacherService.findById(teacherId));
    }

    @Test
    void findById_shouldReturnTeacher_whenInputExistedId() {
        int teacherId = teacherService.addTeacher(teacher).getId();
        assertEquals(teacher, teacherService.findById(teacherId));
    }

    @Test
    void findById_shouldThrowIllegalArgumentException() {
        int notExistedId = teacherService.findAllTeachers().size();
        assertThrows(IllegalArgumentException.class, () -> teacherService.findById(notExistedId));
    }

    @Test
    void findAllTeachers_shouldReturnList() {
        teacherService.addTeacher(teacher);
        teacherService.addTeacher(teacher2);
        assertEquals(2, teacherService.findAllTeachers().size());
    }

    @Test
    void findByUsername_shouldReturnTeacher_whenInputExistedData() {
        teacherService.addTeacher(teacher);
        assertEquals(teacher, teacherService.findByUsername(teacher.getUser().getUsername()));
    }

    @Test
    void findByUsername_shouldThrowUserNotFoundException() {
        String notExistedUsername = "someUsername";
        assertThrows(UserNotFoundException.class, () -> teacherService.findByUsername(notExistedUsername));
    }

    @Test
    void assignLessonToTeacher_shouldReturnTrue_whenInputCorrectData() {
        teacherService.addTeacher(teacher);
        lessonService.addLesson(lesson);
        assertTrue(teacherService.assignLessonToTeacher(lesson.getId(), teacher.getId()));
    }

    @Test
    void assignLessonToTeacher_shouldReturnFalse_whenInputInCorrectData() {
        assertFalse(teacherService.assignLessonToTeacher(lesson.getId(), teacher.getId()));
    }

    @Test
    void deleteLessonFromTeacher_shouldReturnTrue_whenInputCorrectData() {
        teacherService.addTeacher(teacher);
        lessonService.addLesson(lesson);
        assertTrue(teacherService.deleteLessonFromTeacher(lesson.getId(), teacher.getId()));
    }

    @Test
    void deleteLessonFromTeacher_shouldReturnFalse_whenInputInCorrectData() {
        assertFalse(teacherService.deleteLessonFromTeacher(lesson.getId(), teacher.getId()));
    }

    @Test
    void setTeacherAbsent_shouldReturnTrue_whenInputCorrectData() {
        teacherService.addTeacher(teacher);
        assertTrue(teacherService.setTeacherAbsent(teacher.getId(), day));
    }

    @Test
    void setTeacherAbsent_shouldReturnFalse_whenInputInCorrectData() {
        assertFalse(teacherService.setTeacherAbsent(teacher.getId(), day));
    }

    @Test
    void deleteTeacherAbsent_shouldReturnTrue_whenInputCorrectData() {
        teacher.getAbsentPeriod().add(day);
        teacherService.addTeacher(teacher);
        assertTrue(teacherService.deleteTeacherAbsent(teacher.getId(), day));
    }

    @Test
    void deleteTeacherAbsent_shouldReturnFalse_whenInputInCorrectData() {
        assertFalse(teacherService.deleteTeacherAbsent(teacher.getId(), day));
    }

    @Test
    void checkIsAbsent_shouldReturnTrue_whenInputCorrectData() {
        teacher = Teacher.builder().user(user).departmentId(1).absentPeriod(Stream.of(day).collect(Collectors.toList()))
                .position("position").build();
        teacherService.addTeacher(teacher);
        assertTrue(teacherService.checkIsAbsent(day.getDateOne(), teacher.getId()));
    }

    @Test
    void checkIsAbsent_shouldReturnFalse_whenInputInCorrectData() {
        teacherService.addTeacher(teacher);
        assertFalse(teacherService.checkIsAbsent(day.getDateOne(), teacher.getId()));
    }
}
