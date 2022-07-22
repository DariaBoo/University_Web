package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.naming.NamingException;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import ua.foxminded.university.config.HibernateConfigTest;
import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.entities.Lesson;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateConfigTest.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
class LessonDAOImplTest {

    @Autowired
    private LessonDAO lessonDAO;

    private Lesson lesson;
    private List<Lesson> lessons = new ArrayList<Lesson>();

    @BeforeAll
    void lesson() {
        lesson = Lesson.builder().name("Test").description("test").build();
    }

    @BeforeEach
    void init() throws NamingException, SQLException {
        new EmbeddedDatabaseBuilder().setName("test").setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:tablesTest.sql").build();
    }

    @Test
    void getLessonFromDBTest() {
        assertNotNull(lessonDAO.findByID(1));
    }

    @Test
    void addLesson_shouldAddedLesson_whenInputNewLesson() throws SQLException {
        int countOfLessons = lessonDAO.findAllLessons().get().size();
        lessonDAO.addLesson(lesson);
        assertEquals(countOfLessons + 1, lessonDAO.findAllLessons().get().size());
    }

    @Test
    void addLesson_shouldThrowDAOException_whenInputNotUniqueLessonName() {
        lesson = Lesson.builder().name("Test").description("test").build();
        lessonDAO.addLesson(lesson);
        Lesson notUniqueName = Lesson.builder().name("Test").description("test").build();
        assertThrows(DAOException.class, () -> lessonDAO.addLesson(notUniqueName));
    }

    @Test
    void deleteLesson_shouldReturnTrue_whenInputID() {
        int id = lessonDAO.addLesson(lesson);
        assertTrue(lessonDAO.deleteLesson(id));
    }

    @ParameterizedTest(name = "{index}. When input not existed lesson id or negative number or zero will return false.")
    @ValueSource(ints = { 100, -1, 0 })
    void deleteLesson_shouldReturnFalse_whenInputNotExistedID(int lessonID) {
        assertFalse(lessonDAO.deleteLesson(lessonID));
    }

    @Test
    void findByID_shouldReturnLesson_whenInputExistedLessonID() {
        int id = lessonDAO.addLesson(lesson);
        assertEquals(Optional.of(lesson), lessonDAO.findByID(id));
    }

    @Test
    void findByID_shouldReturnEmptyOptional_whenInputNotExistedLessonID() {
        assertEquals(Optional.empty(), lessonDAO.findByID(100));
    }

    @Test
    void findByID_shouldReturnEmptyOptional_whenInputNegativeNumber() {
        assertEquals(Optional.empty(), lessonDAO.findByID(-1));
    }

    @Test
    void findAllLessons_shouldReturnCountOfLessons_whenCallTheMethod() {
        assertEquals(5, lessonDAO.findAllLessons().get().size());
    }

    @Test
    void findAllLessons_shouldReturnFirstThreeLessons_whenCallTheMethod() {
        lessons.add(Lesson.builder().id(1).name("Alchemy")
                .description("the study of transmutation of substances into other forms.").build());
        lessons.add(Lesson.builder().id(2).name("Herbology")
                .description("the study of magical plants and how to take care of, utilise and combat them.")
                .build());
        lessons.add(Lesson.builder().id(3).name("History of Magic")
                .description("the study of magical history.").build());
        assertEquals(lessons, lessonDAO.findAllLessons().get().stream().limit(3).collect(Collectors.toList()));
    }

    @Test
    void updateLesson_shouldReturnUpdatedName_whenInputCorrectData() {
        int id = lessonDAO.addLesson(lesson);
        lesson.setName("test2");
        lessonDAO.updateLesson(lesson);
        assertEquals("test2", lessonDAO.findByID(id).get().getName());
    }

    @Test
    void findLessonsByTeacherId_shouldReturnCountOfLessons_whenCallTheMethod() {
        assertEquals(2, lessonDAO.findLessonsByTeacherId(1).get().size());
    }

    @Test
    void findLessonsByTeacherId_shouldThrowNoResultException_whenInputNotExistedTeacherId() {
        assertThrows(NoResultException.class, () -> lessonDAO.findLessonsByTeacherId(-1).get().size());
    }

    @Test
    void findLessonsByGroupId_shouldReturnCountOfLessons_whenCallTheMethod() {
        assertEquals(3, lessonDAO.findLessonsByGroupId(1).get().size());
    }

    @Test
    void findLessonsByGroupId_shouldReturnCountOfLessons_InputNotExistedGroupId() {
        assertThrows(NoResultException.class, () -> lessonDAO.findLessonsByGroupId(-1).get().size());
    }
}
