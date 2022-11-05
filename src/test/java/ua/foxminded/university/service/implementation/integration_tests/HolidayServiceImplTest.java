package ua.foxminded.university.service.implementation.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.service.HolidayService;
import ua.foxminded.university.service.entities.Holiday;
import ua.foxminded.university.service.exception.EntityConstraintViolationException;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/holidays.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class HolidayServiceImplTest {

    @Autowired
    private HolidayService holidayService;
    private Holiday holiday;
    private Holiday holiday2;

    @BeforeEach
    void setup() {
        holiday = Holiday.builder().name("holiday").date(LocalDate.of(2022, 10, 31)).build();
    }

    @Test
    void addHoliday_shouldReturnSavedHoliday_whenInputNewHoliday() {
        assertEquals(holiday, holidayService.addHoliday(holiday));
    }

    @Test
    void addHoliday_shouldThrowUniqueConstraintViolationException_whenInputExistedName() {
        holidayService.addHoliday(holiday);
        holiday2 = Holiday.builder().name("holiday").date(LocalDate.of(2022, 10, 31)).build();
        assertThrows(EntityConstraintViolationException.class, () -> holidayService.addHoliday(holiday2));
    }

    @Test
    void deleteHoliday_shouldReturnTrue_whenInputExistedId() {
        holidayService.addHoliday(holiday);
        holidayService.deleteHoliday(holiday.getId());
        assertEquals(0, holidayService.findAllHolidays().size());
    }

    @Test
    void deleteHoliday_shouldThrowEntityNotFoundException_whenInputNotExistedId() {
        int notExistedId = holidayService.findAllHolidays().size();
        assertThrows(EntityNotFoundException.class, () -> holidayService.deleteHoliday(notExistedId));
    }

    @Test
    void findAllHolidays_shouldReturnList() {
        holidayService.addHoliday(holiday);
        holiday2 = Holiday.builder().name("holiday2").date(LocalDate.of(2022, 10, 30)).build();
        holidayService.addHoliday(holiday2);
        List<Holiday> holidays = new ArrayList<>();
        holidays.add(holiday);
        holidays.add(holiday2);
        assertEquals(holidays.size(), holidayService.findAllHolidays().size());
    }
}
