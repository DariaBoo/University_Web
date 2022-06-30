package ua.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.foxminded.university.service.implementation.GroupServiceImpl;
import ua.foxminded.university.service.implementation.LessonServiceImpl;
import ua.foxminded.university.service.implementation.TeacherServiceImpl;

@Controller
@RequestMapping("/teachers")
public class TeachersController {
    private final TeacherServiceImpl teacherServiceImpl;
    private final GroupServiceImpl groupServiceImpl;
    private final LessonServiceImpl lessonServiceImpl;

    @Autowired
    public TeachersController(TeacherServiceImpl teacherServiceImpl, GroupServiceImpl groupServiceImpl,
            LessonServiceImpl lessonServiceImpl) {
        this.teacherServiceImpl = teacherServiceImpl;
        this.groupServiceImpl = groupServiceImpl;
        this.lessonServiceImpl = lessonServiceImpl;
    }

    @GetMapping
    public String listAllTeachers(Model model) {
        model.addAttribute("teachers", teacherServiceImpl.findAllTeachers());
        return "teachers/list";
    }

    @RequestMapping("/{id}")
    public String viewTeacherById(@PathVariable Integer id, Model model) {
        model.addAttribute("teacher", teacherServiceImpl.findByID(id));
        model.addAttribute("groups", groupServiceImpl.findGroupsByTeacherId(id));
        model.addAttribute("lessons", lessonServiceImpl.findLessonsByTeacherId(id));
        model.addAttribute("absentDays", teacherServiceImpl.showTeacherAbsent(id));
        return "teachers/view";
    }

}
