package ua.foxminded.university.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.HolidayDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.HolidayService;
import ua.foxminded.university.service.entities.Holiday;
import ua.foxminded.university.service.exception.ServiceException;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Slf4j
@Service
public class HolidayServiceImpl implements HolidayService {    

    @Autowired
    private HolidayDAO holidayDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Holiday> findAllHolidays() {
        List<Holiday> resultList = holidayDAO.findAllHolidays().orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Take list of holidays - {}, otherwise return IllegalArgumentException", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     * @return 
     */
    @Override
    @Transactional
    public int addHoliday(Holiday holiday) {
        int result = 0;
        try {
            result = holidayDAO.addHoliday(holiday);
            log.debug("Add a new holiday and return an id - {}", result);
        } catch(DAOException e) {
            log.error(e.getMessage(), e.getCause());
            throw new ServiceException(e.getMessage());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteHoliday(int holidayID) {
        return holidayDAO.deleteHoliday(holidayID);
    }
}
