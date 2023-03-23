package ua.foxminded.university.controller;

import java.time.LocalDate;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.controller.urls.URL;
import ua.foxminded.university.controller.validator.ValidationUtils;
import ua.foxminded.university.security.model.AuthenticatedUser;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.RoomService;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.TimetableService;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Room;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.Timetable;
import ua.foxminded.university.service.exception.EntityConstraintViolationException;

@Slf4j
@Controller
public class TimetableController {

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
    @Autowired
    private TeacherService teacherService;

    private static String message = "message";

    @RequestMapping(URL.APP_TIMETABLE)
    public String chooseDatePeriod(Model model) {
        return "timetable/index";
    }

    @RequestMapping(URL.APP_TIMETABLE_SHOW)
    public String showTimetable(HttpServletRequest request, Model model) {
        LocalDate setDateOne = LocalDate.parse(request.getParameter("from"));
        LocalDate setDateTwo = LocalDate.parse(request.getParameter("to"));
        Day day = new Day();
        day.setDateOne(setDateOne);
        day.setDateTwo(setDateTwo);
        try {
            model.addAttribute("timetables", timetableService.showTimetable(day));
        } catch (IllegalArgumentException e) {
            log.error("[ON showTimetable]:: IllegalArgumentException {}", e.getLocalizedMessage());
            model.addAttribute(message, e.getMessage());
        }
        return "timetable/index";
    }

    @GetMapping(URL.APP_TIMETABLE_SCHEDULE)
    public String scheduleTimetable(Model model) {
        model.addAttribute("groups", groupService.findAllGroups());
        return timetableNew;
    }

    @GetMapping(URL.APP_TIMETABLE_SCHEDULE_GROUP)
    public String scheduleTimetableForGroup(@PathVariable("groupId") int id, Model model) {
        Group group = groupService.findById(id);
        model.addAttribute("group", group);
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("lessons", group.getLessons());
        return timetableNew;
    }

    @GetMapping(URL.APP_TIMETABLE_SCHEDULE_GROUP_LESSON)
    public String scheduleTimetableForLesson(@PathVariable("lessonId") int lessonId,
            @PathVariable("groupId") int groupId, Model model) {
        Group group = new Group();
        Lesson lesson = new Lesson();
        try {
            group = groupService.findById(groupId);
            lesson = lessonService.findById(lessonId);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getLocalizedMessage());
        }
        int countOfStudents = group.getStudents().size();
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

    @PostMapping(value = URL.APP_TIMETABLE_SCHEDULE)
    public String saveTimetable(@Valid @ModelAttribute("timetable") Timetable timetable, BindingResult bindingResult,
            RedirectAttributes redirectAtt, Model model) {
        Day day = new Day();
        String result = "";
        if (bindingResult.hasErrors()) {
            result = ValidationUtils.getErrorMessages(bindingResult);
            log.warn("[ON saveTimetable]:: validation errors - {}", result);
        } else {
            try {
                result = timetableService.scheduleTimetable(timetable);
            } catch (EntityConstraintViolationException e) {
                log.error("[ON saveTimetable]:: EntityConstraintViolationException {}", e.getLocalizedMessage());
                result = e.getLocalizedMessage();
            }
        }
        day.setDateOne(timetable.getDate());
        day.setDateTwo(timetable.getDate());
        redirectAtt.addFlashAttribute(message, result);
        System.out.println("PRINT MESSAGE: " + result);
        redirectAtt.addFlashAttribute("day", day.getDateOne());
        redirectAtt.addFlashAttribute("timetables", timetableService.showTimetable(day));
        return "redirect:/app/timetable/schedule";

    }

    @RequestMapping(URL.APP_TIMETABLE_DELETE)
    public String deleteTimetable(@PathVariable Integer id, RedirectAttributes redirectAtt) {
        try {
            timetableService.deleteTimetable(id);
            redirectAtt.addFlashAttribute(message, "Timetable was deleted!");
        } catch (EntityNotFoundException ex) {
            log.error("[ON deleteTimetable]:: bad request");
            redirectAtt.addFlashAttribute(message, "Can't delete past timetable!");
        }
        return "redirect:/app/timetable";
    }

    @RequestMapping(URL.STUDENT_TIMETABLE)
    public String showStudentTimetable(HttpServletRequest request, Model model) {
        try {
            LocalDate setDateOne = LocalDate.parse(request.getParameter("from"));
            LocalDate setDateTwo = LocalDate.parse(request.getParameter("to"));
            Day day = new Day();
            day.setDateOne(setDateOne);
            day.setDateTwo(setDateTwo);
            AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            Student student = studentService.findByUsername(user.getUsername());
            model.addAttribute("timetables", timetableService.getStudentTimetable(day, student));
        } catch (IllegalArgumentException e) {
            log.error("[ON showStudentTimetableho]:: IllegalArgumentException {}", e.getLocalizedMessage());
            model.addAttribute(message, e.getMessage());
        }
        return "students/studentPage";
    }

    @RequestMapping(URL.TEACHER_TIMETABLE)
    public String showTeacherTimetable(HttpServletRequest request, Model model) {
        try {
            LocalDate setDateOne = LocalDate.parse(request.getParameter("from"));
            LocalDate setDateTwo = LocalDate.parse(request.getParameter("to"));
            AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            Teacher teacher = teacherService.findByUsername(user.getUsername());
            Day day = new Day();
            day.setDateOne(setDateOne);
            day.setDateTwo(setDateTwo);
            model.addAttribute("timetables", timetableService.getTeacherTimetable(day, teacher));
        } catch (IllegalArgumentException e) {
            log.error("[ON showTeacherTimetable]:: IllegalArgumentException {}", e.getLocalizedMessage());
            model.addAttribute(message, e.getMessage());
        }
        return "teachers/teacherPage";
    }
}
