package ua.foxminded.university.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"ua.foxminded.university"})
public class AppSpringBoot extends SpringBootServletInitializer {

    public static void main(String[] args) {
        System.setProperty("server.servlet.context-path", "/university");
        SpringApplication.run(AppSpringBoot.class, args);
    }
}
