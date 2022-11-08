package ua.foxminded.university.controller.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.TimetableController;
import ua.foxminded.university.dao.RoomDAO;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.TimetableService;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Room;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;
import ua.foxminded.university.service.entities.User;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/timetable.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class TimetableControllerTest {

    @Autowired
    private TimetableController timetableController;
    @Autowired
    private TimetableService timetableService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private RoomDAO roomDao;
    @Autowired
    private UserDAO userDao;
    private Model model = new ConcurrentModel();
    @Mock
    private RedirectAttributes redirectAtt;
    @Mock
    private BindingResult bindingResult;
    @MockBean
    private SecurityContextHolder context;

    private Teacher teacher;
    private Group group;
    private Student student;
    private Lesson lesson;
    private Room room;
    private Day day = new Day();
    private User user;
    private Timetable timetable;
    private Timetable timetable2;
    private LocalDate date = LocalDate.of(2022, 10, 28);

    @BeforeEach
    void setup() {
        day.setDateOne(date);
        day.setDateTwo(date);
        user = User.builder().username("username").firstName("name").lastName("surname").password("password").build();
        userDao.save(user);
        group = Group.builder().name("AA-00").departmentId(1).students(Stream.of(student).collect(Collectors.toList()))
                .build();
        groupService.addGroup(group);
        room = new Room();
        room.setNumber(101);
        room.setCapacity(25);
        lesson = Lesson.builder().name("lesson").description("description").build();
        teacher = Teacher.builder().user(user).position("position").departmentId(1).build();
        teacherService.addTeacher(teacher);
        roomDao.save(room);
        lessonService.addLesson(lesson);
    }

    @Test
    void chooseDatePeriod_shouldReturnCorrectPage() {
        assertEquals("timetable/index", timetableController.chooseDatePeriod(model));
    }

    @Test
    void showTimetable_shouldReturnStatus302_whenInputCorrectData() {
        timetable = Timetable.builder().date(date).lessonTimePeriod("08:00 - 09:20").group(group).lesson(lesson)
                .teacher(teacher).room(room).build();
        timetableService.scheduleTimetable(timetable);
        List<Timetable> timetables = new ArrayList<>();
        timetables.add(timetable);
        String dateOne = "2022-10-28";
        String dateTwo = "2022-10-28";
        assertEquals(timetables.size(), timetableController.showTimetable(dateOne, dateTwo, model).getBody().size());
        assertEquals(HttpStatus.FOUND, timetableController.showTimetable(dateOne, dateTwo, model).getStatusCode());
    }

    @Test
    void scheduleTimetable_shouldReturnCorrectPage() {
        assertEquals("timetable/new", timetableController.scheduleTimetable(model));
    }

    @Test
    @Transactional
    void scheduleTimetableForLesson_shouldReturnCorrectPage() {
        assertEquals("timetable/new",
                timetableController.scheduleTimetableForLesson(lesson.getId(), group.getId(), model));
    }

    @Test
    void saveTimetable_shouldReturnStatus200_whenInputCorrectData() {
        timetable2 = Timetable.builder().date(date).lessonTimePeriod("08:00 - 09:20").group(group).lesson(lesson)
                .teacher(teacher).room(room).build();
        assertEquals(new ResponseEntity<>("Timetable was scheduled successfully!", HttpStatus.CREATED),
                timetableController.saveTimetable(timetable2, bindingResult));
    }

    @Test
    void saveTimetable_shouldReturnErrorMessage_whenInputIncorrectData() {
        timetable2 = Timetable.builder().date(date).lessonTimePeriod("08:00 - 09:20").group(group).lesson(lesson)
                .teacher(teacher).room(null).build();
        String message = "Room may not be null";
        assertTrue(timetableController.saveTimetable(timetable2, bindingResult).getBody().contains(message));
    }

    @Test
    void deleteTimetable_shouldReturnStatus200_whenInputCorrectData() {
        timetable = Timetable.builder().date(date).lessonTimePeriod("08:00 - 09:20").group(group).lesson(lesson)
                .teacher(teacher).room(room).build();
        timetableService.scheduleTimetable(timetable);
        assertEquals(new ResponseEntity<>("Timetable was deleted!", HttpStatus.OK),
                timetableController.deleteTimetable(timetable.getTimetableId()));
    }

    @Test
    void deleteTimetable_shouldReturnStatus400_whenInputInCorrectData() {
        timetable = Timetable.builder().date(date).lessonTimePeriod("08:00 - 09:20").group(group).lesson(lesson)
                .teacher(teacher).room(room).build();
        assertEquals(
                new ResponseEntity<>("Timetable with id 0 doesn't exist. Nothing to delete.", HttpStatus.BAD_REQUEST),
                timetableController.deleteTimetable(timetable.getTimetableId()));
    }
}
