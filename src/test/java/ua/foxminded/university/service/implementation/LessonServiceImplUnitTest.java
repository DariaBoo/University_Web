package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.exception.UniqueConstraintViolationException;

@ExtendWith(SpringExtension.class)
class LessonServiceImplUnitTest {

    @Mock
    private LessonDAO lessonDao;

    @InjectMocks
    private LessonServiceImpl lessonService;

    private Lesson lesson;
    private Lesson lesson2;

    @BeforeEach
    void setup() {
        lesson = Lesson.builder().id(1).name("name").description("description").build();
        lesson2 = Lesson.builder().id(2).name("name2").description("description").build();
    }

    @Test
    void addLesson_shouldReturnAddedLesson_whenAddNewLesson() {
        given(lessonDao.save(lesson)).willReturn(lesson);
        given(lessonDao.existsById(lesson.getId())).willReturn(true);
        assertEquals(lesson, lessonService.addLesson(lesson));
        verify(lessonDao, times(1)).save(any(Lesson.class));
    }

    @Test
    void addLesson_shouldThrowUniqueConstraintViolationException_whenInputNotUniqueName() {
        given(lessonDao.findByName(lesson.getName())).willReturn(lesson);
        assertThrows(UniqueConstraintViolationException.class, () -> lessonService.addLesson(lesson));
        verify(lessonDao, times(0)).save(any(Lesson.class));
    }

    @Test
    void updateLesson() {
        String newName = "newName";
        String newDescription = "newDescription";
        given(lessonDao.save(lesson)).willReturn(lesson);
        lesson.setName(newName);
        lesson.setDescription(newDescription);
        Lesson updatedLesson = lessonService.updateLesson(lesson);

        assertEquals(updatedLesson.getName(), newName);
        assertEquals(updatedLesson.getDescription(), newDescription);
    }

    @Test
    void updateLesson_shouldThrowUniqueConstraintViolationException_whenInputNotUniqueName() {
        given(lessonDao.findByName(lesson.getName())).willReturn(lesson);
        assertThrows(UniqueConstraintViolationException.class, () -> lessonService.updateLesson(lesson));
        verify(lessonDao, times(0)).save(any(Lesson.class));
    }

    @Test
    void deleteLesson() {
        int lessonId = 1;
        willDoNothing().given(lessonDao).deleteById(lessonId);
        lessonService.deleteLesson(lessonId);
        verify(lessonDao, times(1)).deleteById(lessonId);
    }

    @Test
    void findById_shouldReturnLesson_whenInputExistedId() {
        int lessonId = 1;
        given(lessonDao.findById(lessonId)).willReturn(Optional.of(lesson));
        Lesson savedLesson = lessonService.findById(lesson.getId());
        assertNotNull(savedLesson);
        assertEquals(lesson, savedLesson);
    }

    @Test
    void findById_shouldReturnIllegalArgumentException_whenInputNotExistedLessonId() {
        int lessonId = 100;
        given(lessonDao.findById(lessonId)).willReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> lessonService.findById(lessonId));
    }

    @Test
    void findAllLessons_shouldReturnCountOfLessons_whenCallTheMethod() {
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson);
        lessons.add(lesson2);
        given(lessonDao.findAll()).willReturn(lessons);

        List<Lesson> lessonList = lessonService.findAllLessons();
        assertNotNull(lessonList);
        assertEquals(2, lessonList.size());
    }

    @Test
    void findAllLessons_shouldReturnEmptyList_whenCallTheMethod() {
        given(lessonDao.findAll()).willReturn(new ArrayList<Lesson>());
        List<Lesson> lessonList = lessonService.findAllLessons();
        assertEquals(0, lessonList.size());
    }
}
