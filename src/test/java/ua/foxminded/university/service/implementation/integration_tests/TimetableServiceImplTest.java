package ua.foxminded.university.service.implementation.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import ua.foxminded.university.dao.RoomDAO;
import ua.foxminded.university.dao.TimetableDAO;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.TimetableService;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Room;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;
import ua.foxminded.university.service.entities.User;
import ua.foxminded.university.service.exception.EntityConstraintViolationException;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/timetable.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class TimetableServiceImplTest {

    @Autowired
    private TimetableService timetableService;
    @Autowired
    private TimetableDAO timetableDao;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private RoomDAO roomDao;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserDAO userDao;
    private Teacher teacher;
    private Group group;
    private Student student;
    private Lesson lesson;
    private Room room;
    private Day day = new Day();
    private Day day2 = new Day();
    private User user;
    private Timetable timetable;
    private Timetable timetable2;
    private LocalDate absentTeacherDay = LocalDate.of(2022, 10, 03);
    private LocalDate existedTimetable = LocalDate.of(2022, 10, 31);
    private LocalDate notExistedTimetable = LocalDate.of(2022, 10, 28);

    @BeforeEach
    void setup() {
        day2.setDateTwo(absentTeacherDay);
        day2.setDateOne(absentTeacherDay);
        user = User.builder().username("username").firstName("name").lastName("surname").password("password").build();
        userDao.save(user);
        group = Group.builder().name("AA-00").departmentId(1).build();
        groupService.addGroup(group);
        room = new Room();
        room.setNumber(101);
        room.setCapacity(25);
        lesson = Lesson.builder().name("lesson").description("description").build();
        List<Day> absentPeriodList = new ArrayList<>();
        absentPeriodList.add(day2);
        teacher = Teacher.builder().user(user).position("position").departmentId(1).build();
        teacher.getAbsentPeriod().add(day2);
        teacherService.addTeacher(teacher);
        roomDao.save(room);
        lessonService.addLesson(lesson);
    }

    @Test
    void scheduleTimetable_shouldReturnSuccessMessage_whenInputCorrectData() {
        timetable = Timetable.builder().date(existedTimetable).lessonTimePeriod("08:00 - 09:20").group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        String successMessage = "Timetable was scheduled successfully!";
        assertEquals(successMessage, timetableService.scheduleTimetable(timetable));
    }

    @Test
    @Transactional
    void scheduleTimetable_shouldReturnErrorMessage_whenInputAbsentTeacher() {
        timetable = Timetable.builder().date(absentTeacherDay).lessonTimePeriod("08:00 - 09:20").teacher(teacher)
                .group(group).lesson(lesson).room(room).build();
        String message = "Teacher [id::1] is absent [date::2022-10-03].";
        assertEquals(message, timetableService.scheduleTimetable(timetable));
    }

    @Test
    void scheduleTimetable_shouldThrowEntityConstraintViolationException_whenInputBlankLessonTimePeriod() {
        timetable = Timetable.builder().date(absentTeacherDay).lessonTimePeriod("").teacher(teacher).group(group)
                .lesson(lesson).room(room).build();
        String errorMessage = "Lesson time period may not be null or empty";
        Exception exception = assertThrows(EntityConstraintViolationException.class,
                () -> timetableService.scheduleTimetable(timetable));
        assertTrue(exception.getLocalizedMessage().contains(errorMessage));
    }

    @Test
    @Transactional
    void scheduleTimetable_shouldThrowEntityConstraintViolationException_whenInputScheduledGroup() {
        timetable = Timetable.builder().date(existedTimetable).lessonTimePeriod("08:00 - 09:20").group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        timetableService.scheduleTimetable(timetable);
        Timetable timetable2 = Timetable.builder().date(existedTimetable).lessonTimePeriod("08:00 - 09:20").group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        String errorMessage = "Group with id: 1 is already scheduled (date:2022-10-31, time:08:00 - 09:20)!";
        Exception exception = assertThrows(EntityConstraintViolationException.class,
                () -> timetableService.scheduleTimetable(timetable2));
        assertTrue(exception.getLocalizedMessage().contains(errorMessage));
    }

    @Test
    void deleteTimetable_shouldDeleteTimetable_whenInputCorrectData() {
        timetable = Timetable.builder().date(existedTimetable).lessonTimePeriod("08:00 - 09:20").group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        timetableService.scheduleTimetable(timetable);
        timetableService.deleteTimetable(timetable.getTimetableId());
        assertEquals(0, timetableDao.findAll().size());
    }

    @Test
    void deleteTimetable_shouldReturnFalse_whenInputInCorrectData() {
        timetable = Timetable.builder().date(existedTimetable).lessonTimePeriod("08:00 - 09:20").group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        int timetableId = timetable.getTimetableId();
        assertThrows(EntityNotFoundException.class, () -> timetableService.deleteTimetable(timetableId));
    }

    @Test
    void getTeacherTimetable_shouldReturnList_whenInputCorrectData() {
        day.setDateTwo(existedTimetable);
        day.setDateOne(existedTimetable);
        timetable = Timetable.builder().date(existedTimetable).lessonTimePeriod("08:00 - 09:20").group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        timetable2 = Timetable.builder().date(existedTimetable).lessonTimePeriod("09:30 - 11:00").group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        List<Timetable> timetables = new ArrayList<Timetable>();
        timetables.add(timetable);
        timetables.add(timetable2);
        timetableService.scheduleTimetable(timetable);
        timetableService.scheduleTimetable(timetable2);
        assertEquals(timetables.size(), timetableService.getTeacherTimetable(day, teacher).size());
    }

    @Test
    void getTeacherTimetable_shouldReturnEmptyList_whenInputInCorrectData() {
        day.setDateTwo(notExistedTimetable);
        day.setDateOne(notExistedTimetable);
        assertEquals(new ArrayList<>(), timetableService.getTeacherTimetable(day, teacher));
    }

    @Test
    @Transactional
    void getStudentTimetable_shouldReturnList_whenInputCorrectData() {
        student = Student.builder().user(user).group(group).idCard("123").build();
        studentService.addStudent(student);
        day.setDateTwo(existedTimetable);
        day.setDateOne(existedTimetable);
        timetable = Timetable.builder().date(existedTimetable).lessonTimePeriod("08:00 - 09:20").group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        timetable2 = Timetable.builder().date(existedTimetable).lessonTimePeriod("09:30 - 11:00").group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        List<Timetable> timetables = new ArrayList<Timetable>();
        timetables.add(timetable);
        timetables.add(timetable2);
        timetableService.scheduleTimetable(timetable);
        timetableService.scheduleTimetable(timetable2);
        assertEquals(timetables.size(), timetableService.getStudentTimetable(day, student).size());
    }

    @Test
    @Transactional
    void getStudentTimetable_shouldReturnEmptyList_whenInputInCorrectData() {
        day.setDateTwo(notExistedTimetable);
        day.setDateOne(notExistedTimetable);
        student = Student.builder().user(user).group(group).idCard("123").build();
        studentService.addStudent(student);
        assertEquals(new ArrayList<>(), timetableService.getStudentTimetable(day, student));
    }

    @Test
    void showTimetable_shouldReturnList_whenInputCorrectData() {
        day.setDateOne(existedTimetable);
        day.setDateTwo(existedTimetable);
        timetable = Timetable.builder().date(existedTimetable).lessonTimePeriod("08:00 - 09:20").group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        timetable2 = Timetable.builder().date(existedTimetable).lessonTimePeriod("09:30 - 11:00").group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        List<Timetable> timetables = new ArrayList<Timetable>();
        timetables.add(timetable);
        timetables.add(timetable2);
        timetableService.scheduleTimetable(timetable);
        timetableService.scheduleTimetable(timetable2);
        assertEquals(timetables.size(), timetableService.showTimetable(day).size());
    }

    @Test
    void showTimetable_shouldReturnEmptyList_whenInputInCorrectData() {
        day.setDateTwo(notExistedTimetable);
        day.setDateOne(notExistedTimetable);
        assertEquals(new ArrayList<>(), timetableService.showTimetable(day));
    }
}
