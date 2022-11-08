package ua.foxminded.university.service.implementation;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.HolidayDAO;
import ua.foxminded.university.service.HolidayService;
import ua.foxminded.university.service.entities.Holiday;
import ua.foxminded.university.service.exception.EntityConstraintViolationException;

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
    @Transactional(readOnly = true)
    @Cacheable("holidays")
    public List<Holiday> findAllHolidays() {
        List<Holiday> resultList = holidayDAO.findAll();
        log.debug("Found list of holidays, list size :: ", resultList.size());
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Holiday addHoliday(Holiday holiday) {
        Optional<Holiday> savedHoliday = holidayDAO.findByDate(holiday.getDate());
        if (savedHoliday.isPresent()) {
            log.warn("Holiday with date :: {} already exists!", holiday.getDate());
            throw new EntityConstraintViolationException(
                    "Holiday with date :: " + holiday.getDate() + " already exists!");
        }
        return holidayDAO.save(holiday);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteHoliday(int holidayId) {
        if (holidayDAO.existsById(holidayId)) {
            holidayDAO.deleteById(holidayId);
        } else {
            log.warn("Holiday with id {} doesn't exist. Nothing to delete.", holidayId);
            throw new EntityNotFoundException("Holiday with id " + holidayId + " doesn't exist. Nothing to delete");
        }
    }
}
