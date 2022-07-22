package ua.foxminded.university.dao.implementation;

import java.util.List;
import java.util.Optional;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Student;


/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Slf4j
@Repository
public class StudentDAOImpl implements StudentDAO {

    private static final String debugMessage = "Get current session - {}";
    private int result;

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * {@inheritDoc}
     * @throws DAOException 
     */
    @Override
    public int addStudent(Student student) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        try {
        result = (int) currentSession.save(student);
        log.debug("Add a Student - {} with id - {}", student, result);   
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            log.error(
                    "ConstraintViolationException while adding student - {} (name - {}, surname - {} and id card - {} violate the unique primary keys condition  or student id card is not unique",
                    student, student.getFirstName(), student.getLastName(), student.getIdCard());
            throw new DAOException("Student with name " + student.getFirstName() + ", surname "
                    + student.getLastName() + " and id card " + student.getIdCard() + " already exists or student id card is not unique!");
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * @throws DAOException 
     */
    @Override
    public void updateStudent(Student student) {
        Session currentSession = sessionFactory.getCurrentSession();  
        log.info(debugMessage, currentSession);
        try {
        currentSession.update(student);
        log.debug("Update the student - {}", student);
        } catch (org.hibernate.exception.ConstraintViolationException e) {
        log.error(
                "ConstraintViolationException while updating student - {} (name - {}, surname - {} and id card - {} violate the unique primary keys condition  or student id card is not unique",
                student, student.getFirstName(), student.getLastName(), student.getIdCard());
        throw new DAOException("Student with name " + student.getFirstName() + ", surname "
                    + student.getLastName() + " and id card " + student.getIdCard() + " already exists or student id card is not unique!");
        }       
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteStudent(int studentID) {
        boolean isDeleted = false;
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Student> studentToDelete = Optional.ofNullable(currentSession.byId(Student.class).load(studentID));
        if(studentToDelete.isPresent()) {
        currentSession.delete(studentToDelete.get());
        isDeleted = true;
        log.debug("Delete student with id - {} from the database", studentID);
        }
        return isDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean changePassword(int studentID, String newPassword) {
        boolean isChanged = false;
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession); 
        if(findByID(studentID).isPresent()) {
        currentSession.createNamedQuery("Student_changePassword")
                .setParameter("newPassword", newPassword)
                .setParameter("id", studentID)
                .executeUpdate();
        isChanged = true;
        log.debug("Change password for student with id - {}", studentID);
        }
        return isChanged;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Student> findByID(int studentID) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<Student> resultStudent = Optional.ofNullable(currentSession.get(Student.class, studentID));
        log.debug("Find student by id {} and return optional student - {}", studentID, resultStudent);
        return resultStudent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Student>> findAllStudents() {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<List<Student>> resultList = Optional
                .of(currentSession.createQuery("from Student", Student.class).getResultList());
        log.debug("Find all students and return optional list of students {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Student>> findStudentsByGroup(int groupID) {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
        CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
        log.info("Create criteriaQuery - {} for student class", criteriaQuery);
        Root<Student> root = criteriaQuery.from(Student.class);
        log.info("Create root -{} for student class", root);
        Group group = new Group();
        group.setId(groupID);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("group"), group));
        Query query = currentSession.createQuery(criteriaQuery);
        log.info("Create query by criteriaQuery - {}", criteriaQuery);
        Optional<List<Student>> resultList = Optional.ofNullable(query.getResultList());
        log.debug("Find all students by group id and return optional list of students {}", resultList);
        return resultList;
    }
}
