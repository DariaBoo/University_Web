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
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.entities.Lesson;

@SpringBootTest(classes = { AppSpringBoot.class })
@TestInstance(Lifecycle.PER_CLASS)
class LessonsControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private LessonsController lessonsController;
    private MockMvc mockMvc;
    @MockBean
    private LessonService lessonService;
    @Mock
    private Model model;
    
    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    
    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void listAllLessons_shouldReturnStatus200() throws Exception {
        when(lessonService.findAllLessons()).thenReturn(new ArrayList<>());
        mockMvc.perform(get(URL.APP_LESSONS).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        assertEquals("lessons/list", lessonsController.listAllLessons(model));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void viewLessonById_shouldReturnStatus200() throws Exception {
        given(lessonService.findById(any(Integer.class))).willReturn(new Lesson());
        mockMvc.perform(get(URL.APP_LESSONS_VIEW_BY_ID, 1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        assertEquals("lessons/view", lessonsController.viewLessonById(any(Integer.class), model));
    }
}
