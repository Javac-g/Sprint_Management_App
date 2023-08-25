package com.softserve.edu.jom.service;

import com.softserve.edu.jom.controller.WithMockCustomUser;
import com.softserve.edu.jom.model.Marathon;
import com.softserve.edu.jom.model.Role;
import com.softserve.edu.jom.model.User;
import com.softserve.edu.jom.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@AutoConfigureTestEntityManager
public class UserServiceDBTest {
    private UserService userService;
    private TestEntityManager entityManager;
    private RoleRepository roleRepository;
    private static final String COUNT_QUERY_STRING = "select count(x) from %s x";

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setEntityManager(TestEntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Test
    @WithMockCustomUser
    public void testAddNewUserToMarathon() {
        User user = new User();
        String fisrtName = "Alex";
        user.setFirstName(fisrtName);
        String lastName = "Smith";
        user.setLastName(lastName);
        String emai = "alex.smith@gmail.com";
        user.setEmail(emai);
        String password = "asdasdsd";
        user.setPassword(password);
        Role role = roleRepository.getByRole(User.Role.MENTOR);
        user.setRole(role);
        Long marathonId = 1L;
        long initialUserCount = count("User");
        boolean isAdded = userService.addUserToMarathon(user, marathonId);
        entityManager.flush();
        assertTrue(isAdded);
        long currentUserCount = count("User");
        assertEquals(initialUserCount + 1, currentUserCount);
        Marathon marathon = entityManager.find(Marathon.class, marathonId);
        entityManager.refresh(marathon);
        Set<User> userList = marathon.getUsers();
        assertTrue(userList != null && userList.size() >= 1);
        User savedUser = userList.stream().filter(i -> emai.equals(i.getEmail())).collect(Collectors.toList()).get(0);
        assertNotNull(savedUser.getId());
        assertEquals(fisrtName, savedUser.getFirstName());
        assertEquals(lastName, savedUser.getLastName());
        assertEquals(password, savedUser.getPassword());
        assertEquals(role, savedUser.getRole());
        assertEquals(emai, savedUser.getEmail());
    }

    @Test
    @WithMockCustomUser
    public void testRemoveUserFromMarathon() {
        Long userId = 1L;
        Long marathonId = 1L;
        long initialUserCount = count("User");
        long initialMarathonCount = count("Marathon");
        int initialUserMarathonCount = entityManager.find(User.class, userId).getMarathons().size();
        userService.removeUserFromMarathon(userId, marathonId);
        entityManager.flush();
        long currentUserCount = count("User");
        long currentMarathonCount = count("Marathon");
        User currentUser = entityManager.find(User.class, userId);
        entityManager.refresh(currentUser);
        int currentUserMarathonCount = currentUser.getMarathons().size();
        assertEquals(initialUserCount, currentUserCount);
        assertEquals(initialMarathonCount, currentMarathonCount);
        assertEquals(initialUserMarathonCount - 1, currentUserMarathonCount);
    }

    /**
     * Returns total count of given entities
     *
     * @param entityName - entity name
     * @return total items saved in DB
     */
    protected long count(String entityName) {
        return entityManager.getEntityManager().createQuery(String.format(COUNT_QUERY_STRING, entityName), Long.class).getSingleResult();
    }
}