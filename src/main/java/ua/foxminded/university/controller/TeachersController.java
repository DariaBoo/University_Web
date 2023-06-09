package ua.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.foxminded.university.controller.urls.URL;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.entities.Teacher;

@Controller
public class TeachersController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private GroupService groupService;

    @GetMapping(URL.APP_TEACHERS)
    public String listAllTeachers(Model model) {
        model.addAttribute("teachers", teacherService.findAllTeachers());
        return "teachers/list";
    }

    @RequestMapping(URL.APP_TEACHERS_VIEW_BY_ID)
    public String viewTeacherById(@PathVariable Integer id, Model model) {
        Teacher teacher = teacherService.findById(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("groups", groupService.findGroupsByTeacherId(id));
        model.addAttribute("lessons", teacher.getLessons());
        model.addAttribute("absentDays", teacher.getAbsentPeriod());
        return "teachers/view";
    }
}
