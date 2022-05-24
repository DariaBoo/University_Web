package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.service.exception.ServiceException;
import ua.foxminded.university.service.pojo.Lesson;

class LessonServiceImplTest {
    private LessonServiceImpl lessonServiceImpl;
    private AnnotationConfigApplicationContext context;
    private Lesson lesson;
    private Exception exception;
    private String expectedMessage;
    private String actualMessage;
    private final int maxLessonNameSize = 20;
    private final int maxDexcriptionSize = 100;
    
    @BeforeEach
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        this.lessonServiceImpl = context.getBean("lessonServiceImpl", LessonServiceImpl.class);
    }

    @Test
    void addLesson_shouldThrowServiceException_whenInputIncorrectLessonData() {
        lesson = new Lesson.LessonBuilder().setName(createCountOfSymbols(maxLessonNameSize + 1)).setDescription(createCountOfSymbols(maxDexcriptionSize + 1)).build();
        assertThrows(ServiceException.class, () -> lessonServiceImpl.addLesson(lesson));
        
    }
    @Test
    void addLesson_shouldReturnResult_whenInputCorrectLessonData() throws ServiceException {
        lesson = new Lesson.LessonBuilder().setName("Quidditch").setDescription("sport").build();
        assertEquals(1, lessonServiceImpl.addLesson(lesson));
    }
    @Test
    void addLesson_shouldThrowServiceExceptionMessage_whenInputIncorrectLessonName() {
        lesson = new Lesson.LessonBuilder().setName(createCountOfSymbols(maxLessonNameSize + 1)).setDescription("sport").build();
        exception = assertThrows(ServiceException.class, () -> lessonServiceImpl.addLesson(lesson));
        expectedMessage = "Lesson name is out of bound.";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void addLesson_shouldThrowServiceExceptionMessage_whenInputIncorrectDescription() {
        lesson = new Lesson.LessonBuilder().setName("Quidditch").setDescription(createCountOfSymbols(maxDexcriptionSize + 1)).build();
        exception = assertThrows(ServiceException.class, () -> lessonServiceImpl.addLesson(lesson));
        expectedMessage = "Lesson description is out of bound.";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void updateLesson_shouldThrowServiceException_whenInputIncorrectLessonData() {
        lesson = new Lesson.LessonBuilder().setID(1).setName(createCountOfSymbols(maxLessonNameSize + 1)).setDescription(createCountOfSymbols(maxDexcriptionSize + 1)).build();
        assertThrows(ServiceException.class, () -> lessonServiceImpl.updateLesson(lesson));
    }
    @Test
    void updateLesson_shouldReturnResult_whenInputCorrectLessonData() throws ServiceException {
        lesson = new Lesson.LessonBuilder().setID(1).setName("Quidditch").setDescription("sport").build();
        assertEquals(1, lessonServiceImpl.updateLesson(lesson));
    }
    @Test
    void updateLesson_shouldThrowServiceExceptionMessage_whenInputIncorrectLessonName() {
        lesson = new Lesson.LessonBuilder().setName(createCountOfSymbols(maxLessonNameSize + 1)).setDescription("sport").build();
        exception = assertThrows(ServiceException.class, () -> lessonServiceImpl.updateLesson(lesson));
        expectedMessage = "Lesson name is out of bound.";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void updateLesson_shouldThrowServiceExceptionMessage_whenInputIncorrectDescription() {
        lesson = new Lesson.LessonBuilder().setName("Quidditch").setDescription(createCountOfSymbols(maxDexcriptionSize + 1)).build();
        exception = assertThrows(ServiceException.class, () -> lessonServiceImpl.updateLesson(lesson));
        expectedMessage = "Lesson description is out of bound.";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
           
    private String createCountOfSymbols(int count) {
        return Stream.generate(() -> "a")
                .limit(count)
                .collect(Collectors.joining());
    }
}
