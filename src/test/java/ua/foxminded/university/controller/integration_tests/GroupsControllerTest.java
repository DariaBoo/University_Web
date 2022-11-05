package ua.foxminded.university.controller.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.GroupsController;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.entities.Group;

@SpringBootTest(classes = AppSpringBoot.class)
@TestInstance(Lifecycle.PER_CLASS)
class GroupsControllerTest {

    @Autowired
    private GroupsController groupsController;
    @Autowired
    private GroupService groupService;
    private Model model = new ConcurrentModel();
    private Group group;

    @BeforeAll
    void setup() {
        group = Group.builder().name("AA-00").departmentId(1).build();
        groupService.addGroup(group);
    }

    @Test
    void listAllGroups_shouldReturnCorrectPage() {
        assertEquals("groups/list", groupsController.listAllGroups(model));
    }

    @Test
    void viewGroupById_shouldReturnCorrectPage() {
        assertEquals("groups/view", groupsController.viewGroupById(group.getId(), model));
    }
}
