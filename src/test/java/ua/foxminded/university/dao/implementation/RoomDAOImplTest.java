package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfigTest;

@TestInstance(Lifecycle.PER_CLASS)
class RoomDAOImplTest {
    private AnnotationConfigApplicationContext context;
    private RoomDAOImpl roomDAOImpl;
    
    @BeforeAll
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        roomDAOImpl = context.getBean("roomDAOImpl", RoomDAOImpl.class);
    }
    @Test
    void test() {
        assertEquals(10, roomDAOImpl.findAll().get().stream().count());
    }

}
