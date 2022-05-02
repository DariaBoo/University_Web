package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.service.pojo.Lesson;

@Component
@TestInstance(Lifecycle.PER_CLASS)
class LessonDAOImplTest {
    private LessonDAOImpl lessonDAOImpl;
    private AnnotationConfigApplicationContext context;

    @BeforeAll
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        lessonDAOImpl = context.getBean("lessonDAOImpl", LessonDAOImpl.class);
    }

    @Test
    void addLesson_shouldReturnAddedLessonID_whenInputNewLesson() throws SQLException {
        assertEquals(6, lessonDAOImpl
                .addLesson(new Lesson.LessonBuilder().setName("Quidditch").setDescription("sport").build()));
        lessonDAOImpl.deleteLesson(6);
    }

    @Test
    void addLesson_shouldReturnNegativeNumber_whenInputExistedLesson() throws SQLException {
        assertEquals(-1, lessonDAOImpl.addLesson(new Lesson.LessonBuilder().setName("Alchemy").build()));
    }

    @Test
    void deleteLesson_shouldReturnCountOfDeletedRows_whenInputID() throws SQLException {
        int id = lessonDAOImpl.addLesson(new Lesson.LessonBuilder().setName("Kung-fu").setDescription("sport").build());
        assertEquals(1, lessonDAOImpl.deleteLesson(id));
    }

    @ParameterizedTest(name = "{index}. When input not existed lesson id or negative number or zero will return false.")
    @ValueSource(ints = { 100, -1, 0 })
    void deleteLesson_shouldReturnCountOfDeletedRows_whenInputNotExistedID(int lessonID) {
        assertEquals(0, lessonDAOImpl.deleteLesson(lessonID));
    }

    @Test
    void isExists_shourtReturnTrue_whenInputExistedLessonID() {
        assertTrue(lessonDAOImpl.isLessonExists(1));
    }

    @ParameterizedTest(name = "{index}. When input not existed lesson id or negative number or zero will return false.")
    @ValueSource(ints = { 100, -1, 0 })
    void isExists_shourtReturnFalse_whenInputNotExistedLessonID(int lessonID) {
        assertFalse(lessonDAOImpl.isLessonExists(lessonID));
    }
}
