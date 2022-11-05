package ua.foxminded.university.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping(URL.APP_TIMETABLE)
    public String chooseDatePeriod(Model model) {
        return "timetable/index";
    }

    @RequestMapping(URL.APP_TIMETABLE_SHOW)
    public ResponseEntity<List<Timetable>> showTimetable(@RequestParam("from") String dateOne,
            @RequestParam("to") String dateTwo, Model model) {
        LocalDate setDateOne = LocalDate.parse(dateOne);
        LocalDate setDateTwo = LocalDate.parse(dateTwo);
        Day day = new Day();
        day.setDateOne(setDateOne);
        day.setDateTwo(setDateTwo);
        List<Timetable> timetable = new ArrayList<>();
        try {
            timetable = timetableService.showTimetable(day);
            return new ResponseEntity<>(timetable, HttpStatus.FOUND);
        } catch (IllegalArgumentException e) {
            log.error("[ON showTimetable]:: IllegalArgumentException {}", e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<String> saveTimetable(@Valid @ModelAttribute("timetable") Timetable timetable,
            BindingResult bindingResult) {
        String body = "";
        if (bindingResult.hasErrors()) {
            body = ValidationUtils.getErrorMessages(bindingResult);
            log.warn("[ON saveTimetable]:: validation errors - {}", body);
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        } else {
            try {
                body = timetableService.scheduleTimetable(timetable);
                return new ResponseEntity<>(body, HttpStatus.CREATED);
            } catch (EntityConstraintViolationException e) {
                log.error("[ON saveTimetable]:: EntityConstraintViolationException {}", e.getLocalizedMessage());
                body = e.getLocalizedMessage();
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(URL.APP_TIMETABLE_DELETE)
    public ResponseEntity<String> deleteTimetable(@PathVariable Integer id) {
        try {
            timetableService.deleteTimetable(id);
            return new ResponseEntity<>("Timetable was deleted!", HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            log.error("[ON deleteTimetable]:: bad request");
            return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(URL.STUDENT_TIMETABLE)
    public ResponseEntity<List<Timetable>> showStudentTimetable(@RequestParam("from") String dateOne,
            @RequestParam("to") String dateTwo, Model model) {
        LocalDate setDateOne = LocalDate.parse(dateOne);
        LocalDate setDateTwo = LocalDate.parse(dateTwo);
        AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Student student = studentService.findByUsername(user.getUsername());
        Day day = new Day();
        day.setDateOne(setDateOne);
        day.setDateTwo(setDateTwo);
        try {
            List<Timetable> body = timetableService.getStudentTimetable(day, student);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
        } catch (IllegalArgumentException e) {
            log.error("[ON showStudentTimetable]:: IllegalArgumentException {}", e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(URL.TEACHER_TIMETABLE)
    public ResponseEntity<List<Timetable>> showTeacherTimetable(@RequestParam("from") String dateOne,
            @RequestParam("to") String dateTwo, Model model) {
        LocalDate setDateOne = LocalDate.parse(dateOne);
        LocalDate setDateTwo = LocalDate.parse(dateTwo);
        AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Teacher teacher = teacherService.findByUsername(user.getUsername());
        Day day = new Day();
        day.setDateOne(setDateOne);
        day.setDateTwo(setDateTwo);
        try {
            List<Timetable> body = timetableService.getTeacherTimetable(day, teacher);
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("[ON showTeacherTimetable]:: IllegalArgumentException {}", e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
