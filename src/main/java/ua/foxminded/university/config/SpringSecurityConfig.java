package ua.foxminded.university.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ua.foxminded.university.security.AuthorisedUserDetailsService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
    
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    @Autowired
    private AuthorisedUserDetailsService userDetailsService;
        
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers("/university/user_logged").hasAnyRole(USER, ADMIN)
        .antMatchers("/university/student/timetable").hasAnyRole(USER, ADMIN)
        .antMatchers("/university/teacher/timetable").hasAnyRole(USER, ADMIN)
        .antMatchers("/university/timetable/**").hasRole(ADMIN)
        .antMatchers("/university").permitAll()        
        .anyRequest().authenticated()
        .and()
        .logout()
        .logoutSuccessUrl("/university").invalidateHttpSession(true)//.clearAuthentication(true)
        .deleteCookies("JSESSIONID")
        .and()
        .formLogin().disable();
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
