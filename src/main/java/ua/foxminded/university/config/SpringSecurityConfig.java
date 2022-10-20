package ua.foxminded.university.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ua.foxminded.university.security.Roles;
import ua.foxminded.university.security.UserDetailsServiceImpl;
import ua.foxminded.university.security.jwt.JwtAuthenticationEntryPoint;
import ua.foxminded.university.security.jwt.JwtTokenFilter;
import ua.foxminded.university.security.success_handler.UrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String STUDENT_URL = "/student/**";
    private static final String TEACHER_URL = "/teacher/**";
    private static final String ADMIN_URL = "/app/**";
    private static final String WELCOME_URL = "/";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_URL = "/logout";
    private static final String LOGIN_ERROR_URL = "/login_error";
    private static final String[] staticResources = {"/css/**", "/img/**", "/navbar/**"};

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .authorizeRequests()
        .mvcMatchers(staticResources).permitAll()
        .mvcMatchers(STUDENT_URL).hasAnyAuthority(Roles.ROLE_ADMIN, Roles.ROLE_STUDENT)
        .mvcMatchers(TEACHER_URL).hasAnyAuthority(Roles.ROLE_ADMIN, Roles.ROLE_TEACHER)
        .mvcMatchers(ADMIN_URL).hasAuthority(Roles.ROLE_ADMIN)
        .mvcMatchers(WELCOME_URL, LOGIN_ERROR_URL).permitAll()
        .mvcMatchers().denyAll() 
        .and()
        .formLogin().loginPage(LOGIN_URL).successHandler(authenticationSuccessHandler()).failureUrl(LOGIN_ERROR_URL)
        .and()
        .logout().logoutUrl(LOGOUT_URL)
        .logoutSuccessUrl(WELCOME_URL)
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID");    
        http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new UrlAuthenticationSuccessHandler();
    }
}
