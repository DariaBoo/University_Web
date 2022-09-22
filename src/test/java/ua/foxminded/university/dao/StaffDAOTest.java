package ua.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.foxminded.university.springboot.AppSpringBoot;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppSpringBoot.class)
@Sql({"/staff_test.sql"})
class StaffDAOTest {
    
    private static final String USERNAME = "USERNAME";    
    @Autowired
    private StaffDAO repository;

    @Test
    void findAll() {
        assertEquals(3, repository.findAll().size());
    }
    
    @Test
    void findByUsername() {
        assertEquals(USERNAME, repository.findByUsername(USERNAME).getUsername());
    }
}
