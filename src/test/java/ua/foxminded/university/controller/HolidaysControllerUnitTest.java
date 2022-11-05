package ua.foxminded.university.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.urls.URL;
import ua.foxminded.university.service.HolidayService;
import ua.foxminded.university.service.entities.Holiday;
import ua.foxminded.university.service.exception.EntityConstraintViolationException;

@SpringBootTest(classes = { AppSpringBoot.class })
@TestInstance(Lifecycle.PER_CLASS)
class HolidaysControllerUnitTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private HolidaysController holidaysController;
    private MockMvc mockMvc;
    @MockBean
    private HolidayService holidayService;
    @MockBean
    private BindingResult bindingResult;
    @Mock
    private Model model;
    @Mock
    private RedirectAttributes redirectAtt;
    private Holiday holiday;

    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        holiday = Holiday.builder().id(1).name("holiday").date(LocalDate.now()).build();
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void listAllHolidays_shouldReturnStatus200() throws Exception {
        when(holidayService.findAllHolidays()).thenReturn(new ArrayList<>());
        mockMvc.perform(get(URL.APP_HOLIDAYS).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        assertEquals("holidays/list", holidaysController.listAllHolidays(model));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void deleteHolidayById_shouldReturnStatus200_whenDeleteHolidayReturnTrue() throws Exception {
        doNothing().when(holidayService).deleteHoliday(1);
        mockMvc.perform(delete(URL.APP_DELETE_HOLIDAY_BY_ID, 1).contentType(MediaType.APPLICATION_JSON)
                .content("{redirectAtt}")).andExpect(status().is3xxRedirection());
        assertEquals("redirect:/app/holidays", holidaysController.deleteHolidayById(1, redirectAtt));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void deleteHolidayById_shouldReturnStatus200_whenDeleteHolidayReturnFalse() throws Exception {
        doNothing().when(holidayService).deleteHoliday(1);
        mockMvc.perform(delete(URL.APP_DELETE_HOLIDAY_BY_ID, 1).contentType(MediaType.APPLICATION_JSON)
                .content("{redirectAtt}")).andExpect(status().is3xxRedirection());
        assertEquals("redirect:/app/holidays", holidaysController.deleteHolidayById(1, redirectAtt));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void createNewHoliday() {
        assertEquals("holidays/new", holidaysController.createNewHoliday(new Holiday()));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void saveHoliday() throws Exception {
        given(bindingResult.hasErrors()).willReturn(false);
        when(holidayService.addHoliday(holiday)).thenReturn(holiday);
        mockMvc.perform(post(URL.APP_HOLIDAYS, holiday)).andExpect(status().is3xxRedirection());
        assertEquals("redirect:/app/holidays",
                holidaysController.saveHoliday(new Holiday(), bindingResult, redirectAtt));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void saveHoliday_whenEntityIsNotUnique() throws Exception {
        given(bindingResult.hasErrors()).willReturn(false);
        when(holidayService.addHoliday(holiday)).thenThrow(EntityConstraintViolationException.class);
        mockMvc.perform(post(URL.APP_HOLIDAYS, holiday)).andExpect(status().is3xxRedirection());
        assertEquals("redirect:/app/holidays",
                holidaysController.saveHoliday(new Holiday(), bindingResult, redirectAtt));
    }
}
