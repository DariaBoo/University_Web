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
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.entities.Group;

@SpringBootTest(classes = { AppSpringBoot.class })
@TestInstance(Lifecycle.PER_CLASS)
class GroupsControllerUnitTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private GroupsController groupsController;
    private MockMvc mockMvc;
    @MockBean
    private GroupService groupService;
    @Mock
    private Model model;

    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void listAllGroups_shouldReturnStatus200() throws Exception {
        when(groupService.findAllGroups()).thenReturn(new ArrayList<>());
        mockMvc.perform(get(URL.APP_GROUPS).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        assertEquals("groups/list", groupsController.listAllGroups(model));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void viewGroupById_shouldReturnStatus200() throws Exception {
        given(groupService.findById(any(Integer.class))).willReturn(new Group());
        mockMvc.perform(get(URL.APP_GROUPS_VIEW_BY_ID, 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("groups/view", groupsController.viewGroupById(any(Integer.class), model));
    }
}
