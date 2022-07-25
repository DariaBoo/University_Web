package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import ua.foxminded.university.config.HibernateConfigTest;
import ua.foxminded.university.dao.TeacherDAO;
import ua.foxminded.university.dao.TimetableDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Room;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateConfigTest.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
class TimetableDAOImplTest {
    
    @Autowired
    private TimetableDAO timetableDAO;
    @Autowired
    private TeacherDAO teacherDAO;

    private Day day = new Day();
    private Lesson lesson;
    private Group group;
    private Teacher teacher;
    private Room room;
    private Timetable timetable;

    @BeforeEach
    void init() {
        new EmbeddedDatabaseBuilder().setName("test").setType(EmbeddedDatabaseType.H2)
        .addScript("classpath:tablesTest.sql").build();
        lesson = Lesson.builder().id(1).build();
        group = Group.builder().id(1).build();
        teacher = Teacher.builder().id(1).build();
        room = new Room();
        room.setNumber(101);
    }

    @Test
    void scheduleTimetable_shouldReturnCountOfAddedRows_whenInputCorrectData() {       
        timetable = Timetable.builder().date(LocalDate.of(2022, 06, 16)).lessonTimePeriod("08:00 - 09:20").lesson(lesson).group(group).teacher(teacher).room(room).build();
        assertEquals(8, timetableDAO.scheduleTimetable(timetable));
    }
    
    @Test()
    void scheduleTimetable_shouldThrowConstrainViolationException_whenInputScheduledGroup() {
        timetable = Timetable.builder().date(LocalDate.of(2022, 06, 16)).lessonTimePeriod("8:00-9:20").lesson(lesson).group(group).teacher(teacher).room(room).build();
        timetableDAO.scheduleTimetable(timetable);
        Timetable t = Timetable.builder().date(LocalDate.of(2022, 06, 16)).lessonTimePeriod("8:00-9:20").lesson(lesson).group(group).teacher(teacher).room(room).build();
        assertThrows(DAOException.class, () -> timetableDAO.scheduleTimetable(t));         
    }

    @ParameterizedTest(name = "{index}. Input data: lessonID {0}, groupID {1}.")
    @CsvSource({"100,1", "1, 100" })
    void scheduleTimetable_shouldThrowDAOException_whenInputInCorrectData(int lessonID, int groupID) {
        lesson = Lesson.builder().id(lessonID).build();
        group = Group.builder().id(groupID).build();
        timetable = Timetable.builder().date(LocalDate.of(2022, 06, 19)).lessonTimePeriod("8:00-9:20").lesson(lesson).group(group).teacher(teacher).room(room).build();

        assertThrows(DAOException.class, () -> timetableDAO.scheduleTimetable(timetable));
    }

    @Test
    void scheduleTimetable_shouldThrowDAOException_whenTeacherAbsent() {
        Day dayAbsent = new Day();
        dayAbsent.setDateOne(LocalDate.of(2022, 06, 19));
        dayAbsent.setDateTwo(LocalDate.of(2022, 06, 20));
        teacherDAO.setTeahcerAbsent(1, dayAbsent);
        lesson = Lesson.builder().id(1).build();
        group = Group.builder().id(1).build();
        timetable = Timetable.builder().date(LocalDate.of(2022, 06, 19)).lessonTimePeriod("8:00-9:20").lesson(lesson).group(group).teacher(teacher).room(room).build();
        assertThrows(NoSuchElementException.class, () -> timetableDAO.scheduleTimetable(timetable));
    }
    
    @Test
    void scheduleTimetable_shouldThrowDAOException_whenIsNoAvailableRooms() throws DAOException {
    timetable = Timetable.builder().date(LocalDate.of(2022, 06, 19)).lessonTimePeriod("8:00-9:20").lesson(lesson).group(group).teacher(teacher).room(room).build();
    timetableDAO.scheduleTimetable(timetable);
    lesson = Lesson.builder().id(2).build();
    group = Group.builder().id(2).build();
    teacher = Teacher.builder().id(2).build();
    Timetable timetable2 = Timetable.builder().date(LocalDate.of(2022, 06, 19)).lessonTimePeriod("8:00-9:20").lesson(lesson).group(group).teacher(teacher).room(room).build();
    assertThrows(DAOException.class, () -> timetableDAO.scheduleTimetable(timetable2));
    }

    @Test
    void deleteTimetable_shouldReturnTrue_whenInputCorrectTimetableIDAndNotPastDate() {
        timetable = Timetable.builder().date(LocalDate.now().plusDays(1)).lessonTimePeriod("8:00-9:20").lesson(lesson).group(group).teacher(teacher).room(room).build();
        int id = timetableDAO.scheduleTimetable(timetable);
        assertTrue(timetableDAO.deleteTimetable(id));
    }

    @DisplayName("It's impossible to delete past timetable")
    @Test
    void deleteTimetable_shouldReturnZero_whenInputCorrectTimetableIDAndPastDate() {
        timetable = Timetable.builder().date(LocalDate.now().minusDays(1)).lessonTimePeriod("8:00-9:20").lesson(lesson).group(group).teacher(teacher).room(room).build();
        int id = timetableDAO.scheduleTimetable(timetable);
        assertFalse(timetableDAO.deleteTimetable(id));
    }

    @Test
    void getMonthTimetable_shouldReturnListOfTimetable_whenInputCorrectDateAndTeacherID() {
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setDateTwo(LocalDate.of(2023, 04, 02));         
        Teacher teacher = Teacher.builder().id(1).build();
        assertEquals(2, timetableDAO.getTeacherTimetable(day, teacher).get().size());
    }

    @Test
    void getMonthTimetable_shouldReturnEmptyList_whenInputInCorrectDateAndTeacherID() {
        day.setDateOne(LocalDate.of(2021, 04, 01));
        day.setDateTwo(LocalDate.of(2021, 04, 02));
        Teacher teacher = Teacher.builder().id(1).build();
        assertEquals(Optional.of(new ArrayList<Timetable>()), timetableDAO.getTeacherTimetable(day, teacher));
    }

    @Test
    void getMonthTimetable_shouldReturnListOfDayTimetable_whenInputCorrectDateAndStudentID() {
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setDateTwo(LocalDate.of(2023, 04, 02));
        Student student = Student.builder().id(1).build();
        assertEquals(0, timetableDAO.getStudentTimetable(day, student).get().size());
    }

    @Test
    void getMonthTimetable_shouldReturnMapWithEmptyList_whenInputInCorrectDateAndStudentID() {
        day.setDateOne(LocalDate.of(2021, 04, 01));
        day.setDateTwo(LocalDate.of(2021, 04, 02));
        Student student = Student.builder().id(1).build();
        assertEquals(Optional.of(new ArrayList<Timetable>()), timetableDAO.getStudentTimetable(day, student));
    }
    
    @Test
    void showMonthTimetable_shouldReturnSizeOfList_whenInputExestedDate() {
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setDateTwo(LocalDate.of(2023, 04, 03));        
        assertEquals(6, timetableDAO.showTimetable(day).get().size());
    }
    @Test
    void showMonthTimetable_shouldReturnIsEmptyTrue_whenInputNotExestedDate() {
        day.setDateOne(LocalDate.of(2021, 04, 01));
        day.setDateTwo(LocalDate.of(2021, 04, 03));   
        assertTrue(timetableDAO.showTimetable(day).get().isEmpty());
    }
    @Test
    void showMonthTimetable_shouldReturnSizeOfList_whenInputTheSameDate() {
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setDateTwo(LocalDate.of(2023, 04, 01));   
        assertEquals(2, timetableDAO.showTimetable(day).get().size());
    }  
}
