package ua.foxminded.university.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.dao.exception.UniqueConstraintViolationException;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.exception.ServiceException;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDAO groupDAO;
    @Autowired
    private LessonDAO lessonDAO;

    int result = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean addGroup(Group group) {
        try {
            log.info("Add group");
            groupDAO.save(group);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.error(e.getMessage(), e.getCause());
            throw new UniqueConstraintViolationException("Group with name - [" + group.getName() + "] already exists");
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
                if (violation != null) {
                    log.error(violation.getMessageTemplate());
                    throw new ServiceException(violation.getMessageTemplate());//rename to EntityConstraintViolationException
                }
            }
        }
        return groupDAO.existsById(group.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateGroup(Group group) {
        try {
            groupDAO.save(group);
            log.info("Update group with id :: {}", group.getId());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.error(e.getMessage(), e.getCause());
            throw new UniqueConstraintViolationException("Group with name - [" + group.getName() + "] already exists");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteGroup(int groupId) {
        groupDAO.deleteById(groupId);
        return !groupDAO.existsById(groupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean assignLessonToGroup(int groupId, int lessonId) {
        Optional<Group> group = groupDAO.findById(groupId);
        Optional<Lesson> lesson = lessonDAO.findById(lessonId);
        if (group.isPresent() && lesson.isPresent()) {
            group.get().getLessons().add(lesson.get());
            groupDAO.save(group.get());
            if (group.get().getLessons().contains(lesson.get())) {
                log.info("Lesson with id :: {} was added to group with id :: {}", lessonId, groupId);
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteLessonFromGroup(int groupId, int lessonId) {
        Optional<Group> group = groupDAO.findById(groupId);
        Optional<Lesson> lesson = lessonDAO.findById(lessonId);
        if (group.isPresent() && lesson.isPresent()) {
            group.get().getLessons().remove(lesson.get());
            groupDAO.save(group.get());
            if (!group.get().getLessons().contains(lesson.get())) {
                log.info("Lesson with id :: {} was removed from group with id :: {}", lessonId, groupId);
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable("groups")
    public List<Group> findAllGroups() {
        List<Group> resultList = groupDAO.findAll();
        log.debug("Found all groups, list size :: {}", resultList.size());
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Group findById(int groupId) {
        Group resultGroup = groupDAO.findById(groupId).orElseThrow(
                () -> new IllegalArgumentException("Error occured while searching group by id: " + groupId));
        log.debug("Found group with id :: {}", groupId);
        return resultGroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Group> findGroupsByTeacherId(int teacherId) {
        List<Group> resultList = groupDAO.findByLessons_Teachers_Id(teacherId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Error occured while searching group by teacher id - {} " + teacherId));
        log.debug("Found groups (count - {}) by teacher id - {}", resultList.size(), teacherId);
        return resultList;
    }
}
