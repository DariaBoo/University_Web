package ua.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.StudentService;

@Controller
@RequestMapping("/groups")
public class GroupsController {
    
    @Autowired
    private GroupService groupService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private LessonService lessonService;
    
    @GetMapping
    public String listAllGroups(Model model) {
        model.addAttribute("groups", groupService.findAllGroups());
        return "groups/list";
    }
    
    @RequestMapping("/{id}")
    public String viewGroupById(@PathVariable Integer id, Model model) {
        model.addAttribute("group", groupService.findById(id));
        model.addAttribute("lessons", lessonService.findLessonsByGroupId(id));
        model.addAttribute("students", studentService.findStudentsByGroup(id));
        return "groups/view";
    }
}
