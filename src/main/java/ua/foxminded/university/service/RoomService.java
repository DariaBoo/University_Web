package ua.foxminded.university.service;

import java.util.List;

import ua.foxminded.university.service.entities.Room;

public interface RoomService {
    /**
     * Shows all rooms
     * @return list of rooms
     */
    List<Room> findAll();

    /**
     * Shows all rooms
     * @return list of suitable rooms
     */
    List<Room> findSuitableRooms(int groupID);
}
