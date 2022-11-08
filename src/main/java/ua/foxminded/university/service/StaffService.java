package ua.foxminded.university.service;

import ua.foxminded.university.service.entities.Staff;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
public interface StaffService {

    /**
     * The method add new staff
     * 
     * @param staff
     * @return added staff
     */
    Staff addStaff(Staff staff);

    /**
     * Deletes staff by id
     * 
     * @param staffId
     */
    void deleteStaff(int staffId);
}
