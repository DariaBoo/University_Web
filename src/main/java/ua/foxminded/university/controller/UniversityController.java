package ua.foxminded.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ua.foxminded.university.controller.urls.URL;

@Controller
public class UniversityController {

//    @GetMapping("/welcome")
//    public String showWelcomePage() {
//        return "/user_page";
//    }
            
//    @GetMapping("/admin")
//    public String showAdminLoginPage() {
//        return "/index";
//    }
    
    @GetMapping(URL.APP_HOME)
    public String showHomePage() {
        return "/home";
    }    
    
    @GetMapping(URL.HOME)
    public String showDashboardPage() {
        return "/dashboard";
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
