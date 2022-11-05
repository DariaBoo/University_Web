package ua.foxminded.university.controller.integration_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.RoomsController;

@SpringBootTest(classes = AppSpringBoot.class)
class RoomsControllerTest {

    @Autowired
    private RoomsController roomsController;
    private Model model = new ConcurrentModel();

    @Test
    void listAllRooms_shouldReturnCorrectPage() {
        assertEquals("rooms/list", roomsController.listAllRooms(model));
    }
}
