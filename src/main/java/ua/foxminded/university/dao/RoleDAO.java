package ua.foxminded.university.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.university.service.entities.Role;

public interface RoleDAO extends JpaRepository<Role, Integer>{        
    
    Optional<Role> findByName(String name);
}
