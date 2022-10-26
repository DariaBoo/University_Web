package ua.foxminded.university.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import ua.foxminded.university.service.RoomService;

@SpringBootTest(classes = { AppSpringBoot.class })
@TestInstance(Lifecycle.PER_CLASS)
class RoomsControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private RoomsController roomsController;
    private MockMvc mockMvc;
    @MockBean
    private RoomService roomService;
    @Mock
    private Model model;
    
    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    
    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void listAllRooms_shouldReturnStatus200() throws Exception {
        when(roomService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get(URL.APP_ROOMS).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        assertEquals("rooms/list", roomsController.listAllRooms(model));
    }
}
