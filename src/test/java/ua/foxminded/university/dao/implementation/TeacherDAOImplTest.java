package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import ua.foxminded.university.config.HibernateConfigTest;
import ua.foxminded.university.dao.TeacherDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Teacher;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateConfigTest.class }, loader = AnnotationConfigContextLoader.class)
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
class TeacherDAOImplTest {

    @Autowired
    private TeacherDAO teacherDAO;

    private Day day;
    private Teacher teacher;
    private List<Teacher> teachers = new ArrayList<Teacher>();

    @BeforeAll
    void setValues() {
        teacher = new Teacher.TeacherBuidler().setFirstName("test").setLastName("test").setPosition("test").setPassword("555").build();
        day = new Day(LocalDate.of(2023, 01, 01), LocalDate.of(2023, 01, 01));
    }

    @BeforeEach
    void init() {
        new EmbeddedDatabaseBuilder().setName("test").setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:tablesTest.sql").build();
    }

    @Test
    void addTeacher_shouldThrowDAOException_whenInputExistedTeacher() { 
        teacher = new Teacher.TeacherBuidler().setFirstName("test").setLastName("test").setPosition("test").setPassword("555").build();
        int id = teacherDAO.addTeacher(teacher);
        Teacher notUniqueNameAndSurname = new Teacher.TeacherBuidler().setFirstName("test").setLastName("test").setPosition("professor").setPassword("555").build();
        assertThrows(DAOException.class, () -> teacherDAO.addTeacher(notUniqueNameAndSurname));
        teacherDAO.deleteTeacher(id);
    }

    @Test
    void addTeacher_shouldReturnAddedTeacherID_whenInputNewTeacher() {
        int countOfTeachers = teacherDAO.findAllTeachers().get().size();
        teacher = new Teacher.TeacherBuidler().setFirstName("test2").setLastName("test2").setPosition("test").setPassword("555").build();
        int id = teacherDAO.addTeacher(teacher);
        assertEquals(countOfTeachers + 1, id);
        teacherDAO.deleteTeacher(id);
    }

    @Test
    void deleteTeacher_shouldReturnTrue_whenInputID() throws SQLException {
        int id = teacherDAO.addTeacher(teacher);
        assertTrue(teacherDAO.deleteTeacher(id));
    }

    @ParameterizedTest(name = "When input not existed teacher id or negative number will return false.")
    @ValueSource(ints = { 100, -1 })
    void deleteTeacher_shouldReturnZero_whenInputIncorrectID(int incorrectID) {
        assertFalse(teacherDAO.deleteTeacher(incorrectID));
    }

    @Test
    void assignLessonToTeacher_shouldReturnTrue_whenInputExistedLessonIDAndTeacherIDAndRowNotExists() {
        assertTrue(teacherDAO.assignLessonToTeacher(1, 5));
    }

//    @Test
//    void assignLessonToTeacher_shouldReturnFalse_whenInputExistedLessonIDAndTeacherIDAndRowExists() {
//        teacherDAO.assignLessonToTeacher(1, 5);
//        assertFalse(teacherDAO.assignLessonToTeacher(1, 5));
//    }

    @ParameterizedTest(name = "{index}. When input incorrect lesson id {0} and incorrect teacher id {1}.")
    @CsvSource({"1, 100", "100, 1", "100, 100" })
    void assignLessonToTeacher_shouldReturnZero_whenInputIncorrectData(int lessonID, int teacherID) {
        assertThrows(org.hibernate.ObjectNotFoundException.class, () -> teacherDAO.assignLessonToTeacher(lessonID, teacherID));
    }

    @Test
    void deleteLessonFromTeacher_shouldReturnTrue_whenInputExistedLessonIDAndTeacherID() {
        System.out.println("isassigned: " + teacherDAO.assignLessonToTeacher(1, 3));
        assertTrue(teacherDAO.deleteLessonFromTeacher(1, 3));
    }

    @ParameterizedTest(name = "{index}. When input not existed or incorrect lesson id {0} and not existed or incorrect teacher id {1}.")
    @CsvSource({"1, 100", "100, 1", "100, 100" })
    void deleteLessonFromTeacher_shouldThrowException_whenInputNotExistedLessonIDatTheTeacher(int lessonID, int teacherID) {
        assertThrows(org.hibernate.ObjectNotFoundException.class, () -> teacherDAO.deleteLessonFromTeacher(lessonID, teacherID));
    }

    @Test
    void setTeacherAbsent_shouldReturnCountOfSetRows_whenInputExistedTeacher() {
        assertEquals(12, teacherDAO.setTeahcerAbsent(1, day));
    }

