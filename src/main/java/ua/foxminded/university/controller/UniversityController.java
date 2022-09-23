package ua.foxminded.university.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UniversityController {

    @GetMapping("/welcome")
    public String showWelcomePage() {
        return "/user_page";
    }
    
    @PostMapping("/welcome/student/login")
    public String loginStudent(@RequestParam String username, @RequestParam String password) {//@RequestBody AuthenticationDto dto
        return "students/studentPage";
    }
    
    @PostMapping("/welcome/teacher/login")
    public String loginTeacher(@RequestParam String username, @RequestParam String password) {        
        return "teachers/teacherPage";
    }

    @GetMapping("/user/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("USER LOGOUT METHOD------------------------" + auth);
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/welcome";
    }
    
    @GetMapping("/admin")
    public String showAdminLoginPage() {
        return "/index";
    }
    
    @PostMapping("/admin/login")
    public String showLoginForm(@RequestParam String username, @RequestParam String password) {        
            return "/home";
    }    
    
    @GetMapping("/home")
    public String showHomePage() {
        return "/home";
    }

    @GetMapping("/admin/logout")
    public String logoutAdminPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("ADMIN LOGOUT METHOD------------------------" + auth);
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/admin";
    }

}
