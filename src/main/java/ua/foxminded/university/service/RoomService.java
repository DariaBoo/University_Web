package ua.foxminded.university.service;

import java.util.List;

import ua.foxminded.university.service.pojo.Room;

public interface RoomService {
    /**
     * Shows all rooms
     * @return list of rooms
     */
    List<Room> findAll();
}
