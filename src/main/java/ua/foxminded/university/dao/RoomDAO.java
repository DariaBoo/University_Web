package ua.foxminded.university.dao;

import java.util.List;

import ua.foxminded.university.service.pojo.Room;

public interface RoomDAO {
    
    /**
     * Shows all rooms
     * @return list of rooms
     */
    List<Room> findAll();

}
