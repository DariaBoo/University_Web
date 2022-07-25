package ua.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeachersController {
    
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private LessonService lessonService;

    @GetMapping
    public String listAllTeachers(Model model) {
        model.addAttribute("teachers", teacherService.findAllTeachers());
        return "teachers/list";
    }

    @RequestMapping("/{id}")
    public String viewTeacherById(@PathVariable Integer id, Model model) {
        model.addAttribute("teacher", teacherService.findByID(id));
        model.addAttribute("groups", groupService.findGroupsByTeacherId(id));
        model.addAttribute("lessons", lessonService.findLessonsByTeacherId(id));
        model.addAttribute("absentDays", teacherService.showTeacherAbsent(id));
        return "teachers/view";
    }
}
