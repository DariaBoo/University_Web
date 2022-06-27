package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.stream.Collectors;

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
    private Holiday futureHoliday = new Holiday();
    private Holiday pastHoliday = new Holiday();
    
    @BeforeAll
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        holidayDAOImpl = context.getBean("holidayDAOImpl", HolidayDAOImpl.class);
        futureHoliday.setDate(LocalDate.now().plusDays(1));
        futureHoliday.setName("Holiday");
        pastHoliday.setDate(LocalDate.now().minusDays(1));
        pastHoliday.setName("Holiday");
    }
    
    @Test
    void addHoliday_shouldReturnOne_whenInputCorrectHolidayData() {
        assertEquals(1, holidayDAOImpl.addHoliday(futureHoliday));
    }
    
    @Test
    void deleteHoliday_shouldReturnOne_whenInputHolidayIdAfterNow() {
        holidayDAOImpl.addHoliday(futureHoliday);
        int lastId = (int)holidayDAOImpl.findAllHolidays().get().stream().count();        
        assertEquals(1, holidayDAOImpl.deleteHoliday(lastId));
    }
    @Test
    void deleteHoliday_shouldReturnZero_whenInputHolidayIdBeforeNow() {
        holidayDAOImpl.addHoliday(pastHoliday);
        int lastId = (int)holidayDAOImpl.findAllHolidays().get().stream().count();        
        assertEquals(0, holidayDAOImpl.deleteHoliday(lastId));
    }
    @Test
    void deleteHoliday_shouldReturnZero_whenInputIncorrectId() {       
        assertEquals(0, holidayDAOImpl.deleteHoliday(-1));
    }
    @Test
    void findAllHolidays_shouldReturnCountOfHolidays_whenCallTheMethod() {
        assertEquals(8, (int)holidayDAOImpl.findAllHolidays().get().stream().count());
    }
    
    @Test
    void findAllHolidays_shouldReturnCountOfHolidays_whenCallTheMethod2() {
        Holiday result = new Holiday();
        result.setId(1);
        result.setName("NEW YEAR");
        result.setDate(LocalDate.of(2022, 01, 01));
        assertEquals(result, holidayDAOImpl.findAllHolidays().get().stream().limit(1).collect(Collectors.toList()).get(0));
    }

}
