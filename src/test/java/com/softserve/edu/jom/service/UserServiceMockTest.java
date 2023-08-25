package com.softserve.edu.jom.service;

import com.softserve.edu.jom.model.Marathon;
import com.softserve.edu.jom.model.Role;
import com.softserve.edu.jom.model.User;
import com.softserve.edu.jom.repository.MarathonRepository;
import com.softserve.edu.jom.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WithMockUser(authorities = {"MENTOR"})
public class UserServiceMockTest {
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MarathonRepository marathonRepository;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void testGetUserById() {
        User expected = createUser();
        Mockito.when(userRepository.getUserById(expected.getId()))
                .thenReturn(expected);
        User foundUser = userService.getUserById(1L);
        assertEquals(expected.getId(), foundUser.getId());
        assertEquals(expected.getFirstName(), foundUser.getFirstName());
        assertEquals(expected.getLastName(), foundUser.getLastName());
        assertEquals(expected.getPassword(), foundUser.getPassword());
        assertEquals(expected.getRole(), foundUser.getRole());
        assertEquals(expected.getEmail(), foundUser.getEmail());
    }

    @Test
    public void testGetAll() {
        User expected = createUser();
        Mockito.when(userRepository.findAll())
                .thenReturn(Arrays.asList(expected));
        List<User> userList = userService.getAll();
        assertEquals(1, userList.size());
        assertEquals(expected.getId(), userList.get(0).getId());
        assertEquals(expected.getFirstName(), userList.get(0).getFirstName());
        assertEquals(expected.getLastName(), userList.get(0).getLastName());
        assertEquals(expected.getPassword(), userList.get(0).getPassword());
        assertEquals(expected.getRole(), userList.get(0).getRole());
        assertEquals(expected.getEmail(), userList.get(0).getEmail());
    }

    @Test
    public void testCreateOrUpdateUser() {
        User expected = createUser();
        Mockito.when(userRepository.getUserById(expected.getId()))
                .thenReturn(expected);
        Mockito.when(userRepository.save(expected)).thenReturn(expected);
        User savedUser = userService.createOrUpdateUser(expected);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(expected.getFirstName(), savedUser.getFirstName());
        assertEquals(expected.getLastName(), savedUser.getLastName());
        assertEquals(expected.getPassword(), savedUser.getPassword());
        assertEquals(expected.getRole(), savedUser.getRole());
        assertEquals(expected.getEmail(), savedUser.getEmail());
    }


    @Test
    public void testGetAllByRole() {
        User expected = createUser();
        Mockito.when(userRepository.getAllByRole(User.Role.MENTOR)).thenReturn(Arrays.asList(expected));
        List<User> mentorList = userService.getAllByRole("MENTOR");
        assertEquals(1, mentorList.size());
        assertEquals(1, mentorList.size());
        assertEquals(expected.getId(), mentorList.get(0).getId());
        assertEquals(expected.getFirstName(), mentorList.get(0).getFirstName());
        assertEquals(expected.getLastName(), mentorList.get(0).getLastName());
        assertEquals(expected.getPassword(), mentorList.get(0).getPassword());
        assertEquals(expected.getRole(), mentorList.get(0).getRole());
        assertEquals(expected.getEmail(), mentorList.get(0).getEmail());
    }

    @Test
    public void testGetAllByRoleWhenRoleWrong() {
        Mockito.when(userRepository.getAllByRole(User.Role.MENTOR)).thenReturn(Arrays.asList(createUser()));
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getAllByRole("wrong_role");
        });
    }

    @Test
    public void testAddUserToNonExistingMarathon() {
        long marathonId = 1000L;
        Mockito.when(marathonRepository.getOne(marathonId)).thenReturn(null);
        assertThrows(NullPointerException.class, () -> {
            userService.addUserToMarathon(createUser(), marathonId);
        });
    }

    @Test
    public void testRemoveUserFromNonExistingMarathon() {
        long userId = 1L;
        long marathonId = 1000L;
        Mockito.when(marathonRepository.getOne(marathonId)).thenReturn(null);
        Mockito.when(userRepository.getOne(userId)).thenReturn(createUser());
        assertThrows(NullPointerException.class, () -> {
            userService.removeUserFromMarathon(userId, marathonId);
        });
    }

    @Test
    public void testRemoveNonExistingUserFromMarathon() {
        long userId = 1L;
        long marathonId = 1000L;
        Mockito.when(marathonRepository.getOne(marathonId)).thenReturn(new Marathon());
        Mockito.when(userRepository.getOne(userId)).thenReturn(null);
        assertThrows(NullPointerException.class, () -> {
            userService.removeUserFromMarathon(userId, marathonId);
        });
    }

    @Test
    public void testGetAllByRoleAndMarathonId() {
        User expected = createUser();
        Mockito.when(userRepository.findByRoleAndMarathonId(User.Role.MENTOR, 2L)).thenReturn(Arrays.asList(expected));
        List<User> mentorList = userService.getAllByRoleAndMarathonId("MENTOR", 2L);
        assertEquals(1, mentorList.size());
        assertEquals(expected.getId(), mentorList.get(0).getId());
        assertEquals(expected.getFirstName(), mentorList.get(0).getFirstName());
        assertEquals(expected.getLastName(), mentorList.get(0).getLastName());
        assertEquals(expected.getPassword(), mentorList.get(0).getPassword());
        assertEquals(expected.getRole(), mentorList.get(0).getRole());
        assertEquals(expected.getEmail(), mentorList.get(0).getEmail());
    }

    @Test
    public void testGetAllByRoleAndMarathonIdWithNotExistingRole() {
        User expected = createUser();
        Mockito.when(userRepository.findByRoleAndMarathonId(User.Role.MENTOR, 2L)).thenReturn(Arrays.asList(expected));
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getAllByRoleAndMarathonId("NOT_EXISTING", 2L);
        });
    }


    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Alex");
        user.setLastName("Smith");
        user.setEmail("alex.smith@gmail.com");
        user.setPassword("asdasdsd");
        Role role = new Role();
        role.setId(1L);
        role.setRole(User.Role.MENTOR);
        user.setRole(role);
        return user;
    }
}