package ua.foxminded.university.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.university.controller.urls.URL;
import ua.foxminded.university.dao.exception.UniqueConstraintViolationException;
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
import ua.foxminded.university.service.exception.ServiceException;

@Controller
public class TimetableController {

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
    @Autowired
    private TeacherService teacherService;

    @RequestMapping(URL.APP_TIMETABLE)
    public String chooseDatePeriod(Model model) {
        return "timetable/index";
    }

    @RequestMapping(URL.APP_TIMETABLE_SHOW)
    public ResponseEntity<List<Timetable>> showTimetable(HttpServletRequest request, Model model) {
        LocalDate setDateOne = LocalDate.parse(request.getParameter("from"));
        LocalDate setDateTwo = LocalDate.parse(request.getParameter("to"));
        Day day = new Day();
        day.setDateOne(setDateOne);
        day.setDateTwo(setDateTwo);
        HttpHeaders headers = new HttpHeaders();
        headers.add("show_timetable", "test");
        List<Timetable> timetable = new ArrayList<>();
        try {
            timetable = timetableService.showTimetable(day);
            return new ResponseEntity<>(timetable, headers, HttpStatus.FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(URL.APP_TIMETABLE_SCHEDULE)
    public String scheduleTimetable(Model model) {
        try {
        model.addAttribute("groups", groupService.findAllGroups());
        } catch (UniqueConstraintViolationException e) {
            System.out.println("ERROR");
        }
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

    @RequestMapping(value = URL.APP_TIMETABLE_SCHEDULE, method = RequestMethod.POST)
    public ResponseEntity<String> saveTimetable(@ModelAttribute("timetable") Timetable timetable, RedirectAttributes redirectAtt) {
        Day day = new Day();  
        String body = "";
        try {
        body = timetableService.scheduleTimetable(timetable);
        } catch (ServiceException e) {
            body = e.getMessage();
        }
        day.setDateOne(timetable.getDate());
        day.setDateTwo(timetable.getDate());
        redirectAtt.addFlashAttribute(message, body);
        redirectAtt.addFlashAttribute("day", day.getDateOne());
        redirectAtt.addFlashAttribute("timetables", timetableService.showTimetable(day));
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @RequestMapping(URL.APP_TIMETABLE_DELETE)
    public ResponseEntity<String> deleteTimetable(@PathVariable Integer id) {
        boolean isDeleted = timetableService.deleteTimetable(id);
        if (isDeleted) {
            return new ResponseEntity<>("Timetable was deleted!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Can't delete past timetable!", HttpStatus.BAD_REQUEST);
        }
    }    

    @RequestMapping(URL.STUDENT_TIMETABLE)
    public ResponseEntity<List<Timetable>> showStudentTimetable(HttpServletRequest request, Model model) {
        LocalDate setDateOne = LocalDate.parse(request.getParameter("from"));
        LocalDate setDateTwo = LocalDate.parse(request.getParameter("to"));
        AuthenticatedUser user = (AuthenticatedUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Student student = studentService.findByUsername(user.getUsername());
        Day day = new Day();
        day.setDateOne(setDateOne);
        day.setDateTwo(setDateTwo);
        try {
            List<Timetable> body = timetableService.getStudentTimetable(day, student);
            System.out.println("CONTROLLER BODY-------------" + body);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);//new ResponseEntity<>(body, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @RequestMapping(URL.TEACHER_TIMETABLE)
    public ResponseEntity<List<Timetable>> showTeacherTimetable(HttpServletRequest request, Model model) {
        LocalDate setDateOne = LocalDate.parse(request.getParameter("from"));
        LocalDate setDateTwo = LocalDate.parse(request.getParameter("to"));
        AuthenticatedUser user = (AuthenticatedUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Teacher teacher = teacherService.findByUsername(user.getUsername());
        Day day = new Day();
        day.setDateOne(setDateOne);
        day.setDateTwo(setDateTwo);
        try {
            List<Timetable> body = timetableService.getTeacherTimetable(day, teacher);
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
