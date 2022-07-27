package ua.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.university.service.HolidayService;
import ua.foxminded.university.service.entities.Holiday;
import ua.foxminded.university.service.exception.ServiceException;

@Controller
@RequestMapping("/holidays")
public class HolidaysController {

    private String message = "message";

    @Autowired
    private HolidayService holidayService;

    @GetMapping()
    public String listAllHolidays(Model model) {
        model.addAttribute("holidays", holidayService.findAllHolidays());
        return "holidays/list";
    }

    @RequestMapping("/holiday/delete/{id}")
    public String deleteHolidayById(@PathVariable Integer id, RedirectAttributes redirectAtt) {
        if (holidayService.deleteHoliday(id)) {
            redirectAtt.addFlashAttribute(message, "Holiday was deleted!");
        } else {
            redirectAtt.addFlashAttribute(message, "Can't delete past holiday!");
        }
        return "redirect:/holidays";
    }

    @GetMapping("/new")
    public String createNewHoliday(@ModelAttribute("holiday") Holiday holiday) {
        return "holidays/new";
    }

    @RequestMapping("/timetable")
    public String chooseDatePeriod() {
        return "holidays/list";
    }

    @PostMapping()
    public String saveHoliday(@ModelAttribute("holiday") Holiday holiday, RedirectAttributes redirectAtt) {
        try {
            if (holidayService.addHoliday(holiday)) {
                redirectAtt.addFlashAttribute(message, "Holiday was added!");
            } else {
                redirectAtt.addFlashAttribute(message, "Error to add holiday [ " + holiday.getName() + "]!");
            }
        } catch (ServiceException e) {
            redirectAtt.addFlashAttribute(message, e.getMessage());
        }
        return "redirect:/holidays";
    }
}
