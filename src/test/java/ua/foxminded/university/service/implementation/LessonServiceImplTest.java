package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.config.HibernateConfigTest;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.exception.ServiceException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateConfigTest.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
class LessonServiceImplTest {

    @Autowired
    private LessonService lessonService;

    private Lesson lesson;
    private Exception exception;
    private String expectedMessage;
    private String actualMessage;

    @BeforeEach
    void init() {
        new EmbeddedDatabaseBuilder().setName("test").setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:tablesTest.sql").build();
    }

    @Test
    void addLesson_shouldReturnResult_whenInputCorrectLessonData() {
        lesson = new Lesson.LessonBuilder().setName("Test").setDescription("test").build();
        assertEquals(lessonService.findAllLessons().size() + 1, lessonService.addLesson(lesson));
    }

    @Test
    void addLesson_shouldReturnResult_whenInputNotUniqueLessonName() {
        lesson = new Lesson.LessonBuilder().setName("Test").setDescription("test").build();
        lessonService.addLesson(lesson);
        Lesson notUniqueName = new Lesson.LessonBuilder().setName("Test").setDescription("test").build();
        exception = assertThrows(ServiceException.class, () -> lessonService.addLesson(notUniqueName));
        expectedMessage = "Lesson with name Test already exists!";
        actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
