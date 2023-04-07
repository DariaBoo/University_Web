package ua.foxminded.university.controller.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.HolidaysController;
import ua.foxminded.university.service.entities.Holiday;

@SpringBootTest(classes = AppSpringBoot.class)
@Transactional
class HolidaysControllerTest {

    @Autowired
    private HolidaysController holidaysController;
    private Model model = new ConcurrentModel();
    @Mock
    private RedirectAttributes redirectAtt;
    @Mock
    private BindingResult bindingResult;
    private Holiday holiday;
    private Holiday holiday2;

    @BeforeEach
    void setup() {
        holiday = Holiday.builder().name("holiday").date(LocalDate.of(2022, 10, 31)).build();
    }

    @Test
    void listAllHolidays_shouldReturnCorrectPage() {
        assertEquals("holidays/list", holidaysController.listAllHolidays(model));
    }

    @Test
    void deleteHolidayById_shouldReturnCorrectPage() {
        assertEquals("redirect:/app/holidays", holidaysController.deleteHolidayById(1, redirectAtt));
    }

    @Test
    void createNewHoliday_shouldReturnCorrectPage() {
        assertEquals("holidays/new", holidaysController.createNewHoliday(new Holiday()));
    }

    @Test
    void saveHoliday_shouldReturnCorrectPage() {
        assertEquals("redirect:/app/holidays", holidaysController.saveHoliday(holiday, bindingResult, redirectAtt));
    }

    @Test
    void saveHoliday_shouldCatchEntityConstraintViolationException_andReturnCorrectPage() {
        holiday2 = Holiday.builder().name("holiday").date(LocalDate.of(2022, 10, 31)).build();
        holidaysController.saveHoliday(holiday, bindingResult, redirectAtt);
        assertEquals("redirect:/app/holidays", holidaysController.saveHoliday(holiday2, bindingResult, redirectAtt));
    }

    @Test
    void saveHoliday_shouldThrowConstraintViolationException() {
        String blankName = "";
        holiday2 = Holiday.builder().name(blankName).date(LocalDate.of(2022, 10, 31)).build();
        assertThrows(ConstraintViolationException.class,
                () -> holidaysController.saveHoliday(holiday2, bindingResult, redirectAtt));
    }
}