    @Test
    void setTeacherAbsent_shouldReturnZero_whenInputNotExistedTeacher() {
        assertThrows(ConstraintViolationException.class, () -> teacherDAO.setTeahcerAbsent(12, day));
    }

    @Test
    void deleteTeacherAbsent_shouldReturnTrue_whenInputCorrectData() {
        teacherDAO.setTeahcerAbsent(1, new Day(LocalDate.of(2023, 01, 02), LocalDate.of(2023, 01, 02)));
        assertTrue(teacherDAO.deleteTeahcerAbsent(1, day));
    }

    @Test
    void deleteTeacherAbsent_shouldReturnFalse_whenInputInCorrectTeacherID() {
        assertFalse(teacherDAO.deleteTeahcerAbsent(100, day));
    }

    @Test
    void findByID_shouldReturnTeacher_whenInputExistedTeacherID() {
        teacher = new Teacher.TeacherBuidler().setID(1).setFirstName("Albus").setLastName("Dumbledore")
                .setPosition("professor").setDepartmentID(1).setPassword("555").build();
        assertEquals(Optional.of(teacher), teacherDAO.findByID(1));
    }

    @Test
    void findByID_shouldReturnOptionalEmpty_whenInputNotExistedTeacherID() {
        assertEquals(Optional.empty(), teacherDAO.findByID(100));
    }

    @Test
    void findAllTeachers_shouldReturnCountOfTeachers_whenCallTheMethod() {
        assertEquals(10, teacherDAO.findAllTeachers().get().size());
    }

    @Test
    void findAllTeachers_shouldReturnFirstThreeTeachers_whenCallTheMethod() {
        teachers.clear();
        teachers.add(new Teacher.TeacherBuidler().setID(1).setFirstName("Albus").setLastName("Dumbledore")
                .setPosition("professor").setDepartmentID(1).setPassword("555").build());
        teachers.add(new Teacher.TeacherBuidler().setID(2).setFirstName("Minerva").setLastName("McGonagall")
                .setPosition("lecturer").setDepartmentID(1).setPassword("555").build());
        teachers.add(new Teacher.TeacherBuidler().setID(3).setFirstName("Severus").setLastName("Snape")
                .setPosition("professor").setDepartmentID(1).setPassword("555").build());
        assertEquals(teachers, teacherDAO.findAllTeachers().get().stream().limit(3).collect(Collectors.toList()));
    }

    @Test
    void findTeachersByLessonId_shouldReturnContOfTeachers_whenInputLessonId() {
        assertEquals(2, teacherDAO.findTeachersByLessonId(1).get().size());
    }

    @Test
    void findTeachersByLessonId_shouldReturnEmptyArrayList_whenInputIncorectLessonId() {
        assertThrows(NoResultException.class, () -> teacherDAO.findTeachersByLessonId(100));
    }

    @Test
    void showTeacherAbsent_shouldReturnSizeOfList_whenInputCorrectTeacherId() {
        assertEquals(5, teacherDAO.showTeacherAbsent(1).get().size());
    }

    @Test
    void showTeacherAbsent_shouldReturnEmptyList_whenInputIncorrectTeacherId() {
        assertTrue(teacherDAO.showTeacherAbsent(100).get().isEmpty());
    }

    @Test
    void changePassword_shouldReturnOne_whenInputExistedTeacherID() {
        assertEquals(1, teacherDAO.changePassword(1, "5555"));
    }

    @Test
    void changePassword_shouldReturnZero_whenInputNotExistedTeacherID() {
        assertEquals(0, teacherDAO.changePassword(100, "5555"));
    }

    @Test
    void changePassword_shouldReturnZero_whenInputNegativeNumber() {
        assertEquals(0, teacherDAO.changePassword(-1, "5555"));
    }

    @Test
    void updateTeacher_shouldReturnOne_whenInputExistedTeacherID() {
        int id = teacherDAO.addTeacher(teacher);
        teacher.setFirstName("test2");
        teacherDAO.updateTeacher(teacher);
        assertEquals("test2", teacherDAO.findByID(id).get().getFirstName());
    }
    
    @Test
    void checkIsAbsent_shouldReturnTrue_whenTeacherIsAbsent() {
        Day dayAbsent = new Day();
        dayAbsent.setDateOne(LocalDate.of(2022, 06, 19));
        dayAbsent.setDateTwo(LocalDate.of(2022, 06, 20));
        teacherDAO.setTeahcerAbsent(1, dayAbsent);
        teacher = new Teacher.TeacherBuidler().setID(1).build();
        LocalDate checkedDate = LocalDate.of(2022,06,19);
        assertTrue(teacherDAO.checkIsAbsent(teacher, checkedDate));
    }
}
