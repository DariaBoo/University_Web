package ua.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.service.entities.Room;

@Repository
public interface RoomDAO extends JpaRepository<Room, Integer> {
    /**
     * Shows all rooms
     * @return optional list of suitable rooms
     */
    @Query("SELECT r FROM Room r WHERE r.capacity >= ?1")
    Optional<List<Room>> findSuitableRooms(int capacity);
}
