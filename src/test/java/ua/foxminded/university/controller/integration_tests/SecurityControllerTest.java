package ua.foxminded.university.controller.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.SecurityController;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.security.jwt.JwtTokenUtil;
import ua.foxminded.university.service.RoleService;
import ua.foxminded.university.service.dto.AuthenticationRequestDto;
import ua.foxminded.university.service.entities.Role;
import ua.foxminded.university.service.entities.User;

@SpringBootTest(classes = AppSpringBoot.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class SecurityControllerTest {

    @Autowired
    private SecurityController securityController;
    @Autowired
    private UserDAO userDao;
    @Autowired
    private RoleService roleService;
    @Mock
    private RedirectAttributes redirectAtt;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    private AuthenticationRequestDto userDto;
    private ResponseEntity<String> result;
    private User user;
    private String username = "username";
    private String password = "password";

    @BeforeEach
    void setup() {
        Role role = new Role("USER");
        roleService.addRole(role);
        user = User.builder().firstName("name").lastName("surname").username(username)
                .roles(Stream.of(role).collect(Collectors.toList()))
                .password("$2a$12$BfKJUIMpdG5vf2cz3h3yaePI9VlvGum92i4wA8dHmNuMT9EriVhP2").build();
        userDao.saveAndFlush(user);
        userDto = new AuthenticationRequestDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
    }

    @Test
    void showLoginPage_shouldReturnCorrectPage() {
        assertEquals("login", securityController.showLoginPage());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void login_shouldReturnStatus200_whenInputCorrectData() {
        String token = "token";
        given(jwtTokenUtil.generateToken(username)).willReturn(token);
        result = ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body("Welcome to Hogwarts, " + username);
        assertEquals(result, securityController.login(userDto));
    }

    @Test
    void login_shouldReturnStatus401_whenInputIncorrectData() {
        userDto = new AuthenticationRequestDto();
        userDto.setUsername("");
        userDto.setPassword(password);
        result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        assertEquals(result, securityController.login(userDto));
    }

    @Test
    void showTokenExpired_shouldReturnCorrectPage() {
        result = ResponseEntity.status(response.getStatus())
                .body("JWT Token expired. " + request.getAttribute("exception"));
        assertEquals(result, securityController.showTokenExpired(request, response));
    }

    @Test
    void logoutPage() {
        assertEquals("redirect:/", securityController.logoutPage(request, response));
    }

    @Test
    void errorLogin() {
        assertEquals("login", securityController.errorLogin(redirectAtt));
    }
}
