package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.TimetableDAO;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/timetable.sql" })
class TimetableDAOImplTest {

    @Autowired
    private TimetableDAO timetableDAO;

    private Day day = new Day();

    @Test
    void getMonthTimetable_shouldReturnListOfTimetable_whenInputCorrectDateAndTeacherId() {
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setDateTwo(LocalDate.of(2023, 04, 02));
        Teacher teacher = Teacher.builder().id(1).build();
        assertEquals(2, timetableDAO.findByDateAndTeacher(day.getDateOne(), day.getDateTwo(), teacher).get().size());
    }

    @Test
    void getMonthTimetable_shouldReturnEmptyList_whenInputInCorrectDateAndTeacherId() {
        day.setDateOne(LocalDate.of(2021, 04, 01));
        day.setDateTwo(LocalDate.of(2021, 04, 02));
        Teacher teacher = Teacher.builder().id(1).build();
        assertEquals(Optional.of(new ArrayList<Timetable>()),
                timetableDAO.findByDateAndTeacher(day.getDateOne(), day.getDateTwo(), teacher));
    }

    @Test
    void getMonthTimetable_shouldReturnListOfDayTimetable_whenInputCorrectDateAndStudentId() {
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setDateTwo(LocalDate.of(2023, 04, 02));
        Student student = Student.builder().id(1).build();
        assertEquals(0,
                timetableDAO.findByDateAndGroup(day.getDateOne(), day.getDateTwo(), student.getGroup()).get().size());
    }

    @Test
    void getMonthTimetable_shouldReturnMapWithEmptyList_whenInputInCorrectDateAndStudentId() {
        day.setDateOne(LocalDate.of(2021, 04, 01));
        day.setDateTwo(LocalDate.of(2021, 04, 02));
        Student student = Student.builder().id(1).build();
        assertEquals(Optional.of(new ArrayList<Timetable>()),
                timetableDAO.findByDateAndGroup(day.getDateOne(), day.getDateTwo(), student.getGroup()));
    }

    @Test
    void showMonthTimetable_shouldReturnSizeOfList_whenInputExestedDate() {
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setDateTwo(LocalDate.of(2023, 04, 03));
        assertEquals(3, timetableDAO.findByDate(day.getDateOne(), day.getDateTwo()).get().size());
    }

    @Test
    void showMonthTimetable_shouldReturnIsEmptyTrue_whenInputNotExestedDate() {
        day.setDateOne(LocalDate.of(2021, 04, 01));
        day.setDateTwo(LocalDate.of(2021, 04, 03));
        assertTrue(timetableDAO.findByDate(day.getDateOne(), day.getDateTwo()).get().isEmpty());
    }

    @Test
    void showMonthTimetable_shouldReturnSizeOfList_whenInputTheSameDate() {
        day.setDateOne(LocalDate.of(2023, 04, 01));
        day.setDateTwo(LocalDate.of(2023, 04, 01));
        assertEquals(1, timetableDAO.findByDate(day.getDateOne(), day.getDateTwo()).get().size());
    }
}
