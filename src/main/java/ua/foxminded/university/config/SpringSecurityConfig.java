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

import ua.foxminded.university.security.UserDetailsServiceImpl;

@Configuration
//@ComponentScan(basePackages = { "ua.foxminded.university.security" })
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

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
        .httpBasic().and()
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/", "/login", "/welcome", "/student/login", "teacher/login", "/admin", "/admin/login", "/home", "/user/logout").permitAll()
        .and()
        .authorizeRequests()
        .antMatchers("/student", "/teacher", "/timetable").hasAuthority("ADMIN")
        .anyRequest().denyAll() 
//        .and()
//        .httpBasic().disable()
        .and()
        .logout()
        .logoutUrl("/user/logout")
        .logoutSuccessUrl("/welcome");//invalidateHttpSession(true)
//        .deleteCookies("JSESSIONID");
//        .authorizeRequests()
//        .mvcMatchers("/welcome").permitAll()
//        .mvcMatchers("/welcome/student/login").hasRole(ADMIN)
//                .antMatchers("/login", "/").hasAnyRole(USER, ADMIN)
//                .antMatchers("/login", "/").permitAll()
//                .antMatchers("/welcome/student/login").hasAnyRole(USER, ADMIN)
//                .anyRequest().denyAll()
//                .and().logout().logoutSuccessUrl("/");
//        .antMatchers("/university/welcome/student/login").hasAnyRole(USER, ADMIN)
//        .antMatchers("/university/welcome/teacher/login").hasAnyRole(USER, ADMIN)
//        .antMatchers("/university/welcome").permitAll()        
//        .antMatchers("/university/student/timetable").hasAnyRole(USER, ADMIN)
//        .antMatchers("/university/teacher/timetable").hasAnyRole(USER, ADMIN) 
//        .antMatchers("/university/timetable/**").hasRole(ADMIN)
//        .antMatchers("/university/admin").permitAll()
//        .antMatchers("/university/admin/login").hasRole(ADMIN)
//        .antMatchers("/university/admin/**").hasRole(ADMIN)
//        .antMatchers("/university/**").hasRole(ADMIN)
//        .anyRequest().authenticated()
//        .and().sessionManagement().maximumSessions(3);
//        .anyRequest().authenticated();
//        .and()
//        .logout()
//        .logoutSuccessUrl("/university").invalidateHttpSession(true)//.clearAuthentication(true)
//        .deleteCookies("JSESSIONID");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
