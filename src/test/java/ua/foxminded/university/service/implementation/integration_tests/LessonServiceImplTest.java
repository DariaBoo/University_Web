package ua.foxminded.university.service.implementation.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.exception.EntityConstraintViolationException;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/lessons.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class LessonServiceImplTest {

    @Autowired
    private LessonService lessonService;
    private Lesson lesson;

    @BeforeEach
    void setup() {
        lesson = Lesson.builder().id(1).name("lesson").description("description").build();
    }

    @Test
    void addLesson_shouldReturnSavedLesson_whenInputNewLesson() {
        assertEquals(lesson, lessonService.addLesson(lesson));
    }

    @Test
    void addLesson_shouldThrowUniqueConstraintViolationException_whenInputLongName() {
        lessonService.addLesson(lesson);
        String notUniqueName = lesson.getName();
        Lesson lesson2 = Lesson.builder().id(1).name(notUniqueName).description("description").build();
        assertThrows(EntityConstraintViolationException.class, () -> lessonService.addLesson(lesson2));
    }

    @Test
    void updateLesson_shouldReturnUpdatedLesson_whenInputCorrectData() {
        lessonService.addLesson(lesson);
        String newName = "lesson2";
        Lesson updatedLesson = lesson = Lesson.builder().id(1).name(newName).description("description").build();
        assertEquals(updatedLesson.getName(), lessonService.updateLesson(updatedLesson).getName());
    }

    @Test
    void updateLesson_shouldThrowUniqueConstraintViolationException_whenInputCorrectData() {
        String notUniqueName = "lesson";
        lessonService.addLesson(lesson);
        Lesson updatedLesson = lesson = Lesson.builder().id(1).name(notUniqueName).description("description").build();
        assertThrows(EntityConstraintViolationException.class, () -> lessonService.updateLesson(updatedLesson));
    }

    @Test
    void deleteLesson_shouldReturnTrue_whenInputExistedId() {
        lessonService.addLesson(lesson);
        lessonService.deleteLesson(lesson.getId());
        assertEquals(0, lessonService.findAllLessons().size());
    }

    @Test
    void deleteLesson_shouldThrowEntityNotFoundException_whenInputNotExistedId() {
        int notExistedId = lessonService.findAllLessons().size();
        assertThrows(EntityNotFoundException.class, () -> lessonService.deleteLesson(notExistedId));
    }

    @Test
    void findAllLessons_shouldReturnList() {
        lessonService.addLesson(lesson);
        Lesson lesson2 = Lesson.builder().name("lesson2").description("description").build();
        lessonService.addLesson(lesson2);
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson);
        lessons.add(lesson2);
        assertEquals(lessons.size(), lessonService.findAllLessons().size());
    }

    @Test
    void findById_shouldReturnLesson_whenInputExistedId() {
        lessonService.addLesson(lesson);
        assertEquals(lesson, lessonService.findById(lesson.getId()));
    }

    @Test
    void findById_shouldThrowIllegalArgumentException_whenInputNotExistedId() {
        int notExistedId = lessonService.findAllLessons().size();
        assertThrows(IllegalArgumentException.class, () -> lessonService.findById(notExistedId));
    }
}
