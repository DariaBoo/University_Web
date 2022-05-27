package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.DayTimetable;
import ua.foxminded.university.service.pojo.Group;
import ua.foxminded.university.service.pojo.Lesson;
import ua.foxminded.university.service.pojo.LessonTimePeriod;
import ua.foxminded.university.service.pojo.Student;
import ua.foxminded.university.service.pojo.Teacher;
import ua.foxminded.university.service.pojo.User;

class TimetableDAOImplTest {
    private TimetableDAOImpl timetable;
    private TeacherDAOImpl teacherDAOImpl;
    private AnnotationConfigApplicationContext context;
    private Day day;
    private Lesson lesson;
    private Group group;
    private DayTimetable dayTimetable;
    private User user;

    @BeforeEach
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        timetable = context.getBean("timetable", TimetableDAOImpl.class);
        teacherDAOImpl = context.getBean("teacherDAOImpl", TeacherDAOImpl.class);
        day = new Day();
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setLessonTimePeriod(LessonTimePeriod.lesson1.getTimePeriod());
    }

    @Test
    void scheduleTimetable_shouldReturnCountOfAddedRows_whenInputCorrectData() throws DAOException {
        lesson = new Lesson.LessonBuilder().setID(5).build();
        group = new Group.GroupBuilder().setID(1).build();
        dayTimetable = new DayTimetable.TimetableBuilder().setDay(day).setLesson(lesson).setGroup(group).build();

        assertEquals(1, timetable.scheduleTimetable(dayTimetable));
    }

    @ParameterizedTest(name = "{index}. Input data: lessonID {0}, groupID {1}.")
    @CsvSource({"100,1", "1, 100" })
    void scheduleTimetable_shouldThrowDAOException_whenInputInCorrectData(int lessonID, int groupID) throws DAOException {
        lesson = new Lesson.LessonBuilder().setID(lessonID).build();
        group = new Group.GroupBuilder().setID(groupID).build();
        dayTimetable = new DayTimetable.TimetableBuilder().setDay(day).setLesson(lesson).setGroup(group).build();

        assertThrows(DAOException.class, () -> timetable.scheduleTimetable(dayTimetable));
    }
    
    @Test
    void scheduleTimetable_shouldThrowDAOException_whenInputScheduledLessonAndGroup() throws DAOException {
        lesson = new Lesson.LessonBuilder().setID(1).build();
        group = new Group.GroupBuilder().setID(1).build();
        dayTimetable = new DayTimetable.TimetableBuilder().setDay(day).setLesson(lesson).setGroup(group).build();

        assertThrows(DAOException.class, () -> timetable.scheduleTimetable(dayTimetable));
    }

    @Test
    void scheduleTimetable_shouldThrowDAOException_whenTeacherAbsent() throws DAOException {
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setDateTwo(LocalDate.of(2023, 04, 02));
        lesson = new Lesson.LessonBuilder().setID(1).build();
        group = new Group.GroupBuilder().setID(1).build();
        dayTimetable = new DayTimetable.TimetableBuilder().setDay(day).setLesson(lesson).setGroup(group).build();
        teacherDAOImpl.setTeahcerAbsent(1, day);
        teacherDAOImpl.setTeahcerAbsent(2, day);

        assertThrows(DAOException.class, () -> timetable.scheduleTimetable(dayTimetable));
    }

    @Test
    void deleteTimetable_shouldReturnCountOfDeletedRows_whenInputCorrectTimetableIDAndNotPastDate() {
        assertEquals(1, timetable.deleteTimetable(2));
    }

    @DisplayName("Result of the test depends on test date because it's impossible to delete past timetable")
    @Test
    void deleteTimetable_shouldReturnZero_whenInputCorrectTimetableIDAndPastDate() {
        assertEquals(0, timetable.deleteTimetable(1));
    }

    @Test
    void getDayTimetable_shouldReturnDayTimetable_whenTimetableScheduledForTheTeacherAndTheDay() {
        Lesson expectedLesson = new Lesson.LessonBuilder().setName("Alchemy").build();
        Teacher expectedTeacher = new Teacher.TeacherBuidler().setFirstName("Albus").setLastName("Dumbledore").build();
        DayTimetable expectedTimetable = new DayTimetable.TimetableBuilder().setDay(day).setLesson(expectedLesson)
                .setGroup(new Group.GroupBuilder().setName("CO-68").build()).setTeacher(expectedTeacher)
                .setRoomNumber(201).build();

        user = new Teacher.TeacherBuidler().setID(1).build();
        assertEquals(Optional.of(expectedTimetable), timetable.getDayTimetable(LocalDate.of(2023, 04, 01), user));
    }

    @ParameterizedTest(name = "{index}. Input data: teacher id {0}.")
    @ValueSource(ints = { 1, 100 })
    void getDayTimetable_shouldReturnOptionalEmpty_whenTeacherHasNotLessonsInThisDayOrTeacherIDIsIncorrect(
            int teacherID) {
        user = new Teacher.TeacherBuidler().setID(teacherID).build();
        assertEquals(Optional.empty(), timetable.getDayTimetable(LocalDate.of(2022, 05, 20), user));
    }

    @Test
    void getMonthTimetable_shouldReturnListOfDayTimetable_whenInputCorrectDateAndTeacherID() {
        day.setDateOne(LocalDate.of(2022, 04, 01));
        day.setDateTwo(LocalDate.of(2022, 04, 02));
        user = new Teacher.TeacherBuidler().setID(1).build();
        List<Optional<DayTimetable>> result = new ArrayList<Optional<DayTimetable>>();
        result.add(timetable.getDayTimetable(day.getDateOne(), user));
        result.add(timetable.getDayTimetable(day.getDateOne().plusDays(1), user));

        assertEquals(result, timetable.getMonthTimetable(day, user));
    }

    @Test
    void getMonthTimetable_shouldReturnEmptyList_whenInputInCorrectDateAndTeacherID() {
        day.setDateOne(LocalDate.of(2021, 04, 01));
        day.setDateTwo(LocalDate.of(2021, 04, 02));
        user = new Teacher.TeacherBuidler().setID(1).build();
        List<Optional<DayTimetable>> result = new ArrayList<>();
        result.add(Optional.empty());
        result.add(Optional.empty());
        assertEquals(result, timetable.getMonthTimetable(day, user));
    }

    @Test
    void getMonthTimetable_shouldReturnListOfDayTimetable_whenInputCorrectDateAndStudentID() {
        day.setDateOne(LocalDate.of(2022, 04, 01));
        day.setDateTwo(LocalDate.of(2022, 04, 02));
        user = new Student.StudentBuilder().setID(1).build();
        List<Optional<DayTimetable>> result = new ArrayList<Optional<DayTimetable>>();
        result.add(timetable.getDayTimetable(day.getDateOne(), user));
        result.add(timetable.getDayTimetable(day.getDateOne().plusDays(1), user));

        assertEquals(result, timetable.getMonthTimetable(day, user));
    }

    @Test
    void getMonthTimetable_shouldReturnEmptyList_whenInputInCorrectDateAndStudentID() {
        day.setDateOne(LocalDate.of(2021, 04, 01));
        day.setDateTwo(LocalDate.of(2021, 04, 02));
        user = new Student.StudentBuilder().setID(1).build();
        List<Optional<DayTimetable>> result = new ArrayList<>();
        result.add(Optional.empty());
        result.add(Optional.empty());
        assertEquals(result, timetable.getMonthTimetable(day, user));
    }
}
