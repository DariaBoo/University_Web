package ua.foxminded.university.dao.integration_tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.HolidayDAO;
import ua.foxminded.university.service.entities.Holiday;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/holidays.sql" })
class HolidayDAOTest {

    @Autowired
    private HolidayDAO holidayDao;
    private Holiday holiday;

    @BeforeEach
    void setup() {
        holiday = Holiday.builder().name("holiday").date(LocalDate.of(2022, 10, 31)).build();
    }

    @Test
    void save() {
        holidayDao.save(holiday);
        assertEquals(1, holidayDao.findAll().size());
    }

    @Test
    void findById() {
        holidayDao.save(holiday);
        Holiday result = holidayDao.findById(holiday.getId()).get();
        assertEquals(holiday, result);
    }

    @Test
    void findAll() {
        holidayDao.save(holiday);
        List<Holiday> holidays = new ArrayList<>();
        holidays.add(holiday);
        assertEquals(holidays, holidayDao.findAll());
    }

    @Test
    void deleteById() {
        holidayDao.save(holiday);
        holidayDao.deleteById(holiday.getId());
        assertEquals(0, holidayDao.findAll().size());
    }

    @Test
    void deleteById_throwException() {
        int notExistedId = (holidayDao.findAll().size() + 1);
        assertThrows(EmptyResultDataAccessException.class, () -> holidayDao.deleteById(notExistedId));
    }

    @Test
    void existById() {
        holidayDao.save(holiday);
        assertTrue(holidayDao.existsById(holiday.getId()));
    }

    @Test
    void findByDate() {
        holidayDao.save(holiday);
        assertEquals(holiday, holidayDao.findByDate(LocalDate.of(2022, 10, 31)).get());
    }
}
