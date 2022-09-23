package ua.foxminded.university.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive;

import ua.foxminded.university.security.AuthorisedUserDetailsService;

@Configuration
@EnableWebSecurity
@Order(2)
public class UserSecurityConfig extends WebSecurityConfigurerAdapter{

    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    @Autowired
    private AuthorisedUserDetailsService userDetailsService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    } 
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .authorizeRequests() 
        .antMatchers("/university/welcome/student/login").hasAnyRole(USER, ADMIN)
        .antMatchers("/university/welcome/teacher/login").hasAnyRole(USER, ADMIN)
        .antMatchers("/university/welcome").permitAll()        
        .antMatchers("/university/student/timetable").hasAnyRole(USER, ADMIN)
        .antMatchers("/university/teacher/timetable").hasAnyRole(USER, ADMIN) 
        .anyRequest().authenticated()
        .and()
        .logout()
//        .logoutRequestMatcher(new AntPathRequestMatcher("/"))
//        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(Directive.ALL)))
        .logoutSuccessUrl("/welcome").invalidateHttpSession(true)
        .clearAuthentication(true)
//        .and().sessionManagement()
//        .maximumSessions(1)
        .and()
        .csrf().disable(); 
//        .anyRequest().authenticated()
//        .and()
//        .logout()
//        .logoutSuccessUrl("/university").invalidateHttpSession(true)
//        .deleteCookies("JSESSIONID")
//        .and()
//        .formLogin().disable();
    }
}
