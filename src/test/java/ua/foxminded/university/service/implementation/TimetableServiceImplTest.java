package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.service.TimetableService;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Room;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;
import ua.foxminded.university.service.exception.ServiceException;
import ua.foxminded.university.springboot.AppSpringBoot;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppSpringBoot.class)
//@Sql({"/schema.sql", "/data.sql"})
@ActiveProfiles("test")
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
        lesson = Lesson.builder().id(1).build();
        group = Group.builder().id(1).build();
        teacher = Teacher.builder().id(1).build();
        room = new Room();
        room.setNumber(101);
    }

    @Test
    void scheduleTimetable_shouldThrowServiceException_whenInputTimetableWithHoliday() {
        lesson = Lesson.builder().id(5).build();
        group = Group.builder().id(1).build();
        timetable = Timetable.builder().date(LocalDate.of(2022, 01, 01))
                .lessonTimePeriod("08:00 - 09:20").lesson(lesson).group(group).teacher(teacher)
                .room(room).build();

        assertThrows(ServiceException.class, () -> timetableService.scheduleTimetable(timetable));
    }

    @Test
    void scheduleTimetable_shouldThrowServiceException_whenInputTimetableWithWeekDay() {
        timetable = Timetable.builder().date(LocalDate.of(2022, 07, 10))
                .lessonTimePeriod("08:00 - 09:20").lesson(lesson).group(group).teacher(teacher)
                .room(room).build();
        assertThrows(ServiceException.class, () -> timetableService.scheduleTimetable(timetable));
    }
}
