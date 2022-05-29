package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.service.pojo.Lesson;

class LessonDAOImplTest {
    private LessonDAOImpl lessonDAOImpl;
    private AnnotationConfigApplicationContext context;
    private Lesson lesson;
    private List<Lesson> lessons = new ArrayList<Lesson>();
    private final int maxLessonNameSize = 20;
    private final int maxDexcriptionSize = 100;

    @BeforeEach
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        lessonDAOImpl = context.getBean("lessonDAOImpl", LessonDAOImpl.class);
    }

    @Test
    void addLesson_shouldReturnAddedLessonID_whenInputNewLesson() throws SQLException {
        assertEquals(1, lessonDAOImpl
                .addLesson(new Lesson.LessonBuilder().setName("Quidditch").setDescription("sport").build()));
        lessonDAOImpl.deleteLesson(1);
    }

    @Test
    void addLesson_shouldReturnNegativeNumber_whenInputExistedLesson() throws SQLException {
        assertEquals(0, lessonDAOImpl.addLesson(new Lesson.LessonBuilder().setName("Alchemy").build()));
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
    void findByID_shouldReturnLesson_whenInputExistedLessonID() {
        lesson = new Lesson.LessonBuilder().setID(1).setName("Alchemy").setDescription("the study of transmutation of substances into other forms.").build();
        assertEquals(Optional.of(lesson), lessonDAOImpl.findByID(1));
    }
    
    @Test
    void findByID_shouldReturnEmptyOptional_whenInputNotExistedLessonID() {
        assertEquals(Optional.empty(), lessonDAOImpl.findByID(100));
    }
    
    @Test
    void findByID_shouldReturnEmptyOptional_whenInputNegativeNumber() {
        assertEquals(Optional.empty(), lessonDAOImpl.findByID(-1));
    }
    
    @Test
    void findAllLessons_shouldReturnCountOfLessons_whenCallTheMethod() {
        assertEquals(5, lessonDAOImpl.findAllLessons().get().stream().count());
    }
    
    @Test
    void findAllLessons_shouldReturnFirstThreeLessons_whenCallTheMethod() {
        lessons.add(new Lesson.LessonBuilder().setID(1).setName("Alchemy").setDescription("the study of transmutation of substances into other forms.").build());
        lessons.add(new Lesson.LessonBuilder().setID(2).setName("Herbology").setDescription("the study of magical plants and how to take care of, utilise and combat them.").build());
        lessons.add(new Lesson.LessonBuilder().setID(3).setName("History of Magic").setDescription("the study of magical history.").build());
        assertEquals(lessons, lessonDAOImpl.findAllLessons().get().stream().limit(3).collect(Collectors.toList()));
    }
    
    @Test
    void updateLesson_shouldReturnOne_whenInputExistedLessonID() {
        lesson = new Lesson.LessonBuilder().setID(1).setName("Quidditch").setDescription("sport").build();
        assertEquals(1, lessonDAOImpl.updateLesson(lesson));
    }
    
    @Test
    void updateLesson_shouldReturnOne_whenInputExistedLessonIDAndNullName() {
        lesson = new Lesson.LessonBuilder().setID(1).setDescription("sport").build();
        assertEquals(1, lessonDAOImpl.updateLesson(lesson));
    }
    
    @Test
    void updateLesson_shouldReturnOne_whenInputExistedLessonIDAndNullDescription() {
        lesson = new Lesson.LessonBuilder().setID(1).setName("Quidditch").setDescription(null).build();
        assertEquals(1, lessonDAOImpl.updateLesson(lesson));
    }
    
    @Test
    void updateLesson_shouldReturnOne_whenInputNotExistedLessonID() {
        lesson = new Lesson.LessonBuilder().setID(100).setName("Quidditch").setDescription("sport").build();
        assertEquals(0, lessonDAOImpl.updateLesson(lesson));
    }
    
    @Test
    void getLessonNameMaxSize_shouldReturnColumnMaxSize_whenCallTheMethod() {
        assertEquals(maxLessonNameSize, lessonDAOImpl.getLessonNameMaxSize());
    }
    
    @Test
    void getDescriptionMaxSize_shouldReturnColumnMaxSize_whenCallTheMethod() {
        assertEquals(maxDexcriptionSize, lessonDAOImpl.getDescriptionMaxSize());
    }
}
