package ua.foxminded.university.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.web.context.WebApplicationContext;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.urls.URL;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.User;

@SpringBootTest(classes = { AppSpringBoot.class })
@TestInstance(Lifecycle.PER_CLASS)
class TeachersControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private TeachersController teachersController;
    private MockMvc mockMvc;
    private Teacher teacher;
    @MockBean
    private TeacherService teacherService;
    @Mock
    private Model model;
    
    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        User user = User.builder().id(1).firstName("name").lastName("surname").username("username").password("password").build();
        teacher = Teacher.builder().id(1).user(user).build();
    }
    
    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void listAllTeachers_shouldReturnStatus200() throws Exception {
        when(teacherService.findAllTeachers()).thenReturn(new ArrayList<>());
        mockMvc.perform(get(URL.APP_TEACHERS).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        assertEquals("teachers/list", teachersController.listAllTeachers(model));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void viewTeacherById_shouldReturnStatus200() throws Exception {
        given(teacherService.findById(any(Integer.class))).willReturn(teacher);
        mockMvc.perform(get(URL.APP_TEACHERS_VIEW_BY_ID, 1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        assertEquals("teachers/view", teachersController.viewTeacherById(any(Integer.class), model));
    }
}
