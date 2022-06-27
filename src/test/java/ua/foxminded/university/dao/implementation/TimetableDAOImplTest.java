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
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.LessonTimePeriod;
import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.Timetable;
import ua.foxminded.university.service.pojo.Group;
import ua.foxminded.university.service.pojo.Lesson;
import ua.foxminded.university.service.pojo.Student;
import ua.foxminded.university.service.pojo.Teacher;
import ua.foxminded.university.service.pojo.User;

class TimetableDAOImplTest {
    private TimetableDAOImpl timetableDAOImpl;
    private TeacherDAOImpl teacherDAOImpl;
    private AnnotationConfigApplicationContext context;
    private Day day;
    private Lesson lesson;
    private Group group;
    private Timetable timetable;
    private User user;

    @BeforeEach
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        timetableDAOImpl = context.getBean("timetableDAOImpl", TimetableDAOImpl.class);
        teacherDAOImpl = context.getBean("teacherDAOImpl", TeacherDAOImpl.class);
        day = new Day();
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setLessonTimePeriod(LessonTimePeriod.lesson1.getTimePeriod());
    }

    @Test
    void exist() {
        assertNotNull(context);
    }
    @Test
    void scheduleTimetable_shouldReturnCountOfAddedRows_whenInputCorrectData() throws DAOException {
        lesson = new Lesson.LessonBuilder().setID(5).build();
        group = new Group.GroupBuilder().setID(1).build();
        timetable = new Timetable.TimetableBuilder().setDay(day).setLesson(lesson).setGroup(group).build();

        assertEquals(1, timetableDAOImpl.scheduleTimetable(timetable));
    }

    @ParameterizedTest(name = "{index}. Input data: lessonID {0}, groupID {1}.")
    @CsvSource({"100,1", "1, 100" })
    void scheduleTimetable_shouldThrowDAOException_whenInputInCorrectData(int lessonID, int groupID) throws DAOException {
        lesson = new Lesson.LessonBuilder().setID(lessonID).build();
        group = new Group.GroupBuilder().setID(groupID).build();
        timetable = new Timetable.TimetableBuilder().setDay(day).setLesson(lesson).setGroup(group).build();

        assertThrows(DAOException.class, () -> timetableDAOImpl.scheduleTimetable(timetable));
    }
    
    @Test
    void scheduleTimetable_shouldThrowDAOException_whenInputScheduledLessonAndGroup() throws DAOException {
        lesson = new Lesson.LessonBuilder().setID(1).build();
        group = new Group.GroupBuilder().setID(1).build();
        timetable = new Timetable.TimetableBuilder().setDay(day).setLesson(lesson).setGroup(group).build();

        assertThrows(DAOException.class, () -> timetableDAOImpl.scheduleTimetable(timetable));
    }

    @Test
    void scheduleTimetable_shouldThrowDAOException_whenTeacherAbsent() throws DAOException {
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setDateTwo(LocalDate.of(2023, 04, 02));
        lesson = new Lesson.LessonBuilder().setID(1).build();
        group = new Group.GroupBuilder().setID(1).build();
        timetable = new Timetable.TimetableBuilder().setDay(day).setLesson(lesson).setGroup(group).build();
        teacherDAOImpl.setTeahcerAbsent(1, day);
        teacherDAOImpl.setTeahcerAbsent(2, day);

        assertThrows(DAOException.class, () -> timetableDAOImpl.scheduleTimetable(timetable));
    }
    
    @Test
    void scheduleTimetable_shouldThrowDAOException_whenIsNoAvailableRooms() throws DAOException {
    day.setDateOne(LocalDate.of(2023, 04, 03));
    lesson = new Lesson.LessonBuilder().setID(1).build();
    group = new Group.GroupBuilder().setID(1).build();
    timetable = new Timetable.TimetableBuilder().setDay(day).setLesson(lesson).setGroup(group).build();
    
    assertThrows(DAOException.class, () -> timetableDAOImpl.scheduleTimetable(timetable));
    }

    @Test
    void deleteTimetable_shouldReturnCountOfDeletedRows_whenInputCorrectTimetableIDAndNotPastDate() {
        assertEquals(1, timetableDAOImpl.deleteTimetable(2));
    }

    @DisplayName("Result of the test depends on test date because it's impossible to delete past timetable")
    @Test
    void deleteTimetable_shouldReturnZero_whenInputCorrectTimetableIDAndPastDate() {
        assertEquals(0, timetableDAOImpl.deleteTimetable(1));
    }

