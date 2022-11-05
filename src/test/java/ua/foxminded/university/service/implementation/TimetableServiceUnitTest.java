package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.foxminded.university.dao.TimetableDAO;
import ua.foxminded.university.service.LessonTimePeriod;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Room;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;
import ua.foxminded.university.service.entities.User;

@ExtendWith(SpringExtension.class)
class TimetableServiceUnitTest {

    @Mock
    private TimetableDAO timetableDao;
    @InjectMocks
    private TimetableServiceImpl timetableService;

    private Timetable timetable;
    private Timetable timetable2;
    private String lessonTimePeriod;
    private LocalDate date;
    private Day day;
    private Student student;

    @BeforeEach
    void setup() {
        student = Student.builder().id(1).user(new User()).group(new Group()).idCard("AA-00").build();
        lessonTimePeriod = LessonTimePeriod.lesson1.toString();
        date = LocalDate.of(2022, 10, 21);
        timetable = Timetable.builder().timetableId(1).date(date).lessonTimePeriod(lessonTimePeriod).group(new Group())
                .lesson(new Lesson()).teacher(new Teacher()).room(new Room()).build();
        timetable2 = Timetable.builder().timetableId(2).date(date).lessonTimePeriod(lessonTimePeriod).group(new Group())
                .lesson(new Lesson()).teacher(new Teacher()).room(new Room()).build();
        day = new Day();
        day.setDateOne(date);
        day.setDateTwo(date);
    }

    @Test
    void deleteTimetable() {
        int timetableId = 1;
        willDoNothing().given(timetableDao).deleteById(timetableId);
        given(timetableDao.existsById(1)).willReturn(true);
        timetableService.deleteTimetable(timetableId);
        verify(timetableDao, times(1)).deleteById(timetableId);
    }

    @Test
    void getTeacherTimetable_shouldReturnNotEmptyList_whenInputExistedData() {
        List<Timetable> timetables = new ArrayList<>();
        timetables.add(timetable);
        timetables.add(timetable2);
        given(timetableDao.findByDateAndTeacher(date, date, new Teacher())).willReturn(Optional.of(timetables));

        List<Timetable> timetableList = timetableService.getTeacherTimetable(day, new Teacher());
        assertNotNull(timetableList);
        assertEquals(2, timetableList.size());
    }

    @Test
    void getTeacherTimetable_shouldThrowIllegalArgumentException_whenInputNotExistedData() {
        given(timetableDao.findByDateAndTeacher(date, date, new Teacher())).willReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> timetableService.getTeacherTimetable(day, new Teacher()));
    }

    @Test
    void getStudentTimetable_shouldReturnNotEmptyList_whenInputExistedData() {
        List<Timetable> timetables = new ArrayList<>();
        timetables.add(timetable);
        timetables.add(timetable2);
        given(timetableDao.findByDateAndGroup(date, date, new Group())).willReturn(Optional.of(timetables));

        List<Timetable> timetableList = timetableService.getStudentTimetable(day, student);
        assertNotNull(timetableList);
        assertEquals(2, timetableList.size());
    }

    @Test
    void getStudentTimetable_shouldThrowIllegalArgumentException_whenInputNotExistedData() {
        given(timetableDao.findByDateAndGroup(date, date, new Group())).willReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> timetableService.getStudentTimetable(day, new Student()));
    }

    @Test
    void showTimetable_shouldReturnNotEmptyList_whenInputExistedData() {
        List<Timetable> timetables = new ArrayList<>();
        timetables.add(timetable);
        timetables.add(timetable2);
        given(timetableDao.findByDate(date, date)).willReturn(Optional.of(timetables));

        List<Timetable> timetableList = timetableService.showTimetable(day);
        assertNotNull(timetableList);
        assertEquals(2, timetableList.size());
    }

    @Test
    void showTimetable_shouldThrowIllegalArgumentException_whenInputNotExistedData() {
        given(timetableDao.findByDate(date, date)).willReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> timetableService.showTimetable(day));
    }
}
