package ua.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import ua.foxminded.university.service.pojo.Room;

public interface RoomDAO {
    
    /**
     * Shows all rooms
     * @return optional list of rooms
     */
    Optional<List<Room>> findAll();

}
