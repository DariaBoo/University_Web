package ua.foxminded.university.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.urls.URL;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.User;
import ua.foxminded.university.service.exception.EntityConstraintViolationException;

@SpringBootTest(classes = { AppSpringBoot.class })
@TestInstance(Lifecycle.PER_CLASS)
class StudentsControllerUnitTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private StudentsController studentsController;
    private MockMvc mockMvc;
    @MockBean
    private StudentService studentService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private BindingResult bindingResult;
    @Mock
    private Model model;
    @Mock
    private RedirectAttributes redirectAtt;
    private Student student;

    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        User user = User.builder().id(1).firstName("name").lastName("surname").username("username").password("password")
                .build();
        student = Student.builder().id(1).user(user).group(new Group()).build();
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void listAllStudents_shouldReturnStatus200() throws Exception {
        when(studentService.findAllStudents()).thenReturn(new ArrayList<>());
        mockMvc.perform(get(URL.APP_STUDENTS).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        assertEquals("students/list", studentsController.listAllStudents(model));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void viewStudentById_shouldReturnStatus200() throws Exception {
        given(studentService.findById(any(Integer.class))).willReturn(student);
        mockMvc.perform(get(URL.APP_STUDENTS_VIEW_BY_ID, 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("students/view", studentsController.viewStudentById(any(Integer.class), model));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void createNewStudent_shouldReturnStatus200() throws Exception {
        mockMvc.perform(get(URL.APP_NEW_STUDENT, 1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        assertEquals("students/new", studentsController.createNewStudent(student, model));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void saveNewStudent_shouldReturnStatus302_whenEntityIsCorrect() throws Exception {
        given(bindingResult.hasErrors()).willReturn(false);
        when(studentService.addStudent(student)).thenReturn(student);
        mockMvc.perform(post(URL.APP_STUDENTS, student)).andExpect(status().is3xxRedirection());
        assertEquals("redirect:/app/students",
                studentsController.saveNewStudent(new Student(), bindingResult, redirectAtt));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void saveNewStudent_shouldReturnStatus302_whenThrowUniqueConstraintViolationException() throws Exception {
        given(bindingResult.hasErrors()).willReturn(false);
        when(studentService.addStudent(student)).thenThrow(EntityConstraintViolationException.class);
        mockMvc.perform(post(URL.APP_STUDENTS, student)).andExpect(status().is3xxRedirection());
        assertEquals("redirect:/app/students",
                studentsController.saveNewStudent(new Student(), bindingResult, redirectAtt));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void saveNewStudent_shouldReturnStatus302_whenEntityHasErrors() throws Exception {
        given(bindingResult.hasErrors()).willReturn(true);
        mockMvc.perform(post(URL.APP_STUDENTS, student)).andExpect(status().is3xxRedirection());
        assertEquals("redirect:/app/students",
                studentsController.saveNewStudent(new Student(), bindingResult, redirectAtt));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void deleteStudent_shouldReturn302() throws Exception {
        doNothing().when(studentService).deleteStudent(1);
        mockMvc.perform(delete(URL.APP_DELETE_STUDENT_BY_ID, 1).contentType(MediaType.APPLICATION_JSON)
                .content("{redirectAtt}")).andExpect(status().is3xxRedirection());
        assertEquals("redirect:/app/students", studentsController.deleteStudent(1, redirectAtt));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void deleteStudent_shouldReturnStatus302_whenDeleteStudentReturnFalse() throws Exception {
        doNothing().when(studentService).deleteStudent(1);
        mockMvc.perform(delete(URL.APP_DELETE_STUDENT_BY_ID, 1).contentType(MediaType.APPLICATION_JSON)
                .content("{redirectAtt}")).andExpect(status().is3xxRedirection());
        assertEquals("redirect:/app/students", studentsController.deleteStudent(1, redirectAtt));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void edit() throws Exception {
        given(studentService.findById(any(Integer.class))).willReturn(student);
        given(groupService.findAllGroups()).willReturn(new ArrayList<Group>());
        mockMvc.perform(get(URL.APP_EDIT_STUDENT_BY_ID, 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("students/edit", studentsController.edit(1, model, redirectAtt));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void update_shouldReturnStatus302_whenStudentIsValid() throws Exception {
        given(bindingResult.hasErrors()).willReturn(true);
        mockMvc.perform(
                post(URL.APP_STUDENTS_VIEW_BY_ID, 1).contentType(MediaType.APPLICATION_JSON).content("{redirectAtt}"))
                .andExpect(status().is3xxRedirection());
        assertEquals("redirect:/app/students", studentsController.update(student, bindingResult, redirectAtt));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void update_shouldReturnStatus302_whenStudentIsNotValid() throws Exception {
        given(bindingResult.hasErrors()).willReturn(false);
        given(studentService.updateStudent(any(Student.class))).willReturn(student);
        mockMvc.perform(
                post(URL.APP_STUDENTS_VIEW_BY_ID, 1).contentType(MediaType.APPLICATION_JSON).content("{redirectAtt}"))
                .andExpect(status().is3xxRedirection());
        assertEquals("redirect:/app/students", studentsController.update(student, bindingResult, redirectAtt));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void changePassword() throws Exception {
        String newPassword = "newPassword";
        willDoNothing().given(studentService).changePassword(any(Integer.class), any(String.class));
        mockMvc.perform(post(URL.STUDENT_CHANGE_PASSWORD, 1).contentType(MediaType.APPLICATION_JSON)
                .param("newPassword", newPassword)).andExpect(status().isOk());
    }
}
