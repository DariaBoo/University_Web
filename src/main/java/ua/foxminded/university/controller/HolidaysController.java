package ua.foxminded.university.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.university.controller.urls.URL;
import ua.foxminded.university.controller.validator.ValidationUtils;
import ua.foxminded.university.service.HolidayService;
import ua.foxminded.university.service.entities.Holiday;
import ua.foxminded.university.service.exception.UniqueConstraintViolationException;

@Controller
public class HolidaysController {

    private String message = "message";

    @Autowired
    private HolidayService holidayService;

    @GetMapping(URL.APP_HOLIDAYS)
    public String listAllHolidays(Model model) {
        model.addAttribute("holidays", holidayService.findAllHolidays());
        return "holidays/list";
    }

    @RequestMapping(URL.APP_DELETE_HOLIDAY_BY_ID)
    public String deleteHolidayById(@PathVariable Integer id, RedirectAttributes redirectAtt) {
        if (holidayService.deleteHoliday(id)) {
            redirectAtt.addFlashAttribute(message, "Holiday was deleted!");
        } else {
            redirectAtt.addFlashAttribute(message, "Can't delete past holiday!");
        }
        return "redirect:/app/holidays";
    }

    @GetMapping(URL.APP_NEW_HOLIDAY)
    public String createNewHoliday(@ModelAttribute("holiday") Holiday holiday) {
        return "holidays/new";
    }

    @PostMapping(URL.APP_HOLIDAYS)
    public String saveHoliday(@Valid @ModelAttribute("holiday") Holiday holiday, BindingResult bindingResult, RedirectAttributes redirectAtt) {
        if(bindingResult.hasErrors()) {
            String errors = ValidationUtils.getErrorMessages(bindingResult);          
            redirectAtt.addFlashAttribute("message", errors);
        } else {
            try {
            holidayService.addHoliday(holiday);
            redirectAtt.addFlashAttribute(message, "Holiday was added successfully!");
            } catch(UniqueConstraintViolationException e) {
                redirectAtt.addFlashAttribute(message, e.getMessage());
            }
        }
        return "redirect:/app/holidays";
    }
}
