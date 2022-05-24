package ua.foxminded.university.view;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfig;
import ua.foxminded.university.dao.implementation.GroupDAOImpl;
import ua.foxminded.university.dao.implementation.LessonDAOImpl;
import ua.foxminded.university.dao.implementation.StudentDAOImpl;
import ua.foxminded.university.service.TimetableService;
import ua.foxminded.university.service.implementation.TimetableServiceImpl;
import ua.foxminded.university.service.pojo.Group;
import ua.foxminded.university.service.pojo.Student;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        StudentDAOImpl st = context.getBean("studentDAOImpl", StudentDAOImpl.class);
        GroupDAOImpl gr = context.getBean(GroupDAOImpl.class);
        LessonDAOImpl ls = context.getBean(LessonDAOImpl.class);
        TimetableServiceImpl tt = context.getBean("timetableServiceImpl",TimetableServiceImpl.class);
//       Student student = st.findByID(1).get();
//        System.out.println(student.getId() + " " + student.getFirstName() + " " + student.getLastName() + " " + student.getGroupID() + " " + student.getIdCard());
//        System.out.println(gr.findAllGroups().get().stream().limit(3).collect(Collectors.toList()).get(1).getName());
        List<Group> groups = new ArrayList<>();
        groups.add(null);
        //System.out.println(groups.get(0).getDepartmentID());
//        System.out.println(ls.findAllLessons().get().stream().limit(3).collect(Collectors.toList()).get(0));
//        System.out.println(st.getFirstNameMaxSize());
//        System.out.println(st.getLastNameMaxSize());
//        System.out.println(st.getIdCardMaxSize());
//        System.out.println(st.getPasswordMaxSize());
        String checkString = "sd-12";
        //System.out.println(checkString.toUpperCase().matches("(\\S+)-(\\d+)")); 
        LocalDate one = LocalDate.of(2022, 04, 05);
        LocalDate two = LocalDate.of(2022, 04, 05);
        //System.out.println(one.isEqual(two));
        DayOfWeek dayOfWeek = DayOfWeek.of(one.get(ChronoField.DAY_OF_WEEK));
        System.out.println(dayOfWeek.getClass().getName());
        LocalDate date = LocalDate.of(2022, 05, 20);
        DayOfWeek day = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
       // System.out.println(tt.isWeekend(date));
    }
}
