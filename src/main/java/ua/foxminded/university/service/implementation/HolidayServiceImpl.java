package ua.foxminded.university.service.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.dao.HolidayDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.HolidayService;
import ua.foxminded.university.service.entities.Holiday;
import ua.foxminded.university.service.exception.ServiceException;

@Service
public class HolidayServiceImpl implements HolidayService {    
    
    private static final Logger log = LoggerFactory.getLogger(HolidayServiceImpl.class);

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
