package com.softserve.edu.jom.repository;

import com.softserve.edu.jom.model.Sprint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class SprintRepositoryTest {

    @Autowired
    SprintRepository sprintRepository;

    @Test
    public void getSprintByIdTest() {
        Sprint actual = sprintRepository.getSprintById(2L);
        LocalDateTime startDate = actual.getStart();
        LocalDateTime endDate = actual.getFinish();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        assertEquals("2020-07-09", startDate.format(formatter));
        assertEquals("2020-07-12", endDate.format(formatter));
        assertEquals("Unit testing. Logging", actual.getTitle());
    }

    @Test
    public void getSprintsByMarathonIdTest() {
        List<Sprint> sprintsList = sprintRepository.getSprintsByMarathonId(1L);
        List<Long> actual = sprintsList.stream()
                .map(Sprint::getId)
                .collect(Collectors.toList());
        List<Long> expectedSprintsId = Arrays.asList(1L, 2L, 3L);
        assertEquals(actual, expectedSprintsId);
    }
   }
