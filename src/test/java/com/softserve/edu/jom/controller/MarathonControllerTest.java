package com.softserve.edu.jom.controller;

import com.softserve.edu.jom.model.Marathon;
import com.softserve.edu.jom.service.MarathonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional
public class MarathonControllerTest {
    private MockMvc mockMvc;
    private MarathonService marathonService;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Autowired
    public void setMarathonService(MarathonService marathonService) {
        this.marathonService = marathonService;
    }

    @Test
    @WithMockCustomUser
    public void getAllMarathonsTest() throws Exception {
        List<Marathon> expected = marathonService.getAll();

        mockMvc.perform(MockMvcRequestBuilders.get("/marathons"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("marathons"))
                .andExpect(MockMvcResultMatchers.model().attribute("marathons", expected));
    }

    @Test
    public void createNewMarathonTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/addMarathon")
        .param("title", "newMarathon"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }
}
