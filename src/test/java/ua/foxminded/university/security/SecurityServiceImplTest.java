package ua.foxminded.university.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.security.serivce.impl.SecurityServiceImpl;
import ua.foxminded.university.service.entities.Role;
import ua.foxminded.university.service.entities.User;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/security.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class SecurityServiceImplTest {

    @Autowired
    private SecurityServiceImpl securityService;
    @Autowired
    private UserDAO userDao;

    private String username = "username";
    private String password = "password";
    private User user;

    @BeforeEach
    void setup() {
        Role role = new Role("role");
        role.setId(1);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user = User.builder().id(1).firstName("name").lastName("surname").username(username)
                .password("$2a$12$4yx6frCLnxDjazevW8.1NeSiPxegodOxQXBJW1JY7/3/cWor4ERi6").roles(roles).build();
        userDao.save(user);
    }

    @Test
    void isAuthenticated_shouldReturnTrue_whenInputExistedData() {
        assertTrue(securityService.isAuthenticated(username, password));
    }

    @Test
    void isAuthenticated_shouldThrowInvalidUserException_whenInputNotExistedData() {
        String notExistedUsername = "none";
        assertThrows(BadCredentialsException.class, () -> securityService.isAuthenticated(notExistedUsername, password));
    }
}
