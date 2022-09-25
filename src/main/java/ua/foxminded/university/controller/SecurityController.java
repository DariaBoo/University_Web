package ua.foxminded.university.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.foxminded.university.service.SecurityService;
import ua.foxminded.university.service.dto.AuthenticationDto;

@Controller
public class SecurityController {
    
    @Autowired
    private SecurityService securityService;
    
    @PostMapping("/student/login")
    public String loginStudent(AuthenticationDto userDto) {
        System.out.println("CONTROLLER START---------------------------------------------");
        boolean response = securityService.login(userDto.getUsername(), userDto.getPassword());
        if(response) {
            return "redirect:/student";
        }
        return "user_page";
    }
    
    @GetMapping("/student")
    public String viewStudentPage() {
        return "students/studentPage";
    }
    
    @PostMapping("/teacher/login")
    public String loginTeacher(AuthenticationDto userDto) {        
        boolean response = securityService.login(userDto.getUsername(), userDto.getPassword());
        if(response) {
            return "redirect:/teacher";
        }
        return "user_page";
    }
    
    @GetMapping("/teacher")
    public String viewTeacherPage() {
        return "teachers/teacherPage";
    }
    
    @GetMapping("/user/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("USER LOGOUT METHOD------------------------" + auth);
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "user_page";
    }
    
    @PostMapping("/admin/login")
    public String showLoginForm(AuthenticationDto userDto) {   
        System.out.println("ADMIN CONTROLLER START---------------------------------------------");
        boolean response = securityService.login(userDto.getUsername(), userDto.getPassword());
        if(response) {
            return "redirect:/home";
        }
        return "/index";
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
    
//    @GetMapping("/")
//    public String showLoginPage() {
//        return "login";
//    }
//    
//    @PostMapping(value ="/login")
//    public String login(AuthenticationDto userDto) {
//        System.out.println("CONTROLLER STARTS-----------------------------------------------" + userDto.getUsername() + userDto.getPassword());
//        boolean response = securityService.login(userDto.getUsername(), userDto.getPassword());
//        if(response) {
//            System.out.println("CONTROLLER REDIRECT TO /STUDENT---------------------------------");
//            return "redirect:/student";
//        }
//        return "/error_authorisation";
//    }
//
//    @GetMapping("/student")
//    public String viewStudentPage() {
//        return "students/studentPage";
//    }
}
