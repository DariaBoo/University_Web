package ua.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.university.service.entities.Holiday;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public interface HolidayDAO extends JpaRepository<Holiday, Integer> {

}
