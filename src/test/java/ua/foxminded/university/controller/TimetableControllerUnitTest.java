package ua.foxminded.university.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.urls.URL;
import ua.foxminded.university.security.model.AuthenticatedUser;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.RoomService;
import ua.foxminded.university.service.StudentService;
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
import ua.foxminded.university.service.exception.EntityConstraintViolationException;

@SpringBootTest(classes = { AppSpringBoot.class })
@TestInstance(Lifecycle.PER_CLASS)
class TimetableControllerUnitTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private TimetableController timetableController;
    private MockMvc mockMvc;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private TimetableService timetableService;
    @MockBean
    private LessonService lessonService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private RoomService roomService;
    @MockBean
    private StudentService studentService;
    @MockBean
    private TeacherService teacherService;
    @Mock
    private Authentication auth;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Model model;

    private Group group;
    private Lesson lesson;
    private Room room;
    private Teacher teacher;
    private Student student;
    private Timetable timetable;
    private LocalDate date = LocalDate.of(2022, 10, 26);
    private AuthenticatedUser authenticatedUser;

    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        group = Group.builder().id(1).name("group").departmentId(1).lessons(new ArrayList<>())
                .students(new ArrayList<>()).build();
        lesson = Lesson.builder().id(1).name("lesson").description("description").teachers(new ArrayList<>()).build();
        room = new Room();
        room.setNumber(101);
        room.setCapacity(25);
        User user = User.builder().id(1).firstName("name").lastName("surname").username("username").password("password")
                .build();
        teacher = Teacher.builder().id(1).user(user).position("position").departmentId(1).build();
        timetable = Timetable.builder().timetableId(1).date(date).lessonTimePeriod("8:00 - 9:20").group(group)
                .lesson(lesson).teacher(teacher).room(room).build();
        authenticatedUser = new AuthenticatedUser(user);
        student = Student.builder().id(1).user(user).group(group).idCard("AA-00").build();

    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void chooseDatePeriod() throws Exception {
        mockMvc.perform(get(URL.APP_TIMETABLE).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        assertEquals("timetable/index", timetableController.chooseDatePeriod(model));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void scheduleTimetable() throws Exception {
        mockMvc.perform(get(URL.APP_TIMETABLE_SCHEDULE).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("timetable/new", timetableController.scheduleTimetable(model));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void scheduleTimetableForGroup() throws Exception {
        given(groupService.findById(any(Integer.class))).willReturn(new Group());
        given(groupService.findAllGroups()).willReturn(new ArrayList<Group>());

        mockMvc.perform(get(URL.APP_TIMETABLE_SCHEDULE_GROUP, 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("timetable/new", timetableController.scheduleTimetableForGroup(1, model));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void saveTimetable_whenHasErrors_shouldReturnStatus3xx() throws Exception {
        given(bindingResult.hasErrors()).willReturn(true);
        mockMvc.perform(post(URL.APP_TIMETABLE_SCHEDULE, timetable)).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void saveTimetable_whenHasNoErrors_shouldReturnStatus3xx() throws Exception {
        given(bindingResult.hasErrors()).willReturn(false);
        given(timetableService.scheduleTimetable(timetable)).willReturn("Timetable was scheduled successfully!");
        mockMvc.perform(post(URL.APP_TIMETABLE_SCHEDULE).contentType(MediaType.APPLICATION_JSON).flashAttr("timetable",
                timetable)).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void saveTimetable_shouldReturnStatus3xx() throws Exception {
        given(bindingResult.hasErrors()).willReturn(false);
        given(timetableService.scheduleTimetable(timetable)).willThrow(EntityConstraintViolationException.class);
        mockMvc.perform(post(URL.APP_TIMETABLE_SCHEDULE).contentType(MediaType.APPLICATION_JSON).flashAttr("timetable",
                timetable)).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void scheduleTimetableForLesson_shouldReturnStatusOk() throws Exception {
        given(groupService.findById(any(Integer.class))).willReturn(group);
        given(lessonService.findById(any(Integer.class))).willReturn(lesson);
        given(groupService.findAllGroups()).willReturn(new ArrayList<>());
        given(roomService.findSuitableRooms(any(Integer.class))).willReturn(new ArrayList<>());
        mockMvc.perform(get(URL.APP_TIMETABLE_SCHEDULE_GROUP_LESSON, 1, 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("timetable/new", timetableController.scheduleTimetableForLesson(1, 1, model));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void deleteTimetable_shouldReturnStatus3xx() throws Exception {
        doNothing().when(timetableService).deleteTimetable(any(Integer.class));
        mockMvc.perform(delete(URL.APP_TIMETABLE_DELETE, 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void showTimetable_shouldReturnStatusOk() throws Exception {
        given(timetableService.showTimetable(any(Day.class))).willReturn(new ArrayList<Timetable>());
        mockMvc.perform(get(URL.APP_TIMETABLE_SHOW).contentType(MediaType.APPLICATION_JSON).param("from", "2022-10-01")
                .param("to", "2022-10-26")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void showTimetable_shouldReturnStatus200() throws Exception {
        given(timetableService.showTimetable(any(Day.class))).willThrow(IllegalArgumentException.class);
        mockMvc.perform(get(URL.APP_TIMETABLE_SHOW).contentType(MediaType.APPLICATION_JSON).param("from", "2022-10-01")
                .param("to", "2022-10-26")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void showStudentTimetable_shouldReturnStatus200() throws Exception {
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        given(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).willReturn(authenticatedUser);
        given(studentService.findByUsername(authenticatedUser.getUsername())).willReturn(student);
        given(timetableService.getStudentTimetable(any(Day.class), any(Student.class)))
                .willReturn(new ArrayList<Timetable>());
        mockMvc.perform(get(URL.STUDENT_TIMETABLE).contentType(MediaType.APPLICATION_JSON).param("from", "2022-10-01")
                .param("to", "2022-10-26")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void showStudentTimetable_shouldReturnStatusOk() throws Exception {
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        given(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).willReturn(authenticatedUser);
        given(studentService.findByUsername(authenticatedUser.getUsername())).willReturn(student);
        given(timetableService.getStudentTimetable(any(Day.class), any(Student.class)))
                .willThrow(IllegalArgumentException.class);
        mockMvc.perform(get(URL.STUDENT_TIMETABLE).contentType(MediaType.APPLICATION_JSON).param("from", "2022-10-01")
                .param("to", "2022-10-26")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void showTeacherTimetable_shouldReturnStatus200() throws Exception {
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        given(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).willReturn(authenticatedUser);
        given(teacherService.findByUsername(authenticatedUser.getUsername())).willReturn(teacher);
        given(timetableService.getTeacherTimetable(any(Day.class), any(Teacher.class)))
                .willReturn(new ArrayList<Timetable>());
        mockMvc.perform(get(URL.TEACHER_TIMETABLE).contentType(MediaType.APPLICATION_JSON).param("from", "2022-10-01")
                .param("to", "2022-10-26")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void showTeacherTimetable_shouldReturnStatusOk() throws Exception {
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        given(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).willReturn(authenticatedUser);
        given(teacherService.findByUsername(authenticatedUser.getUsername())).willReturn(teacher);
        given(timetableService.getTeacherTimetable(any(Day.class), any(Teacher.class)))
                .willThrow(IllegalArgumentException.class);
        mockMvc.perform(get(URL.TEACHER_TIMETABLE).contentType(MediaType.APPLICATION_JSON).param("from", "2022-10-01")
                .param("to", "2022-10-26")).andExpect(status().isOk());
    }
}
