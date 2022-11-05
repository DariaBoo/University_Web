package ua.foxminded.university.service.implementation.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.RoomDAO;
import ua.foxminded.university.service.RoomService;
import ua.foxminded.university.service.entities.Room;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/groups.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class RoomServiceImplTest {

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomDAO roomDAO;
    private Room room;
    private Room room2;
    private List<Room> rooms;

    @BeforeEach
    void setup() {
        room = new Room();
        room.setNumber(101);
        room.setCapacity(20);
        room2 = new Room();
        room2.setNumber(102);
        room2.setCapacity(25);
        rooms = new ArrayList<>();
        rooms.add(room);
        rooms.add(room2);
        roomDAO.save(room);
        roomDAO.save(room2);
    }

    @Test
    void findAll_shouldReturnList() {
        assertEquals(rooms.size(), roomService.findAll().size());
    }

    @Test
    void findSuitableRooms_shouldReturnList_whenInputCorrectData() {
        assertEquals(rooms.size(), roomService.findSuitableRooms(20).size());
    }

    @Test
    void findSuitableRooms_shouldReturnEmptyList_whenInputIncorectData() {
        assertEquals(new ArrayList<>(), roomService.findSuitableRooms(30));
    }
}
