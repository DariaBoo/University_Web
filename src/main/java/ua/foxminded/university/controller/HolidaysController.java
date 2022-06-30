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

import ua.foxminded.university.service.implementation.HolidayServiceImpl;
import ua.foxminded.university.service.pojo.Holiday;

@Controller
@RequestMapping("/holidays")
public class HolidaysController {
    private final HolidayServiceImpl holidayServiceImpl;

    @Autowired
    public HolidaysController(HolidayServiceImpl holidayServiceImpl) {
        this.holidayServiceImpl = holidayServiceImpl;
    }

    @GetMapping()
    public String listAllHolidays(Model model) {
        model.addAttribute("holidays", holidayServiceImpl.findAllHolidays());
        return "holidays/list";
    }

    @RequestMapping("/holiday/delete/{id}")
    public String deleteHolidayById(@PathVariable Integer id, RedirectAttributes redirectAtt) {
        int result = holidayServiceImpl.deleteHoliday(id);
        if (result == 0) {
            redirectAtt.addFlashAttribute("message", "Can't delete past holiday!");
        } else {
            redirectAtt.addFlashAttribute("message", "Holiday was deleted!");
        }
        return "redirect:/holidays";
    }
    @GetMapping("/new")
    public String createNewHoliday(@ModelAttribute("holiday") Holiday holiday) {
        return "holidays/new";
    }
    @RequestMapping("/timetable")
    public String chooseDatePeriod() {     
        return "timetable/index";
    }
    @PostMapping()
    public String saveHoliday(@ModelAttribute("holiday") Holiday holiday, RedirectAttributes redirectAtt) {
        int result = holidayServiceImpl.addHoliday(holiday);
        if(result == 0) {
            redirectAtt.addFlashAttribute("message", "Error occured while added a holiday!");
        } else {
            redirectAtt.addFlashAttribute("message", "Holiday was added!");
        }
        return "redirect:/timetable";
    }  
}
