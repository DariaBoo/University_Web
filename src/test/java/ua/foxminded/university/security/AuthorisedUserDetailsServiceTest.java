package ua.foxminded.university.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.security.model.AuthenticatedUser;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.RoleEnum;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppSpringBoot.class)
@Sql({"/login_test.sql"})
class AuthorisedUserDetailsServiceTest {
    
    @Autowired
    private UserDetailsServiceImpl service;
    @Autowired
    private BCryptPasswordEncoder encoder;
    private final Group group = new Group();
    private final String studentUsername = "student_1";
    private final String adminUsername = "ADMIN";
    
    @Test
    void test() {
        group.setId(1);
        String test = "$2a$10$cS44nbWAC4VRI47tJchy3emzaf.HrGmjEOFpi2zXFCNeo84eXgSqu";
//        User user = (User) Student.builder().password(test).role(RoleEnum.USER).username(studentUsername).build();
//        AuthorisedUser authorisedUser = new AuthorisedUser(user);
//        assertEquals(authorisedUser , service.loadUserByUsername(studentUsername));
    }
    
    @Test
    void loadUserByUsername_whenLoadStaff() {
        assertNotNull(service.loadUserByUsername(adminUsername));
    }
}
