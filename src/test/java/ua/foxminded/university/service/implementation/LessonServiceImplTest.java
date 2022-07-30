package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.exception.ServiceException;
import ua.foxminded.university.springboot.AppSpringBoot;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppSpringBoot.class)
//@Sql({"/schema.sql", "/data.sql"})
@ActiveProfiles("test")
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
        lesson = Lesson.builder().name("Test").description("test").build();
        assertEquals(lessonService.findAllLessons().size() + 1, lessonService.addLesson(lesson));
    }

    @Test
    void addLesson_shouldReturnResult_whenInputNotUniqueLessonName() {
        lesson = Lesson.builder().name("Test").description("test").build();
        lessonService.addLesson(lesson);
        Lesson notUniqueName = Lesson.builder().name("Test").description("test").build();
        exception = assertThrows(ServiceException.class, () -> lessonService.addLesson(notUniqueName));
        expectedMessage = "Lesson with name Test already exists!";
        actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
