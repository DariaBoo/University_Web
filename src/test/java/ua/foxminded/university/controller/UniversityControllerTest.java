package ua.foxminded.university.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.urls.URL;

@SpringBootTest(classes = { AppSpringBoot.class })
@TestInstance(Lifecycle.PER_CLASS)
class UniversityControllerTest {

    @Autowired
    private WebApplicationContext context;
    private final String contentType = MediaType.APPLICATION_JSON_VALUE;
    private MockMvc mvc;

    @BeforeAll
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void showHomePage_shouldReturn200Status_whenAccessAdmin() throws Exception {
        mvc.perform(get(URL.APP_HOME).with(csrf()).contentType(contentType)).andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(authorities = {"STUDENT"})
    void showHomePage_shouldReturn403Status_whenAccessStudent() throws Exception {
        mvc.perform(get(URL.APP_HOME).with(csrf()).contentType(contentType)).andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(authorities = {"TEACHER"})
    void showHomePage_shouldReturn403Status_whenAccessTeacher() throws Exception {
        mvc.perform(get(URL.APP_HOME).with(csrf()).contentType(contentType)).andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser
    void showHomePage_shouldReturn403Status_whenAccessUser() throws Exception {
        mvc.perform(get(URL.APP_HOME).with(csrf()).contentType(contentType)).andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void showStudentPage_shouldReturn200Status_whenAccessAdmin() throws Exception {
        mvc.perform(get(URL.HOME_STUDENT).with(csrf()).contentType(contentType)).andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(authorities = {"STUDENT"})
    void showStudentPage_shouldReturn200Status_whenAccessStudent() throws Exception {
        mvc.perform(get(URL.HOME_STUDENT).with(csrf()).contentType(contentType)).andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(authorities = {"TEACHER"})
    void showStudentPage_shouldReturn403Status_whenAccessTeacher() throws Exception {
        mvc.perform(get(URL.HOME_STUDENT).with(csrf()).contentType(contentType)).andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser
    void showStudentPage_shouldReturn403Status_whenAccessUser() throws Exception {
        mvc.perform(get(URL.HOME_STUDENT).with(csrf()).contentType(contentType)).andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void showTeacherPage_shouldReturn200Status_whenAccessAdmin() throws Exception {
        mvc.perform(get(URL.HOME_TEACHER).with(csrf()).contentType(contentType)).andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(authorities = {"TEACHER"})
    void showTeacherPage_shouldReturn200Status_whenAccessTeacher() throws Exception {
        mvc.perform(get(URL.HOME_TEACHER).with(csrf()).contentType(contentType)).andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(authorities = {"STUDENT"})
    void showTeacherPage_shouldReturn403Status_whenAccessStudent() throws Exception {
        mvc.perform(get(URL.HOME_TEACHER).with(csrf()).contentType(contentType)).andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser
    void showTeacherPage_shouldReturn403Status_whenAccessUser() throws Exception {
        mvc.perform(get(URL.HOME_TEACHER).with(csrf()).contentType(contentType)).andExpect(status().isForbidden());
    }
}
