package ua.foxminded.university.controller;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.controller.urls.URL;
import ua.foxminded.university.controller.validator.ValidationUtils;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.exception.EntityConstraintViolationException;

@Slf4j
@Controller
public class StudentsController {

    private static final String message = "message";
    private static final String students = "redirect:/app/students";

    @Autowired
    private StudentService studentService;
    @Autowired
    private GroupService groupService;

    @GetMapping(URL.APP_STUDENTS)
    public String listAllStudents(Model model) {
        model.addAttribute("students", studentService.findAllStudents());
        return "students/list";
    }

    @RequestMapping(URL.APP_STUDENTS_VIEW_BY_ID)
    public String viewStudentById(@PathVariable Integer id, Model model) {
        model.addAttribute("student", studentService.findById(id));
        return "students/view";
    }

    @GetMapping(URL.APP_NEW_STUDENT)
    public String createNewStudent(@ModelAttribute("student") Student student, Model model) {
        model.addAttribute("groups", groupService.findAllGroups());
        return "students/new";
    }

    @PostMapping(URL.APP_STUDENTS)
    public String saveNewStudent(@Valid @ModelAttribute("student") Student student, BindingResult bindingResult,
            RedirectAttributes redirectAtt) {
        if (bindingResult.hasErrors()) {
            redirectAtt.addFlashAttribute(message, ValidationUtils.getErrorMessages(bindingResult));
        } else {
            try {
                studentService.addStudent(student);
                redirectAtt.addFlashAttribute(message, "Student was created!");
            } catch (EntityConstraintViolationException e) {
                log.error(e.getMessage());
                redirectAtt.addFlashAttribute(message, e.getMessage());
            }
        }
        return students;
    }

    @RequestMapping(URL.APP_DELETE_STUDENT_BY_ID)
    public String deleteStudent(@PathVariable Integer id, RedirectAttributes redirectAtt) {
        try {
            studentService.deleteStudent(id);
            redirectAtt.addFlashAttribute(message, "Student was deleted!");
        } catch (EntityNotFoundException ex) {
            redirectAtt.addFlashAttribute(message, ex.getLocalizedMessage());
        }
        return students;
    }

    @GetMapping(URL.APP_EDIT_STUDENT_BY_ID)
    public String edit(@PathVariable("id") int id, Model model, RedirectAttributes redirectAtt) {
        try {
            model.addAttribute("student", studentService.findById(id));
        } catch (IllegalArgumentException ex) {
            redirectAtt.addFlashAttribute(message, ex.getLocalizedMessage());
        }
        model.addAttribute("groups", groupService.findAllGroups());
        return "students/edit";
    }

    @PostMapping(URL.APP_STUDENTS_VIEW_BY_ID)
    public String update(@Valid @ModelAttribute("student") Student student, BindingResult bindingResult,
            RedirectAttributes redirectAtt) {
        if (bindingResult.hasErrors()) {
            redirectAtt.addFlashAttribute(message, ValidationUtils.getErrorMessages(bindingResult));
        } else {
            studentService.updateStudent(student);
            redirectAtt.addFlashAttribute(message, "Student was updated!");
        }
        return students;
    }

    @PostMapping(URL.STUDENT_CHANGE_PASSWORD)
    public ResponseEntity<String> changePassword(@PathVariable int id, @RequestParam String newPassword) {
        studentService.changePassword(id, newPassword);
        return new ResponseEntity<>("Password was changed successfully!", HttpStatus.OK);
    }
}
