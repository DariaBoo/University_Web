package ua.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.university.service.entities.Group;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface GroupDAO extends JpaRepository<Group, Integer>{   
    
    /**
     * The method finds all groups by teacher id and returns optional list of groups
     * @return optional list of groups
     */
    Optional<List<Group>> findByLessons_Teachers_Id(int teacherId);    
}
