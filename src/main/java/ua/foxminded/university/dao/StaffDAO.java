package ua.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.university.service.entities.Staff;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface StaffDAO  extends JpaRepository<Staff, Integer> {
    
    /**
     * Finds staff by username
     * @param username
     * @return staff
     */
    Staff findByUserUsername(String username);
}
