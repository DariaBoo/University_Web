package ua.foxminded.university.controller.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.LessonsController;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.entities.Lesson;

@SpringBootTest(classes = AppSpringBoot.class)
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
class LessonsControllerTest {

    @Autowired
    private LessonsController lessonsController;
    @Autowired
    private LessonService lessonService;
    private Model model = new ConcurrentModel();
    private Lesson lesson;

    @BeforeAll
    void setup() {
        lesson = Lesson.builder().id(1).name("lesson").description("description").build();
        lessonService.addLesson(lesson);
    }

    @Test
    void listAllLessons_shouldReturnCorrectPage() {
        assertEquals("lessons/list", lessonsController.listAllLessons(model));
    }

    @Test
    void viewLessonById_shouldReturnCorrectPage() {
        assertEquals("lessons/view", lessonsController.viewLessonById(lesson.getId(), model));
    }
}
