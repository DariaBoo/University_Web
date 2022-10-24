package ua.foxminded.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"ua.foxminded.university"})
@EnableJpaRepositories(basePackages = "ua.foxminded.university.dao")
@EntityScan(basePackages = "ua.foxminded.university.service.entities")
public class AppSpringBoot extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AppSpringBoot.class, args);
    }
}
