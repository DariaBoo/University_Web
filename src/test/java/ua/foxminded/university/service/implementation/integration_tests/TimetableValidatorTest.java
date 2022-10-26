package ua.foxminded.university.service.implementation.integration_tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.RoomDAO;
import ua.foxminded.university.service.LessonTimePeriod;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Holiday;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Room;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;
import ua.foxminded.university.service.exception.UniqueConstraintViolationException;
import ua.foxminded.university.service.implementation.GroupServiceImpl;
import ua.foxminded.university.service.implementation.HolidayServiceImpl;
import ua.foxminded.university.service.implementation.LessonServiceImpl;
import ua.foxminded.university.service.implementation.TeacherServiceImpl;
import ua.foxminded.university.service.implementation.TimetableServiceImpl;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/timetable.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class TimetableValidatorTest {

    @Autowired
    private TimetableServiceImpl timetableService;
    @Autowired
    private GroupServiceImpl groupService;
    @Autowired
    private LessonServiceImpl lessonService;
    @Autowired
    private TeacherServiceImpl teacherService;
    @Autowired
    private RoomDAO roomDao;
    @Autowired
    private HolidayServiceImpl holidayService;

    private Timetable timetable;
    private Timetable scheduledTimetable;
    private Group group;
    private Lesson lesson;
    private Teacher teacher;
    private Room room;
    private Group scheduledGroup;
    private Lesson scheduledLesson;
    private Teacher scheduledTeacher;
    private Room scheduledRoom;
    private String lessonTimePeriod;
    private LocalDate date;

    @BeforeEach
    void setup() {
        lessonTimePeriod = LessonTimePeriod.lesson1.getTimePeriod();
        date = LocalDate.of(2022, 10, 25);
        group = groupService.findById(1);
        lesson = lessonService.findById(1);
        teacher = teacherService.findById(1);
        room = roomDao.findById(101).get();
        scheduledGroup = groupService.findById(2);
        scheduledLesson = lessonService.findById(2);
        scheduledTeacher = teacherService.findById(2);
        scheduledRoom = roomDao.findById(102).get();
        scheduledTimetable = Timetable.builder().timetableId(1).date(date).lessonTimePeriod(lessonTimePeriod)
                .group(scheduledGroup).lesson(scheduledLesson).teacher(scheduledTeacher).room(scheduledRoom).build();
    }

    @Test
    void scheduleTimetable_shouldReturnSuccessMessage_whenInputValidTimetable() {
        timetable = Timetable.builder().timetableId(1).date(date).lessonTimePeriod(lessonTimePeriod).group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        String result = "Timetable was scheduled successfully!";
        assertEquals(result, timetableService.scheduleTimetable(timetable));
    }

    @Test
    void scheduleTimetable_shouldThrowUniqueConstraintViolationException_whenInputScheduledGroup() {
        timetableService.scheduleTimetable(scheduledTimetable);
        timetable = Timetable.builder().timetableId(2).date(scheduledTimetable.getDate())
                .lessonTimePeriod(scheduledTimetable.getLessonTimePeriod()).group(scheduledTimetable.getGroup())
                .lesson(lesson).teacher(teacher).room(room).build();
        Exception ex = assertThrows(UniqueConstraintViolationException.class, () -> timetableService.scheduleTimetable(timetable)); 
        String errorMessage = "Group with id: " + timetable.getGroup().getId() + " is already scheduled (date:"
                + timetable.getDate() + ", time:" + timetable.getLessonTimePeriod() + ")!";
        assertTrue(ex.getLocalizedMessage().contains(errorMessage));
    }
    
    @Test
    void scheduleTimetable_shouldThrowUniqueConstraintViolationException_whenInputScheduledRoom() {
        timetableService.scheduleTimetable(scheduledTimetable);
        timetable = Timetable.builder().timetableId(2).date(scheduledTimetable.getDate())
                .lessonTimePeriod(scheduledTimetable.getLessonTimePeriod()).group(group)
                .lesson(lesson).teacher(teacher).room(scheduledTimetable.getRoom()).build();
        Exception ex =  assertThrows(UniqueConstraintViolationException.class, () -> timetableService.scheduleTimetable(timetable));
        String errorMessage = "Room number: " + timetable.getRoom().getNumber() + " is already scheduled (" + timetable.getDate()
        + ", " + timetable.getLessonTimePeriod() + ")!";
        assertTrue(ex.getLocalizedMessage().contains(errorMessage));
    }
    
    @Test
    void scheduleTimetable_shouldThrowUniqueConstraintViolationException_whenInputScheduledTeacher() {
        timetableService.scheduleTimetable(scheduledTimetable);
        timetable = Timetable.builder().timetableId(2).date(scheduledTimetable.getDate())
                .lessonTimePeriod(scheduledTimetable.getLessonTimePeriod()).group(group)
                .lesson(lesson).teacher(scheduledTimetable.getTeacher()).room(room).build();
        Exception ex = assertThrows(UniqueConstraintViolationException.class, () -> timetableService.scheduleTimetable(timetable));
        String errorMessage = "Teacher with id: " + timetable.getTeacher().getId() + " is already scheduled ("
                + timetable.getDate() + ", " + timetable.getLessonTimePeriod() + ")!";
        assertTrue(ex.getLocalizedMessage().contains(errorMessage));
    }

    @Test
    void scheduleTimetable_shouldReturnErrorMessage_whenInputSundayDate() {
        timetable = Timetable.builder().timetableId(1).date(LocalDate.of(2022, 10, 23))
                .lessonTimePeriod(lessonTimePeriod).group(group).lesson(lesson).teacher(teacher).room(room).build();
        String result = "2022-10-23 is a weekend. Can't schedule timetable!";
        assertEquals(result, timetableService.scheduleTimetable(timetable));
    }

    @Test
    void scheduleTimetable_shouldReturnErrorMessage_whenInputSaturdayDate() {
        timetable = Timetable.builder().timetableId(1).date(LocalDate.of(2022, 10, 22))
                .lessonTimePeriod(lessonTimePeriod).group(group).lesson(lesson).teacher(teacher).room(room).build();
        String result = "2022-10-22 is a weekend. Can't schedule timetable!";
        assertEquals(result, timetableService.scheduleTimetable(timetable));
    }

    @Test
    void scheduleTimetable_shouldReturnErrorMessage_whenInputHolidayDate() {
        LocalDate holidate = LocalDate.of(2022, 10, 24);
        Holiday holiday = Holiday.builder().id(1).name("holiday").date(holidate).build();
        holidayService.addHoliday(holiday);
        timetable = Timetable.builder().timetableId(1).date(holidate).lessonTimePeriod(lessonTimePeriod).group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        String result = holidate + " is a holiday. Can't schedule timetable!";
        assertEquals(result, timetableService.scheduleTimetable(timetable));
    }

    @Test
    void scheduleTimetable_shouldReturnErrorMessage_whenInputIncorrectTeacher() {
        Teacher teacher = Teacher.builder().id(0).build();
        timetable = Timetable.builder().timetableId(1).date(date).lessonTimePeriod(lessonTimePeriod).group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        String result = "Teacher is missing";
        assertEquals(result, timetableService.scheduleTimetable(timetable));
    }
}
