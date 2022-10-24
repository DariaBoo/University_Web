package ua.foxminded.university.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.dao.exceptions.UniqueConstraintViolationException;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;

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
    public Group addGroup(Group group) {
        Group savedGroup = groupDAO.findByName(group.getName());
        if(savedGroup == null) {
            savedGroup = groupDAO.save(group);
            log.info("Saved a new group with id [{}]", savedGroup.getId());
        } else {
            log.warn("Group with name [{}] already exists with id ::{}", savedGroup.getName(), savedGroup.getId());
            throw new UniqueConstraintViolationException("Group with name - [" + group.getName() + "] already exists");
        }
        return savedGroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Group updateGroup(Group group) {
        Group updatedGroup = groupDAO.findByName(group.getName());
        if(updatedGroup == null) {
            updatedGroup = groupDAO.save(group);
            log.info("Update group with id :: {}", group.getId());
        } else {
            log.warn("Group with name [{}] already exists with id ::{}", updatedGroup.getName(), updatedGroup.getId());
            throw new UniqueConstraintViolationException("Group with name - [" + group.getName() + "] already exists");
        }
        return updatedGroup;
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
