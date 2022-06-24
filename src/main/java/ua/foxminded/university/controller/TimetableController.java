package ua.foxminded.university.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
 
import ua.foxminded.university.service.implementation.TimetableServiceImpl;
import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.Student;
import ua.foxminded.university.service.pojo.Teacher;

@Controller
public class TimetableController {
    static int studentId;
    static int teacherId;
    private final TimetableServiceImpl timetableServiceImpl;

    public TimetableController(TimetableServiceImpl timetableServiceImpl) {
        this.timetableServiceImpl = timetableServiceImpl;
    }

    @GetMapping("/timetable")
    public String index() {
        return "timetable/index";
    }

    @GetMapping("/timetable/{from}/{to}")
    public String show(@PathVariable String from, @PathVariable String to, Model model) {
        LocalDate dateOne = LocalDate.parse(from);
        LocalDate dateTwo = LocalDate.parse(to);
        Day day = new Day(dateOne, dateTwo);
        model.addAttribute("timetables", timetableServiceImpl.showTimetable(day));
        return "timetable/show";
    }
    
    @RequestMapping("/student/timetable")
    public String showStudentTimetable(HttpServletRequest request, Model model) {
        LocalDate dateOne = LocalDate.parse(request.getParameter("from"));
        LocalDate dateTwo = LocalDate.parse(request.getParameter("to"));
        Student student = new Student.StudentBuilder().setID(studentId).build();
        Day day = new Day(dateOne, dateTwo);
        try {
            model.addAttribute("timetables", timetableServiceImpl.getUserTimetable(day, student));
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
        }        
        return "students/studentPage";        
    }
    @RequestMapping("/teacher/timetable")
    public String showTeacherTimetable(HttpServletRequest request, Model model) {
        LocalDate dateOne = LocalDate.parse(request.getParameter("from"));
        LocalDate dateTwo = LocalDate.parse(request.getParameter("to"));
        Teacher teacher = new Teacher.TeacherBuidler().setID(teacherId).build();
        Day day = new Day(dateOne, dateTwo);
        try {
            model.addAttribute("timetables", timetableServiceImpl.getUserTimetable(day, teacher));
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
        }        
        return "teachers/teacherPage";        
    }
}
