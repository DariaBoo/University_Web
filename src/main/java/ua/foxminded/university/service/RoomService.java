package ua.foxminded.university.service;

import java.util.List;

import ua.foxminded.university.service.entities.Room;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
public interface RoomService {
    /**
     * Shows all rooms
     * @return list of rooms
     */
    List<Room> findAll();

    /**
     * Shows all rooms by capacity
     * @return list of suitable rooms
     */
    List<Room> findSuitableRooms(int capacity);
}
