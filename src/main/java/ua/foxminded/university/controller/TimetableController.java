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

import ua.foxminded.university.dao.exception.UniqueConstraintViolationException;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.RoomService;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.service.TimetableService;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Room;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;
import ua.foxminded.university.service.exception.ServiceException;

@Controller
public class TimetableController {
    static int studentId;
    static int teacherId;
    private static String message = "message";
    private static String timetableNew = "timetable/new";

    @Autowired
    private TimetableService timetableService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private StudentService studentService;

    @RequestMapping("/timetable")
    public String chooseDatePeriod(Model model) {
        return "timetable/index";
    }

    @RequestMapping("/timetable/show")
    public String showTimetable(HttpServletRequest request, Model model) {
        LocalDate setDateOne = LocalDate.parse(request.getParameter("from"));
        LocalDate setDateTwo = LocalDate.parse(request.getParameter("to"));
        Day day = new Day(setDateOne, setDateTwo);
        try {
            model.addAttribute("timetables", timetableService.showTimetable(day));
        } catch (IllegalArgumentException e) {
            model.addAttribute(message, e.getMessage());
        }
        return "timetable/index";
    }

    @GetMapping("/timetable/schedule")
    public String scheduleTimetable(Model model) {
        model.addAttribute("groups", groupService.findAllGroups());
        return timetableNew;
    }

    @GetMapping("/timetable/schedule/{groupId}")
    public String scheduleTimetableForGroup(@PathVariable("groupId") int id, Model model) {
        Group group = groupService.findById(id);
        model.addAttribute("group", group);
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("lessons", group.getLessons());
        return timetableNew;
    }

    @GetMapping("/timetable/schedule/{groupId}/{lessonId}")
    public String scheduleTimetableForLesson(@PathVariable("lessonId") int lessonId,
            @PathVariable("groupId") int groupId, Model model) {
        Group group = groupService.findById(groupId);
        int countOfStudents = group.getStudents().size();
        Lesson lesson = lessonService.findById(lessonId);
        model.addAttribute("timetable", new Timetable());
        model.addAttribute("lesson", lesson);
        model.addAttribute("group", group);
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("room", new Room());
        model.addAttribute("lessons", group.getLessons());
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("teachers", lesson.getTeachers());
        model.addAttribute("rooms", roomService.findSuitableRooms(countOfStudents));
        return timetableNew;
    }

    @RequestMapping(value = "/timetable/schedule", method = RequestMethod.POST)
    public String saveTimetable(@ModelAttribute("timetable") Timetable timetable, RedirectAttributes redirectAtt,
            Model model) {
        Day day = new Day();
        day.setDateOne(timetable.getDate());
        day.setDateTwo(timetable.getDate());
        try {
            timetableService.scheduleTimetable(timetable);
            redirectAtt.addFlashAttribute(message, "Timetable was scheduled!!!");
            redirectAtt.addFlashAttribute("day", day.getDateOne());
            redirectAtt.addFlashAttribute("showTimetable", timetableService.showTimetable(day));
        } catch (ServiceException | UniqueConstraintViolationException e) {
            redirectAtt.addFlashAttribute(message, e.getMessage());
        }
        return "redirect:/timetable/schedule";
    }

    @RequestMapping("timetable/delete/{id}")
    public String deleteTimetable(@PathVariable Integer id, RedirectAttributes redirectAtt) {
        boolean isDeleted = timetableService.deleteTimetable(id);
        if (isDeleted) {
            redirectAtt.addFlashAttribute(message, "Timetable was deleted!");
        } else {
            redirectAtt.addFlashAttribute(message, "Can't delete past timetable!");
        }
        return "redirect:/timetable";
    }

    @RequestMapping("/student/timetable")
    public String showStudentTimetable(HttpServletRequest request, Model model) {
        LocalDate setDateOne = LocalDate.parse(request.getParameter("from"));
        LocalDate setDateTwo = LocalDate.parse(request.getParameter("to"));
        Student student = studentService.findById(studentId);
        Day day = new Day(setDateOne, setDateTwo);
        try {
            model.addAttribute("timetables", timetableService.getStudentTimetable(day, student));
        } catch (IllegalArgumentException e) {
            model.addAttribute(message, e.getMessage());
        }
        return "students/studentPage";
    }

    @RequestMapping("/teacher/timetable")
    public String showTeacherTimetable(HttpServletRequest request, Model model) {
        LocalDate setDateOne = LocalDate.parse(request.getParameter("from"));
        LocalDate setDateTwo = LocalDate.parse(request.getParameter("to"));
        Teacher teacher = Teacher.builder().id(teacherId).build();
        Day day = new Day(setDateOne, setDateTwo);
        try {
            model.addAttribute("timetables", timetableService.getTeacherTimetable(day, teacher));
        } catch (IllegalArgumentException e) {
            model.addAttribute(message, e.getMessage());
        }
        return "teachers/teacherPage";
    }
}
