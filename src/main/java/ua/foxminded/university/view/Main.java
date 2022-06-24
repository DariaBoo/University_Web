package ua.foxminded.university.view;

import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfig;
import ua.foxminded.university.dao.implementation.LessonDAOImpl;
import ua.foxminded.university.dao.implementation.StudentDAOImpl;
import ua.foxminded.university.dao.implementation.TeacherDAOImpl;
import ua.foxminded.university.service.HolidayService;
import ua.foxminded.university.service.implementation.HolidayServiceImpl;
import ua.foxminded.university.service.implementation.LessonServiceImpl;
import ua.foxminded.university.service.implementation.StudentServiceImpl;
import ua.foxminded.university.service.implementation.TeacherServiceImpl;
import ua.foxminded.university.service.pojo.Student;
import ua.foxminded.university.service.pojo.Teacher;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
//        HolidayServiceImpl holiday = context.getBean("holidayServiceImpl", HolidayServiceImpl.class);
//        System.out.println(holiday.deleteHoliday(7));
//        System.out.println(holiday.findAllHolidays());
//        LessonDAOImpl les = context.getBean("lessonDAOImpl", LessonDAOImpl.class);
//        System.out.println(les.findByID(1));
//        LessonServiceImpl lesS = context.getBean("lessonServiceImpl", LessonServiceImpl.class);
//        System.out.println(lesS.findByID(1));
        
        TeacherServiceImpl te = context.getBean("teacherServiceImpl", TeacherServiceImpl.class);
        TeacherDAOImpl td = context.getBean("teacherDAOImpl", TeacherDAOImpl.class);
        te.findTeachersByLessonId(1).stream().map(t -> t.getId() + " " + t.getFirstName() + " " + t.getLastName()).forEach(System.out::println);

        td.findTeachersByLessonId(1).get().stream().map(t -> t.getId() + " " + t.getFirstName() + " " + t.getLastName()).forEach(System.out::println);
        
//        te.findAllTeachers().stream().map(t -> t.getId() + " " + t.getFirstName() + " " + t.getLastName()).forEach(System.out::println);
//        Teacher tec = te.findByID(1);
//        System.out.println(tec.getId() + " " + tec.getFirstName() + " " + tec.getLastName());
//        System.out.println(les.findLessonsByTeacherId(2));
//            GroupDAOImpl group = context.getBean("groupDAOImpl", GroupDAOImpl.class);
//            System.out.println(group.addGroup(new Group.GroupBuilder().setName("AA-00").setDepartmentID(1).build()));

//        PersonDAO personDAO = context.getBean("personDAO", PersonDAO.class);
//        System.out.println(personDAO.show(1));

//        StudentServiceImpl st = context.getBean("studentServiceImpl", StudentServiceImpl.class);
//        StudentDAOImpl stD = context.getBean("studentDAOImpl", StudentDAOImpl.class);
//        st.findAllStudents().stream().map(student -> student.getFirstName() + " " + student.getLastName()).limit(3).forEach(System.out::println);
        //System.out.println(st.updateStudent(new Student.StudentBuilder().setID(300).setFirstName("Marry").setGroupID(1).setIdCard("a2").build()));
//        Student student = new Student.StudentBuilder().setID(300).setFirstName("Marry").build();
//        System.out.println(student.getLastName());
//        System.out.println(student.getGroupID());
//        System.out.println(student.getIdCard());
//        System.out.println(stD.findAllStudents().get().stream().limit(3).collect(Collectors.toList()));
//        System.out.println(st.updateStudent(student));
    }
}
