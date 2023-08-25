package com.softserve.edu.jom.service;

import com.softserve.edu.jom.model.Progress;
import com.softserve.edu.jom.model.Task;
import com.softserve.edu.jom.model.User;
import com.softserve.edu.jom.repository.ProgressRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ProgressServiceMockTest {
    private ProgressService progressService;
    @MockBean
    private ProgressRepository progressRepository;

    @Autowired
    public void setProgressService(ProgressService progressService) {
        this.progressService = progressService;
    }

    @Test
    public void testProgressById() {
        Progress expected = createProgress();
        Mockito.when(progressRepository.getOne(expected.getId()))
                .thenReturn(expected);
        Progress progress = progressService.getProgressById(expected.getId());
        assertEquals(expected.getStarted(), progress.getStarted());
        assertEquals(expected.getUpdated(), progress.getUpdated());
        assertEquals(expected.getStatus(), progress.getStatus());
        assertNotNull(progress.getUser());
        assertEquals(expected.getUser().getId(), progress.getUser().getId());
        assertNotNull(progress.getTask());
        assertEquals(expected.getTask().getId(), progress.getTask().getId());
    }

    @Test
    public void testAddOrUpdateProgress() {
        Progress expected = createProgress();
        Mockito.when(progressRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        Mockito.when(progressRepository.save(expected)).thenReturn(expected);
        Progress savedProgress = progressService.addOrUpdateProgress(expected);
        assertNotNull(savedProgress);
        assertNotNull(savedProgress.getId());
        assertEquals(expected.getStarted(), savedProgress.getStarted());
        assertEquals(expected.getUpdated(), savedProgress.getUpdated());
        assertEquals(expected.getStatus(), savedProgress.getStatus());
        assertEquals(expected.getTask().getId(), savedProgress.getTask().getId());
        assertEquals(expected.getUser().getId(), savedProgress.getUser().getId());
    }


    private Progress createProgress() {
        Progress progress = new Progress();
        progress.setId(1L);
        progress.setStatus(Progress.TaskStatus.PENDING);
        Task task = new Task();
        task.setId(3L);
        progress.setTask(task);
        User user = new User();
        user.setId(2L);
        progress.setUser(user);
        progress.setStarted(LocalDateTime.of(2020, 7, 31, 17, 30));
        progress.setUpdated(LocalDateTime.of(2020, 8, 01, 9, 30));
        return progress;
    }
}