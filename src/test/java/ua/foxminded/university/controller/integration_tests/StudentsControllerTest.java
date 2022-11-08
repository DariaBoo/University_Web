package ua.foxminded.university.controller.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.StudentsController;
import ua.foxminded.university.dao.RoleDAO;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Role;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.User;

@SpringBootTest(classes = AppSpringBoot.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class StudentsControllerTest {

    @Autowired
    private StudentsController studentsController;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserDAO userDao;
    @Autowired
    private RoleDAO roleDao;
    @Autowired
    private GroupService groupService;
    @Mock
    private RedirectAttributes redirectAtt;
    @Mock
    private BindingResult bindingResult;
    private Model model = new ConcurrentModel();
    private Student student;
    private Student student2;
    private User user;
    private Group group;

    @BeforeEach
    void setup() {
        Role role = new Role("STUDENT");
        roleDao.save(role);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user = User.builder().firstName("name").lastName("surname").username("username").password("password").build();
        userDao.save(user);
        group = Group.builder().name("AA-00").departmentId(1).build();
        groupService.addGroup(group);
        student = Student.builder().user(user).group(group).idCard("AA-1").build();
    }

    @Test
    void listAllStudents_shouldReturnCorrectPage() {
        assertEquals("students/list", studentsController.listAllStudents(model));
    }

    @Test
    void viewStudentById_shouldReturnCorrectPage() {
        studentService.addStudent(student);
        assertEquals("students/view", studentsController.viewStudentById(student.getId(), model));
    }

    @Test
    void createNewStudent_shouldReturnCorrectPage() {
        assertEquals("students/new", studentsController.createNewStudent(student, model));
    }

    @Test
    void saveNewStudent_shouldThrowConstraintViolationException() {
        String blankIdCard = "";
        student2 = Student.builder().user(user).group(group).idCard(blankIdCard).build();
        assertThrows(ConstraintViolationException.class,
                () -> studentsController.saveNewStudent(student2, bindingResult, redirectAtt));
    }

    @Test
    void saveNewStudent_shouldReturnCorrectPage() {
        assertEquals("redirect:/app/students", studentsController.saveNewStudent(student, bindingResult, redirectAtt));
    }

    @Test
    void deleteStudent_shouldReturnCorrectPage_whenInputExistedId() {
        studentService.addStudent(student);
        assertEquals("redirect:/app/students", studentsController.deleteStudent(student.getId(), redirectAtt));
    }

    @Test
    void deleteStudent_shouldReturnCorrectPage_whenInputNotExistedId() {
        assertEquals("redirect:/app/students", studentsController.deleteStudent(student.getId(), redirectAtt));
    }

    @Test
    void edit_shouldReturnCorrectPage_whenInputExistedId() {
        studentService.addStudent(student);
        assertEquals("students/edit", studentsController.edit(student.getId(), model, redirectAtt));
    }

    @Test
    void edit_shouldReturnCorrectPage_whenInputNotExistedId() {
        assertEquals("students/edit", studentsController.edit(student.getId(), model, redirectAtt));
    }

    @Test
    void update_shouldReturnCorrectPage_whenInputExistedId() {
        studentService.addStudent(student);
        assertEquals("redirect:/app/students", studentsController.update(student, bindingResult, redirectAtt));
    }

    @Test
    void update_shouldReturnCorrectPage_whenInputNotExistedId() {
        assertEquals("redirect:/app/students", studentsController.update(student, bindingResult, redirectAtt));
    }

    @Test
    void changePassword_shouldReturnCorrectPage() {
        studentService.addStudent(student);
        ResponseEntity<String> result = new ResponseEntity<>("Password was changed successfully!", HttpStatus.OK);
        assertEquals(result, studentsController.changePassword(student.getId(), "newPassword"));
    }
}
