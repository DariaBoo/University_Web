package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.Teacher;

@Component
@TestInstance(Lifecycle.PER_CLASS)
class TeacherDAOImplTest {
    private TeacherDAOImpl teacherDAOImpl;
    private AnnotationConfigApplicationContext context;
    private Day day;

    @BeforeAll
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        teacherDAOImpl = context.getBean("teacherDAOImpl", TeacherDAOImpl.class);
        day = new Day(LocalDate.of(2022, 04, 22), LocalDate.of(2022, 04, 28));
    }

    @Test
    void addTeacher_shouldReturnNegativeNumber_whenInputExistedTeacher() throws SQLException {
        assertEquals(-1, teacherDAOImpl
                .addTeacher(new Teacher.TeacherBuidler().setFirstName("Albus").setLastName("Dumbledore").build()));
    }

    @Test
    void addTeacher_shouldReturnAddedStudentID_whenInputNewTeacher() throws SQLException {
        assertEquals(11, teacherDAOImpl.addTeacher(new Teacher.TeacherBuidler().setFirstName("Lord")
                .setLastName("Voldemort").setPosition("professor of evil").setPassword(555).build()));
        teacherDAOImpl.deleteTeacher(11);
    }

    @Test
    void deleteTeacher_shouldReturnCountOfDeletedRows_whenInputID() throws SQLException {
        int id = teacherDAOImpl.addTeacher(new Teacher.TeacherBuidler().setFirstName("Lord").setLastName("Voldemort")
                .setPosition("professor of evil").setPassword(555).build());
        assertEquals(1, teacherDAOImpl.deleteTeacher(id));
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
        teacherDAOImpl.assignLessonToTeacher(1, 3);
        assertEquals(-1, teacherDAOImpl.assignLessonToTeacher(1, 3));
    }

    @ParameterizedTest(name = "{index}. When input already existed or incorrect lesson id {0} and not existed or incorrect teacher id {1} will return negative number {2}.")
    @CsvSource({ "1, 1, -1", "1, 100, -1", "100, 1, -1", "100, 100, -1" })
    void assignLessonToTeacher_shouldReturnNegativeNumber_whenInputIncorrectData(int lessonID, int teacherID,
            int result) {
        assertEquals(result, teacherDAOImpl.assignLessonToTeacher(lessonID, teacherID));
    }

    @Test
    void deleteLessonFromTeacher_shouldReturnCountOfDelededRows_whenInputExistedLessonIDAndTeacherID() {
        teacherDAOImpl.assignLessonToTeacher(1, 3);
        assertEquals(1, teacherDAOImpl.deleteLessonFromTeacher(1, 3));
    }

    @ParameterizedTest(name = "{index}. When input not existed or incorrect lesson id {0} and not existed or incorrect teacher id {1} will return negative number {2}.")
    @CsvSource({ "1, 3, -1", "1, 100, -1", "100, 1, -1", "100, 100, -1" })
    void deleteLessonFromTeacher_shouldReturnNegativeNumber_whenInputNotExistedLessonIDInTheTeacher(int lessonID,
            int teacherID, int result) {
        assertEquals(result, teacherDAOImpl.deleteLessonFromTeacher(lessonID, teacherID));
    }

    @Test
    void changePosition_shouldReturnCountOfChangedRows_whenInputExistedTeacherID() {
        assertEquals(1, teacherDAOImpl.changePosition(5, "lecturer"));
    }

    @Test
    void changePosition_shouldReturnNegativeNumber_whenInputNotExistedTeacherID() {
        assertEquals(-1, teacherDAOImpl.changePosition(12, "lecturer"));
    }

    @Test
    void setTeacherAbsent_shouldReturnCountOfSetRows_whenInputExistedTeacher() {
        assertEquals(1, teacherDAOImpl.setTeahcerAbsent(1, day));
    }

    @Test
    void setTeacherAbsent_shouldReturnNegativeNumber_whenInputNotExistedTeacher() {
        assertEquals(-1, teacherDAOImpl.setTeahcerAbsent(12, day));
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
    void isExists_shourtReturnTrue_whenInputExistedTeacherID() {
        assertTrue(teacherDAOImpl.isTeacherExists(1));
    }

    @ParameterizedTest(name = "When input not existed teacher id or negative number or zero will return false.")
    @ValueSource(ints = { 100, -1, 0 })
    void isExists_shourtReturnFalse_whenInputNotExistedTeacherID(int teacherID) {
        assertFalse(teacherDAOImpl.isTeacherExists(teacherID));
    }

}
