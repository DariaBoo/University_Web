package ua.foxminded.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ua.foxminded.university.controller.urls.URL;

@Controller
public class UniversityController {
    
    @GetMapping(URL.APP_HOME)
    public String showHomePage() {
        return "/home";
    }    
    
    @GetMapping(URL.HOME_STUDENT)
    public String viewStudentPage() {
        return "students/studentPage";
    }
    
    @GetMapping(URL.HOME_TEACHER)
    public String viewTeacherPage() {
        return "teachers/teacherPage";
    }
}
