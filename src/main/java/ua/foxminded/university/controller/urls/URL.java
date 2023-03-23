package ua.foxminded.university.controller.urls;

public class URL {

    // UniversityController
    public static final String APP_HOME = "/app/home";
    public static final String HOME_STUDENT = "/student";
    public static final String HOME_TEACHER = "/teacher";

    // SecurityController
    public static final String WELCOME = "/";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String LOGIN_ERROR = "/login_error";
    public static final String EXPIRED_JWT = "/expired-jwt";

    // GroupsController
    public static final String APP_GROUPS = "/app/groups";
    public static final String APP_GROUPS_VIEW_BY_ID = "/app/groups/{id}";

    // HolidaysController
    public static final String APP_HOLIDAYS = "/app/holidays";
    public static final String APP_DELETE_HOLIDAY_BY_ID = "/app/holidays/holiday/delete/{id}";
    public static final String APP_NEW_HOLIDAY = "/app/holidays/new";

    // LessonsController
    public static final String APP_LESSONS = "/app/lessons";
    public static final String APP_LESSONS_VIEW_BY_ID = "/app/lessons/{id}";

    // RoomsController
    public static final String APP_ROOMS = "/app/audiences";

    // StudentsController
    public static final String APP_STUDENTS = "/app/students";
    public static final String APP_STUDENTS_VIEW_BY_ID = "/app/students/{id}";
    public static final String APP_NEW_STUDENT = "/app/students/new";
    public static final String APP_DELETE_STUDENT_BY_ID = "/app/students/delete/{id}";
    public static final String APP_EDIT_STUDENT_BY_ID = "/app/students/edit/{id}";

    public static final String STUDENT_CHANGE_PASSWORD = "/student/{id}/change_password";

    // TeachersController
    public static final String APP_TEACHERS = "/app/teachers";
    public static final String APP_TEACHERS_VIEW_BY_ID = "/app/teachers/{id}";

    // TimetableController
    public static final String APP_TIMETABLE = "/app/timetable";
    public static final String APP_TIMETABLE_SHOW = "/app/timetable/show";
    public static final String APP_TIMETABLE_SCHEDULE = "/app/timetable/schedule";
    public static final String APP_TIMETABLE_SCHEDULE_GROUP = "/app/timetable/schedule/{groupId}";
    public static final String APP_TIMETABLE_SCHEDULE_GROUP_LESSON = "/app/timetable/schedule/{groupId}/{lessonId}";
    public static final String APP_TIMETABLE_DELETE = "/app/timetable/delete/{id}";
    

    public static final String STUDENT_TIMETABLE = "/student/timetable";
    public static final String TEACHER_TIMETABLE = "/teacher/timetable";

    private URL() {

    }
}
