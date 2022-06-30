package ua.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.university.service.implementation.GroupServiceImpl;
import ua.foxminded.university.service.implementation.StudentServiceImpl;
import ua.foxminded.university.service.pojo.Student;

@Controller
@RequestMapping("/students")
public class StudentsController {
    
    private final StudentServiceImpl studentServiceImpl;
    private final GroupServiceImpl groupServiceImpl;
    
    @Autowired
    public StudentsController(StudentServiceImpl studentServiceImpl, GroupServiceImpl groupServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
        this.groupServiceImpl = groupServiceImpl;
    }
    
    @GetMapping()
    public String listAllStudents(Model model) {
        model.addAttribute("students", studentServiceImpl.findAllStudents());
        return "students/list";
    }
    
    @RequestMapping("/{id}")
    public String viewStudentById(@PathVariable Integer id, Model model) {
        model.addAttribute("student", studentServiceImpl.findByID(id));
        return "students/view";
    }    
    @GetMapping("/new")
    public String createNewStudent(@ModelAttribute("student") Student student, Model model) {
        model.addAttribute("groups", groupServiceImpl.findAllGroups());
        return "students/new";
    }
    
    @PostMapping()
    public String saveNewStudent(@ModelAttribute("student") Student student, RedirectAttributes redirectAtt) {
        int result = studentServiceImpl.addStudent(student);
        if (result == 0) {
            redirectAtt.addFlashAttribute("message", "Error occured while added a student or student already exists!");
        } else {
            redirectAtt.addFlashAttribute("message", "Student was added!");
        }
        return "redirect:/students";
    }
    
    @RequestMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Integer id, RedirectAttributes redirectAtt) {
        int result = studentServiceImpl.deleteStudent(id);
        if (result == 0) {
            redirectAtt.addFlashAttribute("message", "Error occured while deleted a student!");
        } else {
            redirectAtt.addFlashAttribute("message", "Student was deleted!");
        }
        return "redirect:/students";
    }
    
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("student", studentServiceImpl.findByID(id));
        model.addAttribute("groups", groupServiceImpl.findAllGroups());
        return "students/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("student") Student student, RedirectAttributes redirectAtt) {
        int result = studentServiceImpl.updateStudent(student);
        if (result == 0) {
            redirectAtt.addFlashAttribute("message", "Error occured while updated a student!");
        } else {
            redirectAtt.addFlashAttribute("message", "Student was updated!");
        }
        return "redirect:/students";
    }
}
