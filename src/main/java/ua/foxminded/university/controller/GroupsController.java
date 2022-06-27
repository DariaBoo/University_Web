package ua.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.foxminded.university.service.implementation.GroupServiceImpl;
import ua.foxminded.university.service.implementation.LessonServiceImpl;
import ua.foxminded.university.service.implementation.StudentServiceImpl;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    private final GroupServiceImpl groupServiceImpl;
    private final StudentServiceImpl studentServiceImpl;
    private final LessonServiceImpl lessonServiceImpl;
    
    
    @Autowired
    public GroupsController(GroupServiceImpl groupServiceImpl, StudentServiceImpl studentServiceImpl, LessonServiceImpl lessonServiceImpl) {
        this.groupServiceImpl = groupServiceImpl;
        this.studentServiceImpl = studentServiceImpl;
        this.lessonServiceImpl = lessonServiceImpl;
    }
    
    @GetMapping
    public String listAllGroups(Model model) {
        model.addAttribute("groups", groupServiceImpl.findAllGroups());
        return "groups/list";
    }
    
    @RequestMapping("/{id}")
    public String viewGroupById(@PathVariable Integer id, Model model) {
        model.addAttribute("group", groupServiceImpl.findById(id));
        model.addAttribute("lessons", lessonServiceImpl.findLessonsByGroupId(id));
        model.addAttribute("students", studentServiceImpl.findStudentsByGroup(id));
        return "groups/view";
    }
}
