package ua.foxminded.university.service;

import ua.foxminded.university.service.entities.Staff;

public interface StaffService {

    Staff addStaff(Staff staff);
    void deleteStaff(int staffId);    
}
