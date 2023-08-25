package com.softserve.edu.jom.service;

import com.softserve.edu.jom.model.Sprint;
import com.softserve.edu.jom.repository.MarathonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase
@Transactional
public class SprintServiceTest {
    private SprintService sprintService;
    private MarathonRepository marathonRepository;

    @Autowired
    public void setSprintService(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @Autowired
    public void setMarathonRepository(MarathonRepository marathonRepository) {
        this.marathonRepository = marathonRepository;
    }

    @Test
    public void addSprintToMarathonTest() {
        Sprint sprint = new Sprint();
        sprint.setTitle("newSprint");
        sprint.setStart(LocalDateTime.now());
        sprint.setFinish(LocalDateTime.now());
        sprintService.addSprintToMarathon(sprint, marathonRepository.getMarathonById(2L));
        assertEquals(sprintService.getSprintsByMarathonId(2L).get(0).getTitle(), sprint.getTitle());
    }

    @Test
    public void updateSprintTest() {
        Sprint actual = sprintService.getSprintById(1L);
        actual.setTitle("Updated");
        sprintService.updateSprint(actual);
        assertEquals(sprintService.getSprintById(1L).getTitle(), actual.getTitle());
    }
}
