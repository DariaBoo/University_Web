package ua.foxminded.university.controller.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.TeachersController;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.service.RoleService;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.entities.Role;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.User;

@SpringBootTest(classes = AppSpringBoot.class)
@TestInstance(Lifecycle.PER_CLASS)
class TeachersControllerTest {

    @Autowired
    private TeachersController teachersController;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserDAO userDao;
    private Model model = new ConcurrentModel();
    private Teacher teacher;

    @BeforeAll
    void setup() {
        Role role = new Role("TEACHER");
        roleService.addRole(role);
        User user = User.builder().firstName("name").lastName("surname").username("username").password("password")
                .build();
        userDao.saveAndFlush(user);
        teacher = Teacher.builder().user(user).departmentId(1).position("position").build();
        teacherService.addTeacher(teacher);
    }

    @Test
    void listAllTeachers_shouldReturnCorrectPage() {
        assertEquals("teachers/list", teachersController.listAllTeachers(model));
    }

    @Test
    void viewTeacherById_shouldReturnCorrectPage() {
        assertEquals("teachers/view", teachersController.viewTeacherById(teacher.getId(), model));
    }
}