//    @Test
//    void getDayTimetable_shouldReturnDayTimetable_whenTimetableScheduledForTheTeacherAndTheDay() {
//        Lesson expectedLesson = new Lesson.LessonBuilder().setName("Alchemy").build();
//        Teacher expectedTeacher = new Teacher.TeacherBuidler().setFirstName("Albus").setLastName("Dumbledore").build();
//        Timetable expectedTimetable = new Timetable.TimetableBuilder().setDay(day).setLesson(expectedLesson)
//                .setGroup(new Group.GroupBuilder().setName("CO-68").build()).setTeacher(expectedTeacher)
//                .setRoomNumber(201).build();
//        List<Timetable> expectedList = new ArrayList<>();
//        expectedList.add(expectedTimetable);
//        user = new Teacher.TeacherBuidler().setID(1).build();
//        
//        assertEquals(Optional.of(expectedList), timetableDAOImpl.getDayTimetable(LocalDate.of(2023, 04, 01), user));
//    }
//
//    @ParameterizedTest(name = "{index}. Input data: teacher id {0}.")
//    @ValueSource(ints = { 1, 100 })
//    void getDayTimetable_shouldReturnOptionalEmpty_whenTeacherHasNotLessonsInThisDayOrTeacherIDIsIncorrect(
//            int teacherID) {
//        user = new Teacher.TeacherBuidler().setID(teacherID).build();
//        assertTrue(timetableDAOImpl.getDayTimetable(LocalDate.of(2022, 05, 20), user).get().isEmpty());
//    }

    @Test
    void getMonthTimetable_shouldReturnListOfDayTimetable_whenInputCorrectDateAndTeacherID() {
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setDateTwo(LocalDate.of(2023, 04, 02)); 
        Day day1 = new Day();
        day1.setDateOne(LocalDate.of(2023, 04, 01));
        day1.setLessonTimePeriod("08:00 - 09:20");
        Day day2 = new Day();
        day2.setDateOne(LocalDate.of(2023, 04, 02));
        day2.setLessonTimePeriod("08:00 - 09:20");
        
        user = new Teacher.TeacherBuidler().setID(1).build();
        Lesson expectedLesson = new Lesson.LessonBuilder().setName("Alchemy").build();
        Teacher expectedTeacher = new Teacher.TeacherBuidler().setFirstName("Albus").setLastName("Dumbledore").build();
        Timetable expectedTimetable1 = new Timetable.TimetableBuilder().setId(2).setDay(day1).setLesson(expectedLesson)
                .setGroup(new Group.GroupBuilder().setName("CO-68").build()).setTeacher(expectedTeacher)
                .setRoomNumber(201).build();
        Timetable expectedTimetable2 = new Timetable.TimetableBuilder().setId(4).setDay(day2).setLesson(expectedLesson)
                .setGroup(new Group.GroupBuilder().setName("CO-68").build()).setTeacher(expectedTeacher)
                .setRoomNumber(201).build();
        List<Timetable> expectedList = new ArrayList<>();
        expectedList.add(expectedTimetable1);
        expectedList.add(expectedTimetable2);
        assertEquals(Optional.of(expectedList), timetableDAOImpl.getUserTimetable(day, user));
    }

    @Test
    void getMonthTimetable_shouldReturnEmptyList_whenInputInCorrectDateAndTeacherID() {
        day.setDateOne(LocalDate.of(2021, 04, 01));
        day.setDateTwo(LocalDate.of(2021, 04, 02));
        user = new Teacher.TeacherBuidler().setID(1).build();

        assertEquals(Optional.of(new ArrayList<Timetable>()), timetableDAOImpl.getUserTimetable(day, user));
    }

    @Test
    void getMonthTimetable_shouldReturnListOfDayTimetable_whenInputCorrectDateAndStudentID() {
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setDateTwo(LocalDate.of(2023, 04, 02));
        user = new Student.StudentBuilder().setID(1).build();
        assertEquals(3, timetableDAOImpl.getUserTimetable(day, user).get().size());
    }

    @Test
    void getMonthTimetable_shouldReturnMapWithEmptyList_whenInputInCorrectDateAndStudentID() {
        day.setDateOne(LocalDate.of(2021, 04, 01));
        day.setDateTwo(LocalDate.of(2021, 04, 02));
        user = new Student.StudentBuilder().setID(1).build();
        assertEquals(Optional.of(new ArrayList<Timetable>()), timetableDAOImpl.getUserTimetable(day, user));
    }
    
    @Test
    void showMonthTimetable_shouldReturnSizeOfList_whenInputExestedDate() {
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setDateTwo(LocalDate.of(2023, 04, 03));        
        assertEquals(6, timetableDAOImpl.showTimetable(day).get().size());
    }
    @Test
    void showMonthTimetable_shouldReturnIsEmptyTrue_whenInputNotExestedDate() {
        day.setDateOne(LocalDate.of(2021, 04, 01));
        day.setDateTwo(LocalDate.of(2021, 04, 03));   
        assertTrue(timetableDAOImpl.showTimetable(day).get().isEmpty());
    }
    @Test
    void showMonthTimetable_shouldReturnSizeOfList_whenInputTheSameDate() {
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setDateTwo(LocalDate.of(2023, 04, 01));   
        assertEquals(2, timetableDAOImpl.showTimetable(day).get().size());
    }
    
}
