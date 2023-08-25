package com.softserve.edu.jom;

import com.softserve.edu.jom.model.Marathon;
import com.softserve.edu.jom.repository.MarathonRepository;
import com.softserve.edu.jom.service.MarathonService;
import com.softserve.edu.jom.service.MarathonServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JomApplicationTests {
//    @TestConfiguration
//    static class MarathonServiceTestConfiguration {
//        @Bean
//        public MarathonServiceImpl marathonService() {
//            return new MarathonServiceImpl();
//        }
//    }
//
//    private MarathonService marathonService;

//    @Autowired
//    public void setMarathonService(MarathonService marathonService) {
//        this.marathonService = marathonService;
//    }
//
//    @MockBean
//    private MarathonRepository marathonRepository;
//
//    @BeforeEach
//    public void setUp() {
//        Marathon marathon = new Marathon();
//        marathon.setTitle("newMarathon");
//        Mockito.when(marathonRepository.findByTitle("newMarathon")).thenReturn(marathon);
//    }
//
//    @Test
//    public void MarathonTest() {
//        String searchTitle = "newMarathon";
//        String actual = marathonRepository.findByTitle(searchTitle).getTitle();
//        Assert.assertEquals(actual, searchTitle);
//    }
//
//    @Test
//    void contextLoads() {
//    }

}
