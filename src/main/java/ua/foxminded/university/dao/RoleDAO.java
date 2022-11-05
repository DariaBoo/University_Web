package ua.foxminded.university.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.university.service.entities.Role;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface RoleDAO extends JpaRepository<Role, Integer> {

    /**
     * Finds role by name
     * 
     * @param name
     * @return optional role
     */
    Optional<Role> findByName(String name);
}
