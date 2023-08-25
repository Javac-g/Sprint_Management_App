package com.softserve.edu.jom.service;

import com.softserve.edu.jom.controller.WithMockCustomUser;
import com.softserve.edu.jom.model.Marathon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase
@Transactional
public class MarathonServiceTest {
    private MarathonService marathonService;

    @Autowired
    public void setMarathonService(MarathonService marathonService) {
        this.marathonService = marathonService;
    }

    @Test
    @WithMockCustomUser
    public void updateMarathon() {
        Marathon actual = marathonService.getMarathonById(2L);
        actual.setTitle("JavaScript Online Marathon Updated");
        marathonService.createOrUpdate(actual);
        assertEquals(marathonService.getMarathonById(2L).getTitle(), actual.getTitle());
    }

    @Test
    @WithMockCustomUser
    public void deleteMarathonTest() {
        Marathon actual = marathonService.getMarathonById(3L);
        marathonService.deleteMarathonById(3L);
        List<Marathon> marathons = marathonService.getAll();
        assertFalse(marathons.contains(actual));
    }

}
