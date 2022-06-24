package ua.foxminded.university.dao.implementation.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import ua.foxminded.university.service.pojo.Group;


/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public class GroupMapper implements RowMapper<Group> {
    private static final Logger log = LoggerFactory.getLogger(GroupMapper.class.getName());
    
    /**
     * Returns rowMapper for Group
     */
    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.trace("Create rowMapper for Group");
        Group group = new Group();
        group.setId(rs.getInt("group_id"));
        group.setName(rs.getString("group_name"));
        group.setDepartmentID(rs.getInt("department_id"));
        return group;
    }
}
