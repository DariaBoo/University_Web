package ua.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/rooms.sql" })
class RoomDAOImplTest {

    @Autowired
    private RoomDAO roomDAO;

    @Test
    void findSuitableRoom_shouldReturnCountOfSuitableRooms_whenInputCountOfStudents() {
        assertEquals(8, roomDAO.findSuitableRooms(20).get().size());
    }

    @Test
    void findSuitableRoom_shouldReturnCountOfSuitableRooms_whenInputZero() {
        assertEquals(10, roomDAO.findSuitableRooms(0).get().size());
    }

    @Test
    void findSuitableRoom_shouldReturnCountOfSuitableRooms_whenInputNegativeNumber() {
        assertEquals(10, roomDAO.findSuitableRooms(-1).get().size());
    }
}
