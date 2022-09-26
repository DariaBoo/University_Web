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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.university.controller.urls.URL;
import ua.foxminded.university.service.SecurityService;
import ua.foxminded.university.service.dto.AuthenticationDto;

@Controller
public class SecurityController {
    
    @Autowired
    private SecurityService securityService;    
    
    
    @GetMapping(URL.WELCOME)
    public String showLoginPage() {
        return "login";
    }
    
    @PostMapping(URL.LOGIN)
    public String login(AuthenticationDto userDto, RedirectAttributes redirectAtt) {
        System.out.println("CONTROLLER STARTS-----------------------------------------------" + userDto.getUsername() + userDto.getPassword());
        boolean response = securityService.login(userDto.getUsername(), userDto.getPassword());
        if(response) {
            System.out.println("CONTROLLER REDIRECT TO /dashboard---------------------------------");
            redirectAtt.addFlashAttribute("username", userDto.getUsername());
            return "redirect:/dashboard";
        }
        return "redirect:/login_error";
    }
    
    @GetMapping(URL.LOGOUT)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("ADMIN LOGOUT METHOD------------------------" + auth);
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }
    
    @RequestMapping(URL.LOGOUT_ERROR)
    public String errorLogin(RedirectAttributes redirectAtt) {
        System.out.println("CONTROLLER ERROR MESSAGE-------------------------------");
        redirectAtt.addFlashAttribute("message", "You have no authority to access");
        return "login";
    }
    
//    @PostMapping("/student/login")
//    public String loginStudent(AuthenticationDto userDto) {
//        System.out.println("CONTROLLER START---------------------------------------------");
//        boolean response = securityService.login(userDto.getUsername(), userDto.getPassword());
//        if(response) {
//            return "redirect:/student";
//        }
//        return "user_page";
//    }    
    
//    @PostMapping("/teacher/login")
//    public String loginTeacher(AuthenticationDto userDto) {        
//        boolean response = securityService.login(userDto.getUsername(), userDto.getPassword());
//        if(response) {
//            return "redirect:/teacher";
//        }
//        return "user_page";
//    }
    
//    @GetMapping("/user/logout")
//    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("USER LOGOUT METHOD------------------------" + auth);
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        return "redirect:/";
//    }
    
//    @PostMapping("/admin/login")
//    public String showLoginForm(AuthenticationDto userDto) {   
//        System.out.println("ADMIN CONTROLLER START---------------------------------------------");
//        boolean response = securityService.login(userDto.getUsername(), userDto.getPassword());
//        if(response) {
//            System.out.println("REDIRECT TO HOME---------------------------------------------------");
//            return "redirect:/home";
//        }
//        return "redirect:/admin";
//    }    

//    @GetMapping("/admin/logout")
//    public String logoutAdminPage(HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("ADMIN LOGOUT METHOD------------------------" + auth);
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        return "redirect:/admin";
//    }
    
//    @GetMapping("/login?error")
//    public String showLoginErrorPage() {
//        return "/error_authorisation";
//    }
    
//    @PostMapping(value ="/login")
//    public String login(AuthenticationDto userDto) {
//        System.out.println("CONTROLLER STARTS-----------------------------------------------" + userDto.getUsername() + userDto.getPassword());
//        User authorisedUser = securityService.login(userDto.getUsername(), userDto.getPassword());
//        if(response) {
//            System.out.println("CONTROLLER REDIRECT TO /STUDENT---------------------------------");
//            
//            return "redirect:/student";
//        }
//        return "/error_authorisation";
//    }
}
