package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.foxminded.university.dao.RoomDAO;
import ua.foxminded.university.service.entities.Room;

@ExtendWith(SpringExtension.class)
class RoomServiceImplUnitTest {

    @Mock
    private RoomDAO roomDao;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room room;
    private Room room2;

    @BeforeEach
    void setup() {
        room = new Room();
        room.setNumber(101);
        room.setCapacity(25);
        room2 = new Room();
        room2.setNumber(102);
        room2.setCapacity(20);
    }

    @Test
    void findAllRooms_shouldReturnCountOfRooms_whenCallTheMethod() {
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        rooms.add(room2);
        given(roomDao.findAll()).willReturn(rooms);

        List<Room> roomsList = roomService.findAll();
        assertNotNull(roomsList);
        assertEquals(2, roomsList.size());
    }

    @Test
    void findAllRooms_shouldReturnEmptyList_whenCallTheMethod() {
        given(roomDao.findAll()).willReturn(new ArrayList<Room>());
        List<Room> roomsList = roomService.findAll();
        assertEquals(0, roomsList.size());
    }

    @Test
    void findSuitableRooms__shouldReturnCountOfRooms_whenCallTheMethod() {
        int capacity = 20;
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        rooms.add(room2);
        given(roomDao.findSuitableRooms(capacity)).willReturn(Optional.of(rooms));

        List<Room> roomsList = roomService.findSuitableRooms(capacity);
        assertNotNull(roomsList);
        assertEquals(2, roomsList.size());
    }

    @Test
    void findSuitableRooms__shouldThrowIllegalArgumentException_whenCallTheMethod() {
        int capacity = 30;
        given(roomDao.findSuitableRooms(capacity)).willReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> roomService.findSuitableRooms(capacity));
    }
}
