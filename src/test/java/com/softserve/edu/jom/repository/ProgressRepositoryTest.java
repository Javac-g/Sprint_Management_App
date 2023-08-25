package com.softserve.edu.jom.repository;

import com.softserve.edu.jom.JomApplication;
import com.softserve.edu.jom.model.Progress;
import com.softserve.edu.jom.model.Task;
import com.softserve.edu.jom.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = JomApplication.class)
class ProgressRepositoryTest {
    TestEntityManager entityManager;

    ProgressRepository progressRepository;

    @Autowired
    public void setEntityManager(TestEntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    public void setProgressRepository(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @Test
    public void testProgressById() {
        Progress progress = progressRepository.getOne(1L);
        assertEquals(LocalDateTime.of(2020, 07, 05, 0, 0), progress.getStarted());
        assertEquals(LocalDateTime.of(2020, 07, 05, 0, 0), progress.getUpdated());
        assertEquals(Progress.TaskStatus.PENDING, progress.getStatus());
        assertNotNull(progress.getUser());
        assertEquals(1, progress.getUser().getId());
        assertNotNull(progress.getTask());
        assertEquals(1, progress.getTask().getId());
    }

    @Test
    public void testGetProgressWhenNotExist() {
        Optional<Progress> foundProgress = progressRepository.findById(100000L);
        assertFalse(foundProgress.isPresent());
    }

    @Test
    public void testCreateProgress() {
        Progress progress = createNewProgress();
        Progress savedProgress = progressRepository.save(progress);
        assertNotNull(savedProgress);
        assertNotNull(savedProgress.getId());
        assertEquals(progress.getStarted(), savedProgress.getStarted());
        assertEquals(progress.getUpdated(), savedProgress.getUpdated());
        assertEquals(progress.getStatus(), savedProgress.getStatus());
        assertEquals(progress.getTask().getId(), savedProgress.getTask().getId());
        assertEquals(progress.getUser().getId(), savedProgress.getUser().getId());
    }

    @Test
    public void testUpdateProgress() {
        Progress.TaskStatus newStatus = Progress.TaskStatus.PASS;
        Long progressId = 1L;
        Progress progress = entityManager.find(Progress.class, progressId);
        assertNotEquals(newStatus, progress.getStatus());
        progress.setStatus(newStatus);
        progressRepository.saveAndFlush(progress);
        Progress savedProgress = entityManager.find(Progress.class, progressId);
        assertEquals(newStatus, savedProgress.getStatus());
    }

    @Test
    public void testFindByUserIdAndMarathonId() {
        List<Progress> progressList = progressRepository.findByUserIdAndMarathonId(1L, 1L);
        assertEquals(3, progressList.size());

        progressList = progressRepository.findByUserIdAndMarathonId(1L, 2L);
        assertEquals(0, progressList.size());
    }

    @Test
    public void testFindByUserIdAndSprintId() {
        List<Progress> progressList = progressRepository.findByUserIdAndSprintId(1L, 1L);
        assertEquals(2, progressList.size());

        progressList = progressRepository.findByUserIdAndSprintId(1L, 2L);
        assertEquals(1, progressList.size());
        assertEquals(5, progressList.get(0).getId());
    }


    private Progress createNewProgress() {
        Progress progress = new Progress();
        progress.setStatus(Progress.TaskStatus.PENDING);
        progress.setTask(entityManager.find(Task.class, 3L));
        progress.setUser(entityManager.find(User.class, 2L));
        progress.setStarted(LocalDateTime.of(2020, 7, 31, 17, 30));
        progress.setUpdated(LocalDateTime.of(2020, 8, 01, 9, 30));
        return progress;
    }
}