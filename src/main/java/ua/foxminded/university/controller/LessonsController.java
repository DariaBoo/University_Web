package ua.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.foxminded.university.controller.urls.URL;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.entities.Lesson;

@Controller
public class LessonsController {
    
    @Autowired
    private LessonService lessonService;

    @GetMapping(URL.APP_LESSONS)
    public String listAllLessons(Model model) {
        model.addAttribute("lessons", lessonService.findAllLessons());
        return "lessons/list";
    }

    @RequestMapping(URL.APP_LESSONS_VIEW_BY_ID)
    public String viewLessonById(@PathVariable Integer id, Model model) {
        Lesson lesson = lessonService.findById(id);
        model.addAttribute("lesson", lesson);
        model.addAttribute("teachers", lesson.getTeachers());
        model.addAttribute("groups", lesson.getGroups());
        return "lessons/view";
    }
}
