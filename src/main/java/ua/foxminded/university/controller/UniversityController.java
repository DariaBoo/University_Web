package ua.foxminded.university.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ua.foxminded.university.security.AuthorisedUserDetailsService;
import ua.foxminded.university.security.model.AuthorisedUser;
import ua.foxminded.university.service.dto.AuthenticationDto;
import ua.foxminded.university.service.entities.Staff;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.Teacher;

@Controller
public class UniversityController {

    @Autowired
    private AuthorisedUserDetailsService userDetailsService;

    @GetMapping("/")
    public String showWelcomePage() {
        return "/index";
    }

    @GetMapping("/home")
    public String showAdminPage() {
        return "/home";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestBody AuthenticationDto dto, Model model) {
        String username = dto.getUsername();
        AuthorisedUser user = (AuthorisedUser) userDetailsService.loadUserByUsername(username);
        String message = "";
        if(user.getUser() instanceof Student) {
            model.addAttribute("student", user);
            return "students/studentPage";
        } else if(user.getUser() instanceof Teacher) {
            model.addAttribute("teacher", user);
            return "teachers/teacherPage";
        } else if(user.getUser() instanceof Staff) {
            message = "Welcome " + username + ".";
            return "/home";
        } else {
            message = "Wrong username or password!";
            model.addAttribute("message", message);
            return "index";
        }
    }

    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }
}
