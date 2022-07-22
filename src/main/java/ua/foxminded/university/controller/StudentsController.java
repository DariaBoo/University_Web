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

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.exception.ServiceException;

@Slf4j
@Controller
@RequestMapping("/students")
public class StudentsController {

    private static final String message = "message";
    private static final String students = "redirect:/students";

    @Autowired
    private StudentService studentService;
    @Autowired
    private GroupService groupService;

    @GetMapping()
    public String listAllStudents(Model model) {
            model.addAttribute("students", studentService.findAllStudents());
        return "students/list";
    }

    @RequestMapping("/{id}")
    public String viewStudentById(@PathVariable Integer id, Model model) {
        model.addAttribute("student", studentService.findByID(id));
        return "students/view";
    }

    @GetMapping("/new")
    public String createNewStudent(@ModelAttribute("student") Student student, Model model) {
        model.addAttribute("groups", groupService.findAllGroups());
        return "students/new";
    }

    @PostMapping()
    public String saveNewStudent(@ModelAttribute("student") Student student, RedirectAttributes redirectAtt) {
        try {
            int studentID = studentService.addStudent(student);
            redirectAtt.addFlashAttribute(message, "Student was added with id " + studentID + "!");
        } catch (ServiceException e) {
            log.error(e.getMessage());
            redirectAtt.addFlashAttribute(message, e.getMessage());
        }
        return students;
    }

    @RequestMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Integer id, RedirectAttributes redirectAtt) {
        boolean isDeleted = studentService.deleteStudent(id);
        if(isDeleted) {
            redirectAtt.addFlashAttribute(message, "Student was deleted!");
        } else {
            redirectAtt.addFlashAttribute(message, "Error to delete!");
        }
        return students;
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") int id, RedirectAttributes redirectAtt) {
            model.addAttribute("student", studentService.findByID(id));
            model.addAttribute("groups", groupService.findAllGroups());
        return "students/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("student") Student student, RedirectAttributes redirectAtt) {
        try {
            studentService.updateStudent(student);
            redirectAtt.addFlashAttribute(message, "Student was updated!");
        } catch (ServiceException e) {
            log.error(e.getMessage());
            redirectAtt.addFlashAttribute(message, e.getMessage());
        }
        return students;
    }
}
