package ua.foxminded.university.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.university.service.implementation.GroupServiceImpl;
import ua.foxminded.university.service.implementation.LessonServiceImpl;
import ua.foxminded.university.service.implementation.TimetableServiceImpl;
import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.Lesson;
import ua.foxminded.university.service.pojo.Student;
import ua.foxminded.university.service.pojo.Teacher;
import ua.foxminded.university.service.pojo.Timetable;

@Controller
public class TimetableController {
    static int studentId;
    static int teacherId;
    private final TimetableServiceImpl timetableServiceImpl;
    private final LessonServiceImpl lessonServiceImpl;
    private final GroupServiceImpl groupServiceImpl;

    @Autowired
    public TimetableController(TimetableServiceImpl timetableServiceImpl, LessonServiceImpl lessonServiceImpl,
            GroupServiceImpl groupServiceImpl) {
        this.timetableServiceImpl = timetableServiceImpl;
        this.lessonServiceImpl = lessonServiceImpl;
        this.groupServiceImpl = groupServiceImpl;
    }

    @RequestMapping("/timetable")
    public String chooseDatePeriod(Model model) {
        return "timetable/index";
    }

    @RequestMapping("/timetable/show")
    public String showTimetable(HttpServletRequest request, Model model) {
        LocalDate dateOne = LocalDate.parse(request.getParameter("from"));
        LocalDate dateTwo = LocalDate.parse(request.getParameter("to"));
        Day day = new Day(dateOne, dateTwo);
        try {
            model.addAttribute("timetables", timetableServiceImpl.showTimetable(day));
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "timetable/index";
    }

    @GetMapping("/timetable/schedule")
    public String scheduleTimetable(Model model) {    
        model.addAttribute("groups", groupServiceImpl.findAllGroups());
        return "timetable/new";
    }
    
    @GetMapping("/timetable/schedule/{id}")
    public String showLessonsToSelectedGroup(@PathVariable("id") int id, Model model) {
        model.addAttribute("day", new Day());
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("group", groupServiceImpl.findById(id));
        model.addAttribute("timetable", new Timetable());
        model.addAttribute("lessons", lessonServiceImpl.findLessonsByGroupId(id));
        model.addAttribute("groups", groupServiceImpl.findAllGroups());
        return "timetable/new";
    }  
    
    @RequestMapping(value = "/timetable/schedule", method = RequestMethod.POST)
    public String saveTimetable(@ModelAttribute("timetable") Timetable timetable, RedirectAttributes redirectAtt, Model model) {        
        Day day = new Day();
        day.setDateOne(timetable.getDay().getDateOne());
        day.setDateTwo(timetable.getDay().getDateOne());
       
        try {
            int result = timetableServiceImpl.scheduleTimetable(timetable);
            if (result == 0) {
                redirectAtt.addFlashAttribute("message", "Error occured while scheduled timetable!");
            } else {
                redirectAtt.addFlashAttribute("message", "Timetable was scheduled!!!");
                redirectAtt.addFlashAttribute("day", day.getDateOne());
                redirectAtt.addFlashAttribute("showTimetable", timetableServiceImpl.showTimetable(day));
            }
        } catch (Exception e) {
            redirectAtt.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/timetable/schedule";
    }
    
    @RequestMapping("timetable/delete/{id}")
    public String deleteTimetable(@PathVariable Integer id, RedirectAttributes redirectAtt) {
        int result = timetableServiceImpl.deleteTimetable(id);
        if (result == 0) {
            redirectAtt.addFlashAttribute("message", "Can't delete past timetable!");
        } else {
            redirectAtt.addFlashAttribute("message", "Timetable was deleted!");
        }

        return "redirect:/timetable";
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
