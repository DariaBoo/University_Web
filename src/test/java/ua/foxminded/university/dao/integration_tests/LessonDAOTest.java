package ua.foxminded.university.dao.integration_tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.service.entities.Lesson;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/lessons.sql" })
class LessonDAOTest {

    @Autowired
    private LessonDAO lessonDao;
    private Lesson lesson;

    @BeforeEach
    void setup() {
        lesson = Lesson.builder().name("lesson").description("description").build();
    }

    @Test
    void save() {
        assertNotNull(lessonDao.save(lesson));
        assertEquals(lesson, lessonDao.save(lesson));
        assertNotNull(lessonDao.findByName("lesson"));
    }

    @Test
    void findById() {
        lessonDao.save(lesson);
        Lesson result = lessonDao.findById(lesson.getId()).get();
        assertEquals(lesson, result);
    }

    @Test
    void findAll() {
        lessonDao.save(lesson);
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson);
        assertEquals(lessons, lessonDao.findAll());
    }

    @Test
    void deleteById() {
        lessonDao.save(lesson);
        lessonDao.deleteById(lesson.getId());
        assertEquals(0, lessonDao.findAll().size());
    }

    @Test
    void existById() {
        lessonDao.save(lesson);
        assertTrue(lessonDao.existsById(lesson.getId()));
    }

    @Test
    void findByName() {
        lessonDao.save(lesson);
        assertEquals(lesson, lessonDao.findByName(lesson.getName()));
    }
}
