package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.service.pojo.Holiday;

@TestInstance(Lifecycle.PER_CLASS)
class HolidayDAOImplTest {
    private HolidayDAOImpl holidayDAOImpl;
    private AnnotationConfigApplicationContext context;
    private Holiday holiday = new Holiday();
    
    @BeforeAll
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        holidayDAOImpl = context.getBean("holidayDAOImpl", HolidayDAOImpl.class);
    }
    
    @Test
    void addHoliday_shouldReturnOne_whenInputCorrectHolidayData() {
        holiday.setDate(LocalDate.of(2022, 05, 23));
        holiday.setHolidayName("Holiday");
        assertEquals(1, holidayDAOImpl.addHoliday(holiday));
    }

//    @Test
//    void 

}
