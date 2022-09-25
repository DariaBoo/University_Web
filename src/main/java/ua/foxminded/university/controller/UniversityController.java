package ua.foxminded.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UniversityController {

    @GetMapping("/welcome")
    public String showWelcomePage() {
        return "/user_page";
    }
            
    @GetMapping("/admin")
    public String showAdminLoginPage() {
        return "/index";
    }
    
    @GetMapping("/home")
    public String showHomePage() {
        return "/home";
    }      
}
