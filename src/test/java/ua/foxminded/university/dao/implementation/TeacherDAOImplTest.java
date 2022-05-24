package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.Teacher;

class TeacherDAOImplTest {
    private TeacherDAOImpl teacherDAOImpl;
    private AnnotationConfigApplicationContext context;
    private Day day;
    private Teacher teacher;
    private List<Teacher> teachers = new ArrayList<Teacher>();
    private final int maxFirstNameSize = 30;
    private final int maxLastNameSize = 30;
    private final int maxPositionSize = 30;
    private final int maxPasswordSize = 10;

    @BeforeEach
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        teacherDAOImpl = context.getBean("teacherDAOImpl", TeacherDAOImpl.class);
        day = new Day(LocalDate.of(2022, 04, 22), LocalDate.of(2022, 04, 28));
    }

    @Test
    void addTeacher_shouldReturnZero_whenInputExistedTeacher() throws SQLException {
        assertEquals(0, teacherDAOImpl
                .addTeacher(new Teacher.TeacherBuidler().setFirstName("Albus").setLastName("Dumbledore").build()));
    }

    @Test
    void addTeacher_shouldReturnAddedStudentID_whenInputNewTeacher() throws SQLException {
        assertEquals(1, teacherDAOImpl.addTeacher(new Teacher.TeacherBuidler().setFirstName("Lord")
                .setLastName("Voldemort").setPosition("professor of evil").setPassword("555").build()));
        teacherDAOImpl.deleteTeacher(11);
    }

    @Test
    void deleteTeacher_shouldReturnCountOfDeletedRows_whenInputID() throws SQLException {
        teacherDAOImpl.addTeacher(new Teacher.TeacherBuidler().setFirstName("Lord").setLastName("Voldemort")
                .setPosition("professor of evil").setPassword("555").build());
        assertEquals(1, teacherDAOImpl.deleteTeacher(11));
    }

    @ParameterizedTest(name = "When input not existed teacher id or negative number will return zero.")
    @ValueSource(ints = { 100, -1 })
    void deleteTeacher_shouldReturnZero_whenInputIncorrectID() {
        assertEquals(0, teacherDAOImpl.deleteTeacher(100));
    }

    @Test
    void assignLessonToTeacher_shouldReturnCountOfAddedRows_whenInputExistedLessonIDAndTeacherIDAndRowNotExists() {
        assertEquals(1, teacherDAOImpl.assignLessonToTeacher(1, 5));
        teacherDAOImpl.deleteLessonFromTeacher(1, 5);
    }

    @Test
    void assignLessonToTeacher_shouldReturnCountOfAddedRows_whenInputExistedLessonIDAndTeacherIDAndRowExists() {
        teacherDAOImpl.assignLessonToTeacher(100, 3);
        assertEquals(0, teacherDAOImpl.assignLessonToTeacher(100, 3));
    }

    @ParameterizedTest(name = "{index}. When input already existed or incorrect lesson id {0} and not existed or incorrect teacher id {1} will return negative number {2}.")
    @CsvSource({ "1, 1, 0", "1, 100, 0", "100, 1, 0", "100, 100, 0" })
    void assignLessonToTeacher_shouldReturnZero_whenInputIncorrectData(int lessonID, int teacherID, int result) {
        assertEquals(result, teacherDAOImpl.assignLessonToTeacher(lessonID, teacherID));
    }

    @Test
    void deleteLessonFromTeacher_shouldReturnCountOfDelededRows_whenInputExistedLessonIDAndTeacherID() {
        teacherDAOImpl.assignLessonToTeacher(1, 3);
        assertEquals(1, teacherDAOImpl.deleteLessonFromTeacher(1, 3));
    }

    @ParameterizedTest(name = "{index}. When input not existed or incorrect lesson id {0} and not existed or incorrect teacher id {1} will return negative number {2}.")
    @CsvSource({ "1, 3, 0", "1, 100, 0", "100, 1, 0", "100, 100, 0" })
    void deleteLessonFromTeacher_shouldReturnZero_whenInputNotExistedLessonIDatTheTeacher(int lessonID, int teacherID,
            int result) {
        assertEquals(result, teacherDAOImpl.deleteLessonFromTeacher(lessonID, teacherID));
    }

    @Test
    void changePosition_shouldReturnCountOfChangedRows_whenInputExistedTeacherID() {
        assertEquals(1, teacherDAOImpl.changePosition(5, "lecturer"));
    }

    @Test
    void changePosition_shouldReturnZero_whenInputNotExistedTeacherID() {
        assertEquals(0, teacherDAOImpl.changePosition(12, "lecturer"));
    }

    @Test
    void setTeacherAbsent_shouldReturnCountOfSetRows_whenInputExistedTeacher() {
        assertEquals(1, teacherDAOImpl.setTeahcerAbsent(1, day));
    }

    @Test
    void setTeacherAbsent_shouldReturnZero_whenInputNotExistedTeacher() {
        assertEquals(0, teacherDAOImpl.setTeahcerAbsent(12, day));
    }

    @Test
    void deleteTeacherAbsent_shouldReturnCountOfDeletedRows_whenInputCorrectData() {
        teacherDAOImpl.setTeahcerAbsent(1, day);
        assertEquals(1, teacherDAOImpl.deleteTeahcerAbsent(1, day));
    }

    @Test
    void deleteTeacherAbsent_shouldReturnZero_whenInputInCorrectTeacherID() {
        assertEquals(0, teacherDAOImpl.deleteTeahcerAbsent(100, day));
    }

    @Test
    void findByID_shouldReturnTeacher_whenInputExistedTeacherID() {
        teacher = new Teacher.TeacherBuidler().setID(1).setFirstName("Albus").setLastName("Dumbledore")
                .setPosition("professor").setDepartmentID(1).build();
        assertEquals(Optional.of(teacher), teacherDAOImpl.findByID(1));
    }

    @Test
    void findByID_shouldReturnOptionalEmpty_whenInputNotExistedTeacherID() {
        assertEquals(Optional.empty(), teacherDAOImpl.findByID(100));
    }

    @Test
    void findAllTeachers_shouldReturnCountOfTeachers_whenCallTheMethod() {
        assertEquals(10, teacherDAOImpl.findAllTeachers().get().stream().count());
    }

    @Test
    void findAllTeachers_shouldReturnFirstThreeTeachers_whenCallTheMethod() {
        teachers.clear();
        teachers.add(new Teacher.TeacherBuidler().setID(1).setFirstName("Albus").setLastName("Dumbledore")
                .setPosition("professor").setDepartmentID(1).build());
        teachers.add(new Teacher.TeacherBuidler().setID(2).setFirstName("Minerva").setLastName("McGonagall")
                .setPosition("lecturer").setDepartmentID(1).build());
        teachers.add(new Teacher.TeacherBuidler().setID(3).setFirstName("Severus").setLastName("Snape")
                .setPosition("professor").setDepartmentID(1).build());
        assertEquals(teachers, teacherDAOImpl.findAllTeachers().get().stream().limit(3).collect(Collectors.toList()));
    }

    @Test
    void findTeachersByDepartment_shouldReturnCountOfTeachers_whenInputExistedDepartmentID() {
        assertEquals(5, teacherDAOImpl.findTeachersByDepartment(1).get().stream().count());
    }

    @Test
    void findTeachersByDepartment_shouldReturnOptionalEmptyList_whenInputNotExistedDepartmentID() {
        assertEquals(Optional.of(new ArrayList<Teacher>()), teacherDAOImpl.findTeachersByDepartment(10));
    }

    @Test
    void findTeachersByDepartment_shouldReturnOptionalEmptyList_whenInputNegativeNumber() {
        assertEquals(Optional.of(new ArrayList<Teacher>()), teacherDAOImpl.findTeachersByDepartment(-1));
    }

    @Test
    void findTeachersByDepartment_shouldReturnListOfTeachers_whenInputExistedDepartmentID() {
        teachers.clear();
        teachers.add(new Teacher.TeacherBuidler().setID(1).setFirstName("Albus").setLastName("Dumbledore")
                .setPosition("professor").setDepartmentID(1).build());
        teachers.add(new Teacher.TeacherBuidler().setID(2).setFirstName("Minerva").setLastName("McGonagall")
                .setPosition("lecturer").setDepartmentID(1).build());
        assertEquals(teachers,
                teacherDAOImpl.findTeachersByDepartment(1).get().stream().limit(2).collect(Collectors.toList()));
    }

    @Test
    void changePassword_shouldReturnOne_whenInputExistedTeacherID() {
        assertEquals(1, teacherDAOImpl.changePassword(1, "5555"));
    }

    @Test
    void changePassword_shouldReturnZero_whenInputNotExistedTeacherID() {
        assertEquals(0, teacherDAOImpl.changePassword(100, "5555"));
    }

    @Test
    void changePassword_shouldReturnZero_whenInputNegativeNumber() {
        assertEquals(0, teacherDAOImpl.changePassword(-1, "5555"));
    }

    @Test
    void updateTeacher_shouldReturnOne_whenInputExistedTeacherID() {
        teacher = new Teacher.TeacherBuidler().setID(1).setFirstName("Lord").setLastName("Voldemort").build();
        assertEquals(1, teacherDAOImpl.updateTeacher(teacher));
    }
    
    @Test
    void updateTeacher_shouldReturnOne_whenInputExistedTeacherIDAndNullName() {
        teacher = new Teacher.TeacherBuidler().setID(1).setLastName("Voldemort").build();
        assertEquals(1, teacherDAOImpl.updateTeacher(teacher));
    }
    
    @Test
    void updateTeacher_shouldReturnOne_whenInputExistedTeacherIDAndNullSurname() {
        teacher = new Teacher.TeacherBuidler().setID(1).setFirstName("Lord").build();
        assertEquals(1, teacherDAOImpl.updateTeacher(teacher));
    }
    
    @Test
    void updateTeacher_shouldReturnOne_whenInputNotExistedTeacherID() {
        teacher = new Teacher.TeacherBuidler().setID(100).setFirstName("Lord").setLastName("Voldemort").build();
        assertEquals(0, teacherDAOImpl.updateTeacher(teacher));
    }
    
    @Test
    void getFirstNameMaxSize_shouldReturnColumnSize() {
        assertEquals(maxFirstNameSize, teacherDAOImpl.getFirstNameMaxSize());       
    }   
    @Test
    void getLastNameMaxSize_shouldReturnColumnSize() {
        assertEquals(maxLastNameSize, teacherDAOImpl.getLastNameMaxSize());       
    } 
    @Test
    void getPositionMaxSize_shouldReturnColumnSize() {
        assertEquals(maxPositionSize, teacherDAOImpl.getPositionMaxSize());       
    } 
    @Test
    void getPasswordMaxSize_shouldReturnColumnSize() {
        assertEquals(maxPasswordSize, teacherDAOImpl.getPasswordMaxSize());       
    } 
}
