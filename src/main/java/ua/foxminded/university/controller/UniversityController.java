package ua.foxminded.university.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.foxminded.university.service.implementation.StudentServiceImpl;
import ua.foxminded.university.service.implementation.TeacherServiceImpl;

@Controller
public class UniversityController {
    private final StudentServiceImpl studentServiceImpl;
    private final TeacherServiceImpl teacherServiceImpl;
    
    @Autowired
    public UniversityController(StudentServiceImpl studentServiceImpl, TeacherServiceImpl teacherServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
        this.teacherServiceImpl = teacherServiceImpl;
    }
    
    @GetMapping("/")
    public String showWelcomePage() {
        return "/index";
    }
    
    @GetMapping("/home")
    public String showAdminPage() {
        return "/home";
    }

    @RequestMapping("/login")
    public String showLoginForm(HttpServletRequest request, Model model) {       
        String userName=request.getParameter("uname");  
        String password=request.getParameter("psw");
        String message;
        if(userName != null && 
                !userName.equals("") 
                && userName.equals("admin") && 
                password != null && 
                !password.equals("") && 
                password.equals("admin")){
            message = "Welcome " +userName + ".";
            return "/home";  
        }else if(userName != null && 
                !userName.equals("") 
                && userName.matches("[a-zA-Z]+#\\d+") && 
                password != null && 
                !password.equals("") && 
                password.equals("555")) {
            int id = Integer.parseInt(userName.split("#")[1]);
            TimetableController.teacherId = id;
            model.addAttribute("teacher", teacherServiceImpl.findByID(id));
            return "teachers/teacherPage"; 
        }else if(userName != null && 
                !userName.equals("") 
                && userName.matches("[a-zA-Z]+_\\d+") && 
                password != null && 
                !password.equals("") && 
                password.equals("1234")) {
            int id = Integer.parseInt(userName.split("_")[1]);
            TimetableController.studentId = id;
            model.addAttribute("student", studentServiceImpl.findByID(id));
            return "students/studentPage"; 
        }else{
            message = "Wrong username or password!";
            model.addAttribute("message", message);
            return "welcome";
        }
     }
      
}
