package ua.foxminded.university.dao.implementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.HolidayDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.entities.Holiday;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Repository
public class HolidayDAOImpl implements HolidayDAO {
    
    private static final Logger log = LoggerFactory.getLogger(HolidayDAOImpl.class.getName());
    private static final String debugMessage = "Get current session - {}";
        
    @Autowired
    private SessionFactory sessionFactory;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Holiday>> findAllHolidays() {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<List<Holiday>> resultList = Optional.ofNullable(currentSession.createQuery("from Holiday", Holiday.class).getResultList());
        log.debug("Return optional list of holidays {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addHoliday(Holiday holiday) throws DAOException{
        int result = 0;
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        try {
        result = (int) currentSession.save(holiday);
        log.debug("Add new holiday to the database and return holiday id - {}", result);
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            log.error(
                    "ConstraintViolationException while adding holiday - {} (date - {} violates the unique primary keys condition",
                    holiday, holiday.getDate());
            throw new DAOException("Holiday with date " + holiday.getDate() + " already exists!");
        }
        return result;
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteHoliday(int holidayID) {
        boolean isDeleted = false;
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Holiday> holidayToDelete = Optional.ofNullable(currentSession.byId(Holiday.class).load(holidayID));
        if(holidayToDelete.isPresent() && holidayToDelete.get().getDate().isAfter(LocalDate.now())) {
        currentSession.delete(holidayToDelete.get());
        isDeleted = true;
        log.debug("Delete holiday by id - {} if date not before current date, is deleted : {}", holidayID, isDeleted);
        }
        return isDeleted;
    }
}
