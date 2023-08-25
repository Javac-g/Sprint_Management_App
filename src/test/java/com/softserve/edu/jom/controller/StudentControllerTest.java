package com.softserve.edu.jom.controller;

import com.softserve.edu.jom.model.User;
import com.softserve.edu.jom.repository.RoleRepository;
import com.softserve.edu.jom.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional
@WithMockCustomUser
@AutoConfigureTestEntityManager
public class StudentControllerTest {
    private MockMvc mockMvc;
    private UserService userService;
    private RoleRepository roleRepository;
    private TestEntityManager entityManager;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setEntityManager(TestEntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    public void getAllStudentTest() throws Exception {
        List<User> expected = userService.getAllByRole("TRAINEE");
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("students"))
                .andExpect(MockMvcResultMatchers.model().attribute("students", expected));
    }

    @Test
    public void getStudentsByMarathonId() throws Exception {
        List<User> expected = userService.getAllByRoleAndMarathonId("TRAINEE", 1L);

        this.mockMvc
                .perform(get("/students/{marathonId}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("students"))
                .andExpect(MockMvcResultMatchers.model().attribute("students", expected));
    }

    @Test
    public void createNewStudentTest() throws Exception {
        User user = createNewStudent();
        mockMvc.perform(MockMvcRequestBuilders.post("/addStudent")
                .flashAttr("student", user))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));
    }

    @Test
    public void showAddStudentPageTest() throws Exception {
        User expectedUser = new User();
        expectedUser.setRole(roleRepository.getByRole(User.Role.TRAINEE));
        mockMvc.perform(get("/addStudent"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", expectedUser))
                .andExpect(MockMvcResultMatchers.model().attributeExists("mode"))
                .andExpect(MockMvcResultMatchers.model().attribute("mode", "Add"))
                .andExpect(view().name("changeStudent"));
    }

    @Test
    public void showEditStudentPageTest() throws Exception {
        Long userId = 1L;
        User user = userService.getUserById(userId);
        Long marathonId = 1L;
        mockMvc.perform(get("/students/{marathon_id}/edit/{student_id}", marathonId, userId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("mode"))
                .andExpect(MockMvcResultMatchers.model().attribute("mode", "Edit"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("marathonId"))
                .andExpect(MockMvcResultMatchers.model().attribute("marathonId", marathonId))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", user))
                .andExpect(view().name("changeStudent"));
    }

    @Test
    public void addNewStudentToMarathonTest() throws Exception {
        User user = createNewStudent();
        Long marathonId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.post("/students/{marathon_id}/add", marathonId)
                .flashAttr("student", user))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/students/" + marathonId));
    }

    @Test
    public void editStudentTest() throws Exception {
        Long userId = 1L;
        Long marathonId = 1L;
        User user = userService.getUserById(userId);
        String expectedEmail = "custom.user.email@gmail.com";
        user.setEmail(expectedEmail);
        mockMvc.perform(MockMvcRequestBuilders.post("/students/{marathon_id}/edit/{student_id}", marathonId, userId)
                .flashAttr("student", user))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/students/" + marathonId));
        User updatedUser = entityManager.find(User.class, userId);
        assertEquals(expectedEmail, updatedUser.getEmail());
    }

    @Test
    public void deleteStudentFromMarathonTest() throws Exception {
        Long userId = 1L;
        Long marathonId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/students/{marathon_id}/delete/{student_id}", marathonId, userId))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/students/" + marathonId));
    }

    @Test
    public void showAddStudentToMarathon() throws Exception {
        Long marathonId = 1L;
        User expectedUser = new User();
        expectedUser.setRole(roleRepository.getByRole(User.Role.TRAINEE));
        mockMvc.perform(MockMvcRequestBuilders.get("/students/{marathon_id}/add", marathonId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", expectedUser))
                .andExpect(MockMvcResultMatchers.model().attributeExists("mode"))
                .andExpect(MockMvcResultMatchers.model().attribute("mode", "Add"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("marathonId"))
                .andExpect(MockMvcResultMatchers.model().attribute("marathonId", marathonId))
                .andExpect(view().name("changeStudent"));
    }

    private User createNewStudent() {
        User user = new User();
        user.setFirstName("Alex");
        user.setLastName("Smith");
        user.setEmail("alex.smith@gmail.com");
        user.setPassword("asdasdsd");
        user.setRole(roleRepository.getByRole(User.Role.TRAINEE));
        return user;
    }
}