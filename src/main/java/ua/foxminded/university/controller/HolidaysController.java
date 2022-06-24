package ua.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.university.service.implementation.HolidayServiceImpl;

@Controller
@RequestMapping("/holidays")
public class HolidaysController {
    private final HolidayServiceImpl holidayServiceImpl;

    @Autowired
    public HolidaysController(HolidayServiceImpl holidayServiceImpl) {
        this.holidayServiceImpl = holidayServiceImpl;
    }

    @GetMapping()
    public String list(Model model) {
        model.addAttribute("holidays", holidayServiceImpl.findAllHolidays());
        return "holidays/list";
    }

    @RequestMapping("/holiday/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAtt) {
        int result = holidayServiceImpl.deleteHoliday(id);
        if (result == 0) {
            redirectAtt.addFlashAttribute("message", "Can't delete past holiday!");
        } else {
            redirectAtt.addFlashAttribute("message", "Holiday was deleted!");
        }
        return "redirect:/holidays";
    }

}
