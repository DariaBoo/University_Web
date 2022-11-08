package ua.foxminded.university.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.controller.urls.URL;
import ua.foxminded.university.security.jwt.JwtTokenUtil;
import ua.foxminded.university.security.service.SecurityService;
import ua.foxminded.university.service.dto.AuthenticationRequestDto;

@SpringBootTest(classes = { AppSpringBoot.class })
@TestInstance(Lifecycle.PER_CLASS)
class SecurityControllerUnitTest {

    @MockBean
    private SecurityService securityService;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private SecurityController securityController;
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BindingResult bindingResult;
    @Mock
    private Model model;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RedirectAttributes redirectAtt;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    private AuthenticationRequestDto notValidUserDto;
    private AuthenticationRequestDto validUserDto;

    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        notValidUserDto = new AuthenticationRequestDto();
        notValidUserDto.setUsername(null);
        notValidUserDto.setPassword("password");
        validUserDto = new AuthenticationRequestDto();
        validUserDto.setUsername("username");
        validUserDto.setPassword("password");
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void showLoginPage_shouldReturnStatus200() throws Exception {
        mockMvc.perform(get(URL.WELCOME).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        assertEquals("login", securityController.showLoginPage());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void login_shouldReturnStatus200_whenEntityHasNoErrors() throws Exception {
        given(bindingResult.hasErrors()).willReturn(false);
        given(securityService.isAuthenticated(any(String.class), any(String.class))).willReturn(true);
        given(jwtTokenUtil.generateToken(any(String.class))).willReturn(new String());
        mockMvc.perform(post(URL.LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDto))).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void login_shouldReturnStatus401() throws Exception {
        given(bindingResult.hasErrors()).willReturn(false);
        given(securityService.isAuthenticated(any(String.class), any(String.class)))
                .willThrow(BadCredentialsException.class);
        mockMvc.perform(post(URL.LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDto))).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void errorLogin_shouldReturnStatus401() throws Exception {
        mockMvc.perform(get(URL.LOGIN_ERROR).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN" })
    void logoutPage_shouldReturnStatus302() throws Exception {
        mockMvc.perform(get(URL.LOGOUT).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is3xxRedirection());
        assertEquals("redirect:/", securityController.logoutPage(request, response));
    }
}
