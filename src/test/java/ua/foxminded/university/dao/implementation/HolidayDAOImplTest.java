package ua.foxminded.university.dao.implementation;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import ua.foxminded.university.config.HibernateConfigTest;
import ua.foxminded.university.dao.HolidayDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.entities.Holiday;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateConfigTest.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
class HolidayDAOImplTest {
    @Autowired
    private HolidayDAO holidayDAO;

    private Holiday futureHoliday = new Holiday();
    private Holiday pastHoliday = new Holiday();
    private Holiday uniqueDate = new Holiday();
    
    @BeforeEach
    void init() {
        new EmbeddedDatabaseBuilder().setName("test").setType(EmbeddedDatabaseType.H2)
        .addScript("classpath:tablesTest.sql").build();  
        uniqueDate.setDate(LocalDate.now());
        uniqueDate.setName("HolidayTest");
        futureHoliday.setDate(LocalDate.now().plusDays(1));
        futureHoliday.setName("Holiday");
        pastHoliday.setDate(LocalDate.now().minusDays(1));
        pastHoliday.setName("Holiday");
    }
    
    @Test
    void addHoliday_shouldReturnOne_whenInputCorrectHolidayData() {    
        assertEquals(holidayDAO.findAllHolidays().get().size() + 1, holidayDAO.addHoliday(uniqueDate));
    }
    @Test
    void addHoliday_shouldThrowDAOException_whenInputHolidayWithNotUniqueDate() {
        holidayDAO.addHoliday(uniqueDate);
        Holiday notUniqueDate = new Holiday();
        notUniqueDate.setDate(LocalDate.now());
        notUniqueDate.setName("HolidayTest");
        assertThrows(DAOException.class, () -> holidayDAO.addHoliday(notUniqueDate));
    }
    
    @Test
    void deleteHoliday_shouldReturnTrue_whenInputHolidayIdAfterNow() {
        int id = holidayDAO.addHoliday(futureHoliday);       
        boolean isTrue = holidayDAO.deleteHoliday(id);
        assertTrue(isTrue);
    }
    @Test
    void deleteHoliday_shouldReturnFalse_whenInputHolidayIdBeforeNow() {
        int id = holidayDAO.addHoliday(pastHoliday);       
        assertFalse(holidayDAO.deleteHoliday(id));
    }
    @ParameterizedTest(name = "{index}. When input not existed id or negative number or zero will return false.")
    @ValueSource(ints = { 1000, -1, 0 })
    void deleteHoliday_shouldReturnZero_whenInputIncorrectId(int incorrectId) {       
        assertFalse(holidayDAO.deleteHoliday(incorrectId));
    }
    @Test
    void findAllHolidays_shouldReturnCountOfHolidays_whenCallTheMethod() {
        assertEquals(7, (int)holidayDAO.findAllHolidays().get().stream().count());
    }
    
    @Test
    void findAllHolidays_shouldReturnCountOfHolidays_whenCallTheMethod2() {
        Holiday result = new Holiday();
        result.setId(1);
        result.setName("NEW YEAR");
        result.setDate(LocalDate.of(2022, 01, 01));
        assertEquals(result, holidayDAO.findAllHolidays().get().stream().limit(1).collect(Collectors.toList()).get(0));
    }
}
