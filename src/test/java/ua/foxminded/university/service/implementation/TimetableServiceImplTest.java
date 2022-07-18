package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.config.HibernateConfigTest;
import ua.foxminded.university.service.TimetableService;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Room;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;
import ua.foxminded.university.service.exception.ServiceException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateConfigTest.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
class TimetableServiceImplTest {
    @Autowired
    private TimetableService timetableService;

    private Lesson lesson;
    private Group group;
    private Timetable timetable;
    private Teacher teacher;
    private Room room;

    @BeforeAll
    void init() {
        new EmbeddedDatabaseBuilder().setName("test").setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:tablesTest.sql").build();
        lesson = new Lesson.LessonBuilder().setID(1).build();
        group = new Group.GroupBuilder().setId(1).build();
        teacher = new Teacher.TeacherBuidler().setID(1).build();
        room = new Room();
        room.setNumber(101);
    }

    @Test
    void scheduleTimetable_shouldThrowServiceException_whenInputTimetableWithHoliday() {
        lesson = new Lesson.LessonBuilder().setID(5).build();
        group = new Group.GroupBuilder().setId(1).build();
        timetable = new Timetable.TimetableBuilder().setDate(LocalDate.of(2022, 01, 01))
                .setLessonTimePeriod("08:00 - 09:20").setLesson(lesson).setGroup(group).setTeacher(teacher)
                .setRoom(room).build();

        assertThrows(ServiceException.class, () -> timetableService.scheduleTimetable(timetable));
    }

    @Test
    void scheduleTimetable_shouldThrowServiceException_whenInputTimetableWithWeekDay() {
        timetable = new Timetable.TimetableBuilder().setDate(LocalDate.of(2022, 07, 10))
                .setLessonTimePeriod("08:00 - 09:20").setLesson(lesson).setGroup(group).setTeacher(teacher)
                .setRoom(room).build();
        assertThrows(ServiceException.class, () -> timetableService.scheduleTimetable(timetable));
    }
}
