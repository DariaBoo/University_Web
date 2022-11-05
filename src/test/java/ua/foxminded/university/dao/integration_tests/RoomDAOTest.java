package ua.foxminded.university.dao.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.RoomDAO;
import ua.foxminded.university.service.entities.Room;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/rooms.sql" })
@Transactional
class RoomDAOTest {

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
    void findSuitableRoom_shouldReturnCountOfSuitableRooms_whenInputCountOfStudents() {
        assertEquals(rooms.size(), roomDAO.findSuitableRooms(15).get().size());
    }

    @Test
    void findSuitableRoom_shouldReturnCountOfSuitableRooms_whenInputZero() {
        assertEquals(rooms.size(), roomDAO.findSuitableRooms(0).get().size());
    }

    @Test
    void findSuitableRoom_shouldReturnZero_whenCapacityBigger() {
        assertEquals(0, roomDAO.findSuitableRooms(26).get().size());
    }

    @Test
    void findAll() {
        assertEquals(rooms.size(), roomDAO.findAll().size());
    }
}
