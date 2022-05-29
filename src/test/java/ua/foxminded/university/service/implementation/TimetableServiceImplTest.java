package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.exception.ServiceException;
import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.DayTimetable;
import ua.foxminded.university.service.pojo.Group;
import ua.foxminded.university.service.pojo.Lesson;
import ua.foxminded.university.service.pojo.LessonTimePeriod;
import ua.foxminded.university.service.pojo.Student;
import ua.foxminded.university.service.pojo.Teacher;

@TestInstance(Lifecycle.PER_CLASS)
class TimetableServiceImplTest {
    private TimetableServiceImpl timetableServiceImpl;
    private AnnotationConfigApplicationContext context;
    private Day day;
    private Lesson lesson;
    private Group group;
    private DayTimetable dayTimetable;

    @BeforeAll
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        timetableServiceImpl = context.getBean("timetableServiceImpl", TimetableServiceImpl.class);
        day = new Day();
        day.setLessonTimePeriod(LessonTimePeriod.lesson1.getTimePeriod());
    }
    
    @Test
    void scheduleTimetable_shouldThrowServiceException_whenInputTimetableWithHoliday() {
        day.setDateOne(LocalDate.of(2022, 01, 01));
        lesson = new Lesson.LessonBuilder().setID(5).build();
        group = new Group.GroupBuilder().setID(1).build();
        dayTimetable = new DayTimetable.TimetableBuilder().setDay(day).setLesson(lesson).setGroup(group).build();

        assertThrows(ServiceException.class, () -> timetableServiceImpl.scheduleTimetable(dayTimetable));
    }    
    
    @Test
    void scheduleTimetable_shouldReturnResult_whenInputTimetableWithWeekDayNotHoliday() throws ServiceException, DAOException {
        day.setDateOne(LocalDate.of(2022, 01, 03));
        lesson = new Lesson.LessonBuilder().setID(4).build();
        group = new Group.GroupBuilder().setID(2).build();
        dayTimetable = new DayTimetable.TimetableBuilder().setDay(day).setLesson(lesson).setGroup(group).build();       
        assertEquals(1, timetableServiceImpl.scheduleTimetable(dayTimetable));
    }

    @Test
    void scheduleTimetable_shouldThrowServiceException_whenInputTimetableWithWeekend() {
        day.setDateOne(LocalDate.of(2022, 05, 22));
        lesson = new Lesson.LessonBuilder().setID(5).build();
        group = new Group.GroupBuilder().setID(1).build();
        dayTimetable = new DayTimetable.TimetableBuilder().setDay(day).setLesson(lesson).setGroup(group).build();

        assertThrows(ServiceException.class, () -> timetableServiceImpl.scheduleTimetable(dayTimetable));
    }
}
