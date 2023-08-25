package com.softserve.edu.jom.repository;

import com.softserve.edu.jom.JomApplication;
import com.softserve.edu.jom.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = JomApplication.class)
public class UserRepositoryTest {
    TestEntityManager entityManager;

    UserRepository userRepository;

    RoleRepository roleRepository;

    @Autowired
    public UserRepositoryTest(TestEntityManager entityManager, UserRepository userRepository, RoleRepository roleRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Test
    public void testCreateUser() {
        User user = createNewUser();
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.getRole(), savedUser.getRole());
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    public void testUpdateUser() {
        String newName = "Vladlen";
        Long userId = 1L;
        User user = userRepository.getOne(userId);
        assertNotEquals(newName, user.getFirstName());
        user.setFirstName(newName);
        userRepository.save(user);
        User savedUser = userRepository.getUserById(userId);
        assertEquals(newName, savedUser.getFirstName());
    }

    @Test
    public void testSaveUserWithDuplicateEmail() {
        User user = userRepository.getUserById(1L);
        entityManager.detach(user);
        user.setId(null);
        user.setEmail("alex@gmail.com");
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user);
        });
    }

    @Test
    public void testSaveUserWitInvalidEmail() {
        User user = userRepository.getUserById(1L);
        entityManager.detach(user);
        user.setId(null);
        user.setEmail("invalid_email");
        assertThrows(ConstraintViolationException.class, () -> {
            userRepository.saveAndFlush(user);
        });
    }

    @Test
    public void testSaveUserWitInvalidLastName() {
        User user = userRepository.getUserById(1L);
        entityManager.detach(user);
        user.setId(null);
        user.setEmail("new@gmail.com");
        user.setLastName("w");
        assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(user);
        });
    }

    @Test
    public void testFindByRoleAndMarathon() {
        List<User> users = userRepository.findByRoleAndMarathonId(User.Role.MENTOR, 1L);
        assertEquals(1, users.size());
        assertEquals(4, users.get(0).getId());
        assertEquals("Victor", users.get(0).getFirstName());

        users = userRepository.findByRoleAndMarathonId(User.Role.TRAINEE, 1L);
        assertEquals(2, users.size());
    }

    @Test
    public void testGetUserById() {
        User user = userRepository.getUserById(2L);
        assertEquals("Mariana", user.getFirstName());
        assertEquals("Kuzma", user.getLastName());
        assertEquals("qweee2", user.getPassword());
        assertEquals(User.Role.TRAINEE, user.getRole().getRole());
        assertEquals("mari@gmail.com", user.getEmail());
    }

    @Test
    public void testGetUserWhenNotExist() {
        User foundUser = userRepository.getUserById(10L);
        assertNull(foundUser);
    }

    @Test
    public void testGetAll() {
        List<User> userList = userRepository.findAll();
        assertEquals(5, userList.size());
    }

    @Test
    public void testGetAllByRole() {
        List<User> mentorList = userRepository.getAllByRole(User.Role.MENTOR);
        assertEquals(2, mentorList.size());
    }

    @Test
    public void testGetAllByRoleAndMarathonId() {
        List<User> mentorList = userRepository.findByRoleAndMarathonId(User.Role.MENTOR, 2L);
        assertEquals(1, mentorList.size());
        assertEquals("Gari", mentorList.get(0).getFirstName());
        assertEquals("Curl", mentorList.get(0).getLastName());
        assertEquals("qweee", mentorList.get(0).getPassword());
        assertEquals(User.Role.MENTOR, mentorList.get(0).getRole().getRole());
        assertEquals("gari@gmail.com", mentorList.get(0).getEmail());
    }

    private User createNewUser() {
        User user = new User();
        user.setFirstName("Alex");
        user.setLastName("Smith");
        user.setEmail("alex.smith@gmail.com");
        user.setPassword("asdasdsd");
        user.setRole(roleRepository.getByRole(User.Role.MENTOR));
        return user;
    }
}