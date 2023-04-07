package ua.foxminded.university.controller.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Transactional
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
    @Mock
    private HttpServletRequest request;
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
    void scheduleTimetable_shouldReturnCorrectPage() {
        assertEquals("timetable/new", timetableController.scheduleTimetable(model));
    }

    @Test
    @Transactional
    void scheduleTimetableForLesson_shouldReturnCorrectPage() {
        assertEquals("timetable/new",
                timetableController.scheduleTimetableForLesson(lesson.getId(), group.getId(), model));
    }
}
