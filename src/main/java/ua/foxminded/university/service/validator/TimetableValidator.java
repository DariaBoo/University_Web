package ua.foxminded.university.service.validator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

import org.springframework.dao.DataIntegrityViolationException;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.service.HolidayService;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.entities.Timetable;

@Slf4j
public class TimetableValidator {

    private Notification notification;
    private static final String uniqueGroupDateTime = "unique_group_date_time";
    private static final String uniqueTeacherDateTime = "unique_teacher_date_time";
    private static final String uniqueRoomDateTime = "unique_room_date_time";
    private HolidayService holidayService;
    private TeacherService teacherService;    
    
    public TimetableValidator(HolidayService holidayService, TeacherService teacherService) {
        this.holidayService = holidayService;
        this.teacherService = teacherService;
    }

    public Notification validateTimetable(Timetable timetable) {
        notification = new Notification();
        if (timetable.getGroup().getId() < 0) {
            addError("Group is missing");
        }
        if (timetable.getLesson() == null) {
            addError("Lesson is missing");
        }
        validateDate(timetable);

        if (timetable.getLessonTimePeriod().isEmpty()) {
            addError("Lesson time is missing");
        }
        validateTeacher(timetable);

        if (timetable.getRoom().getNumber() == 0) {
            addError("Room is missing");
        }
        return notification;
    }

    public Notification validateUniqueConstraint(DataIntegrityViolationException exception, Timetable timetable) {
        notification = new Notification();
        String message = exception.getMostSpecificCause().getMessage();
        if (message.contains(uniqueGroupDateTime)) {
            addError("Group with id: " + timetable.getGroup().getId() + " is already scheduled (date:"
                    + timetable.getDate() + ", time:" + timetable.getLessonTimePeriod() + ")!");
        } else if (message.contains(uniqueTeacherDateTime)) {
            addError("Teacher with id: " + timetable.getTeacher().getId() + " is already scheduled ("
                    + timetable.getDate() + ", " + timetable.getLessonTimePeriod() + ")!");
        } else if (message.contains(uniqueRoomDateTime)) {
            addError("Room number: " + timetable.getRoom().getNumber() + " is already scheduled (" + timetable.getDate()
                    + ", " + timetable.getLessonTimePeriod() + ")!");
        }
        return notification;
    }

    private void validateDate(Timetable timetable) {
        if (timetable.getDate() == null) {
            addError("Date is missing");
            return;
        }
        LocalDate day = timetable.getDate();
        if (isWeekend(day)) {
            addError(day + " is a weekend. Can't schedule timetable!");
        }
        if (isHoliday(day)) {
            addError(day + " is a holiday. Can't schedule timetable!");
        }
    }

    private void validateTeacher(Timetable timetable) {
        int teacherId = timetable.getTeacher().getId();
        if (teacherId == 0) {
            addError("Teacher is missing");
        } else if (teacherService.checkIsAbsent(timetable.getDate(), teacherId)) {
            addError("Teacher [id::" + timetable.getTeacher().getId() + "] is absent [date::" + timetable.getDate()
                    + "].");
        }
    }

    private void addError(String error) {
        log.error(error);
        notification.addError(error);
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
        switch (dayOfWeek) {
        case SATURDAY:
            log.info("Inputed date [{}] is saturday", date);
            return true;
        case SUNDAY:
            log.info("Inputed date [{}] is sunday", date);
            return true;
        default:
            log.info("Inputed date [{}] is a week day", date);
        }
        return false;
    }

    private boolean isHoliday(LocalDate day) {
        boolean result = false;
        if (!holidayService.findAllHolidays().isEmpty()) {
            result = holidayService.findAllHolidays().stream().anyMatch(holiday -> holiday.getDate().isEqual(day));
        }
        log.info("Inputed day [{}] is holiday :: {}", result);
        return result;
    }
}
