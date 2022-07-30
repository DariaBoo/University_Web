package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.foxminded.university.dao.RoomDAO;
import ua.foxminded.university.springboot.AppSpringBoot;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppSpringBoot.class)
//@Sql({"/schema.sql", "/data.sql"})
@ActiveProfiles("test")
class RoomDAOImplTest {
    
    @Autowired
    private RoomDAO roomDAO;
    
    @Test 
    void findSuitableRoom() {
        System.out.println(roomDAO.findAll());
        assertEquals(10, roomDAO.findSuitableRooms(20).get().size());
    }   
}
