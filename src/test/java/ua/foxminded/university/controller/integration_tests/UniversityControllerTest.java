package ua.foxminded.university.controller.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.UniversityController;

@SpringBootTest(classes = AppSpringBoot.class)
class UniversityControllerTest {

    @Autowired
    private UniversityController universityController;

    @Test
    void showHomePage() {
        assertEquals("/home", universityController.showHomePage());
    }

    @Test
    void showStudentPage() {
        assertEquals("students/studentPage", universityController.showStudentPage());
    }

    @Test
    void showTeacherPage() {
        assertEquals("teachers/teacherPage", universityController.showTeacherPage());
    }
}
