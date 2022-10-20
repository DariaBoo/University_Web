package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.service.entities.Group;

@ExtendWith(SpringExtension.class)
class GroupServiceImplUnitTest {
    
    @Mock
    private GroupDAO groupDao;
    @Mock
    private LessonDAO lessonDao;
    
    @InjectMocks
    private GroupServiceImpl groupService;
    
    private Group group;
    private Group group2;

    
    @BeforeEach
    void setup() {
        group = Group.builder().id(1).name("name").departmentId(1).build();
        group2 = Group.builder().id(2).name("name2").departmentId(1).build();
    }
    
    @Test
    void addGroup_shouldReturnTrue_whenAddNewGroup() {
        given(groupDao.save(group)).willReturn(group);
        given(groupDao.existsById(group.getId())).willReturn(true);
        assertEquals(group, groupService.addGroup(group));
        verify(groupDao, times(1)).save(any(Group.class));
    }
    
    @Test
    void findAllGroups_shouldReturnCountOfGroups_whenCallTheMethod() {
        List<Group> groups = new ArrayList<Group>();
        groups.add(group);
        groups.add(group2);
        given(groupDao.findAll()).willReturn(groups);
        
        List<Group> groupsService = groupService.findAllGroups();
        assertNotNull(groupsService);
        assertEquals(2, groupsService.size());
    }

    @Test
    void findAllGroups_shouldReturnEmptyList_whenCallTheMethod() {
        given(groupDao.findAll()).willReturn(new ArrayList<Group>());      
        List<Group> groupsService = groupService.findAllGroups();
        assertEquals(0, groupsService.size());
    }
    
    @Test
    void deleteGroup_shouldReturnTrue_whenInputGroupId() {
        int groupId = 1;
        willDoNothing().given(groupDao).deleteById(groupId);
        groupService.deleteGroup(groupId);
        verify(groupDao, times(1)).deleteById(groupId);
    }
    
    @Test
    void findById_shouldReturnGroup_whenInputExistedGroupId() {
        int groupId = 1;
        given(groupDao.findById(groupId)).willReturn(Optional.of(group));
        Group savedGroup = groupService.findById(group.getId());
        assertNotNull(savedGroup);
        assertEquals(group, savedGroup);
    }
    
    @Test
    void findById_shouldReturnIllegalArgumentException_whenInputNotExistedGroupId() {
        int groupId = 100;
        given(groupDao.findById(groupId)).willReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> groupService.findById(groupId));
    }
}
