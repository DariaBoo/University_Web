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
@RequestMapping("/lessons")
public class LessonsController {
    
    @Autowired
    private LessonService lessonService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private GroupService groupService;

    @GetMapping()
    public String listAllLessons(Model model) {
        model.addAttribute("lessons", lessonService.findAllLessons());
        return "lessons/list";
    }

    @RequestMapping("/{id}")
    public String viewLessonById(@PathVariable Integer id, Model model) {
        model.addAttribute("lesson", lessonService.findByID(id));
        model.addAttribute("teachers", teacherService.findTeachersByLessonId(id));
        model.addAttribute("groups", groupService.findGroupsByLessonId(id));
        return "lessons/view";
    }
}
