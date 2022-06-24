package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfigTest;

@TestInstance(Lifecycle.PER_CLASS)
class HolidayServiceImplTest {
    private AnnotationConfigApplicationContext context;
    private HolidayServiceImpl holidayServiceImpl;
    
    @BeforeAll
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        holidayServiceImpl = context.getBean("holidayServiceImpl", HolidayServiceImpl.class);
    }
    

    @Test
    void test() {
        assertEquals(1, holidayServiceImpl.deleteHoliday(1));
    }

}
