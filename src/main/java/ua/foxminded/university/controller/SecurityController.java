package ua.foxminded.university.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.controller.urls.URL;
import ua.foxminded.university.security.jwt.JwtTokenUtil;
import ua.foxminded.university.security.service.SecurityService;
import ua.foxminded.university.service.dto.AuthenticationRequestDto;

@Slf4j
@Controller
public class SecurityController {

    @Autowired
    private SecurityService securityService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping(URL.WELCOME)
    public String showLoginPage() {
        return "login";
    }

    @PostMapping(URL.LOGIN)
    public ResponseEntity<String> login(@RequestBody AuthenticationRequestDto userDto) {
        String username = userDto.getUsername();
        try {
            String token = null;
            boolean isAuthenticated = securityService.isAuthenticated(username, userDto.getPassword());
            if (isAuthenticated) {
                token = jwtTokenUtil.generateToken(username);
            }
            log.info("[ON login]:: setting header and token to ResponseEntity");
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token)
                    .body("Welcome to Hogwarts, " + username);
        } catch (BadCredentialsException e) {
            log.error("[ON login]:: {}", e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(URL.EXPIRED_JWT)
    public ResponseEntity<String> showTokenExpired(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(response.getStatus())
                .body("JWT Token expired. " + request.getAttribute("exception"));
    }

    @GetMapping(URL.LOGOUT)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    @GetMapping(URL.LOGIN_ERROR)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public String errorLogin(RedirectAttributes redirectAtt) {
        redirectAtt.addFlashAttribute("message", "You have no authority to access");
        return "login";
    }
}
