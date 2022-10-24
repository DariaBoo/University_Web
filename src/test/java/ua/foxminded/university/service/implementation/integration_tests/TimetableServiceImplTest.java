package ua.foxminded.university.service.implementation.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.RoomDAO;
import ua.foxminded.university.service.LessonTimePeriod;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Room;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;
import ua.foxminded.university.service.implementation.GroupServiceImpl;
import ua.foxminded.university.service.implementation.LessonServiceImpl;
import ua.foxminded.university.service.implementation.TeacherServiceImpl;
import ua.foxminded.university.service.implementation.TimetableServiceImpl;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/timetable.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class TimetableServiceImplTest {

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

    private Timetable timetable;
    private Group group;
    private Lesson lesson;
    private Teacher teacher;
    private Room room;

    @BeforeEach
    void setup() {
        group = groupService.findById(1);
        lesson = lessonService.findById(1);
        teacher = teacherService.findById(1);
        room = roomDao.findById(101).get();
    }

    @Test
    void scheduleTimetable_shouldReturnSuccessMessage_whenInputValidTimetable() {
        timetable = Timetable.builder().timetableId(1).date(LocalDate.of(2022, 10, 21))
                .lessonTimePeriod(LessonTimePeriod.lesson1.toString()).group(group).lesson(lesson).teacher(teacher)
                .room(room).build();
        String result = "Timetable was scheduled successfully!";
        assertEquals(result, timetableService.scheduleTimetable(timetable));
    }

    @ParameterizedTest
    @NullSource
    void scheduleTimetable_shouldReturnErrorMessage_whenInputIncorrectGroup(Group group) {
        timetable = Timetable.builder().timetableId(1).date(LocalDate.of(2022, 10, 21))
                .lessonTimePeriod(LessonTimePeriod.lesson1.toString()).group(group).lesson(lesson).teacher(teacher)
                .room(room).build();
        String result = "Group is missing";
        assertEquals(result, timetableService.scheduleTimetable(timetable));
    }

    @ParameterizedTest
    @NullSource
    void scheduleTimetable_shouldReturnErrorMessage_whenInputIncorrectLesson(Lesson lesson) {
        timetable = Timetable.builder().timetableId(1).date(LocalDate.of(2022, 10, 21))
                .lessonTimePeriod(LessonTimePeriod.lesson1.toString()).group(group).lesson(lesson).teacher(teacher)
                .room(room).build();
        String result = "Lesson is missing";
        assertEquals(result, timetableService.scheduleTimetable(timetable));
    }

    @ParameterizedTest
    @NullSource
    void scheduleTimetable_shouldReturnErrorMessage_whenInputIncorrectRoom(Room room) {
        timetable = Timetable.builder().timetableId(1).date(LocalDate.of(2022, 10, 21))
                .lessonTimePeriod(LessonTimePeriod.lesson1.toString()).group(group).lesson(lesson).teacher(teacher)
                .room(room).build();
        String result = "Room is missing";
        assertEquals(result, timetableService.scheduleTimetable(timetable));
    }

    @ParameterizedTest
    @NullSource
    void scheduleTimetable_shouldReturnErrorMessage_whenInputIncorrectDate(LocalDate date) {
        timetable = Timetable.builder().timetableId(1).date(date).lessonTimePeriod(LessonTimePeriod.lesson1.toString())
                .group(group).lesson(lesson).teacher(teacher).room(room).build();
        String result = "Date is missing";
        assertEquals(result, timetableService.scheduleTimetable(timetable));
    }

    @Test
    void scheduleTimetable_shouldReturnErrorMessage_whenInputWeekendDate() {
        timetable = Timetable.builder().timetableId(1).date(LocalDate.of(2022, 10, 23))
                .lessonTimePeriod(LessonTimePeriod.lesson1.toString()).group(group).lesson(lesson).teacher(teacher)
                .room(room).build();
        String result = "2022-10-23 is a weekend. Can't schedule timetable!";
        assertEquals(result, timetableService.scheduleTimetable(timetable));
    }

    @Test
    void scheduleTimetable_shouldReturnErrorMessage_whenInputHolidayDate() {
        timetable = Timetable.builder().timetableId(1).date(LocalDate.of(2022, 10, 23))
                .lessonTimePeriod(LessonTimePeriod.lesson1.toString()).group(group).lesson(lesson).teacher(teacher)
                .room(room).build();
        String result = "2022-10-23 is a weekend. Can't schedule timetable!";
        assertEquals(result, timetableService.scheduleTimetable(timetable));
    }
}
