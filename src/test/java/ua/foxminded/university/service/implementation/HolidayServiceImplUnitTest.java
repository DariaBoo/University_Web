package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.foxminded.university.dao.HolidayDAO;
import ua.foxminded.university.service.entities.Holiday;
import ua.foxminded.university.service.exception.EntityConstraintViolationException;

@ExtendWith(SpringExtension.class)
class HolidayServiceImplUnitTest {

    @Mock
    private HolidayDAO holidayDao;

    @InjectMocks
    private HolidayServiceImpl holidayService;

    private Holiday holiday;
    private Holiday holiday2;

    @BeforeEach
    void setup() {
        holiday = Holiday.builder().id(1).date(LocalDate.now()).name("holiday").build();
        holiday2 = Holiday.builder().id(2).date(LocalDate.now().plusDays(1L)).name("holiday").build();
    }

    @Test
    void addHoliday_shouldReturnAddedHoliday_whenAddNewHoliday() {
        given(holidayDao.save(holiday)).willReturn(holiday);
        given(holidayDao.existsById(holiday.getId())).willReturn(true);
        assertEquals(holiday, holidayService.addHoliday(holiday));
        verify(holidayDao, times(1)).save(any(Holiday.class));
    }

    @Test
    void addHoliday_shouldThrowUniqueConstraintViolationException_whenInputNotUniqueDate() {
        given(holidayDao.findByDate(holiday.getDate())).willReturn(Optional.of(holiday));
        assertThrows(EntityConstraintViolationException.class, () -> holidayService.addHoliday(holiday));
        verify(holidayDao, times(0)).save(any(Holiday.class));
    }

    @Test
    void deleteHoliday() {
        int holidayId = 1;
        given(holidayDao.existsById(holidayId)).willReturn(true);
        willDoNothing().given(holidayDao).deleteById(holidayId);
        holidayService.deleteHoliday(holidayId);
        verify(holidayDao, times(1)).deleteById(holidayId);
    }

    @Test
    void findAllHolidays_shouldReturnCountOfHolidays_whenCallTheMethod() {
        List<Holiday> holidays = new ArrayList<>();
        holidays.add(holiday);
        holidays.add(holiday2);
        given(holidayDao.findAll()).willReturn(holidays);

        List<Holiday> holidayList = holidayService.findAllHolidays();
        assertNotNull(holidayList);
        assertEquals(2, holidayList.size());
    }

    @Test
    void findAllHolidays_shouldReturnEmptyList_whenCallTheMethod() {
        given(holidayDao.findAll()).willReturn(new ArrayList<Holiday>());
        List<Holiday> holidayList = holidayService.findAllHolidays();
        assertEquals(0, holidayList.size());
    }
}
