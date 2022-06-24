package ua.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.foxminded.university.service.implementation.StudentServiceImpl;

@Controller
@RequestMapping("/students")
public class StudentsController {
    
    private final StudentServiceImpl studentServiceImpl;
    
    @Autowired
    public StudentsController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }
    
    @GetMapping()
    public String list(Model model) {
        model.addAttribute("students", studentServiceImpl.findAllStudents());
        return "students/list";
    }
    
    @RequestMapping("/{id}")
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("student", studentServiceImpl.findByID(id));
        return "students/view";
    }    
  
}
