package ua.foxminded.university.dao.implementation.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import ua.foxminded.university.service.pojo.Room;

public class RoomMapper implements RowMapper<Room>{
    private static final Logger log = LoggerFactory.getLogger(RoomMapper.class);

    @Override
    public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.trace("Create rowMapper for Room");
        Room room = new Room();
        room.setNumber(rs.getInt("room_id"));
        room.setCapacity(rs.getInt("capacity"));
        return room;
    }

}
