package ua.foxminded.university.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
@Order(1)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter{
    
    private static final String ADMIN = "ADMIN";

    @Autowired
    private AuthorisedUserDetailsService userDetailsService;
        
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }  

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .authorizeRequests()      
        .antMatchers("/university/admin").permitAll()
        .antMatchers("/university/admin/**").hasRole(ADMIN)  
        .antMatchers("/university/**").hasRole(ADMIN)  
        .and()
        .logout()
        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(Directive.ALL)))
        .logoutSuccessUrl("/admin").invalidateHttpSession(true)
        .clearAuthentication(true)
//        .and().sessionManagement()
//        .maximumSessions(1)
        .and()
        .csrf().disable(); 
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
