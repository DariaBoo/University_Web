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
@RequestMapping("/lessons")
public class LessonsController {
    private final LessonServiceImpl lessonServiceImpl;
    private final TeacherServiceImpl teacherServiceImpl;
    private final GroupServiceImpl groupServiceImpl;

    @Autowired
    public LessonsController(LessonServiceImpl lessonServiceImpl, TeacherServiceImpl teacherServiceImpl,
            GroupServiceImpl groupServiceImpl) {
        this.lessonServiceImpl = lessonServiceImpl;
        this.teacherServiceImpl = teacherServiceImpl;
        this.groupServiceImpl = groupServiceImpl;
    }

    @GetMapping()
    public String listAllLessons(Model model) {
        model.addAttribute("lessons", lessonServiceImpl.findAllLessons());
        return "lessons/list";
    }

    @RequestMapping("/{id}")
    public String viewLessonById(@PathVariable Integer id, Model model) {
        model.addAttribute("lesson", lessonServiceImpl.findByID(id));
        model.addAttribute("teachers", teacherServiceImpl.findTeachersByLessonId(id));
        model.addAttribute("groups", groupServiceImpl.findGroupsByLessonId(id));
        return "lessons/view";
    }

}
