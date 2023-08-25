package com.softserve.edu.jom.repository;

import com.softserve.edu.jom.model.Marathon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class MarathonRepositoryTest {

    @Autowired
    private MarathonRepository marathonRepository;

    @Test
    public void getMarathonByIdTest() {
        Marathon actual = marathonRepository.getMarathonById(1L);
        assertEquals("Java Online Marathon", actual.getTitle());
    }
}
