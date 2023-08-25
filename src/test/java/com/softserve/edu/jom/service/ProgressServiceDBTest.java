package com.softserve.edu.jom.service;

import com.softserve.edu.jom.model.Progress;
import com.softserve.edu.jom.model.Task;
import com.softserve.edu.jom.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase
@Transactional
@AutoConfigureTestEntityManager
public class ProgressServiceDBTest {
    private ProgressService progressService;
    private TestEntityManager entityManager;
    private static final String COUNT_QUERY_STRING = "select count(x) from %s x";

    @Autowired
    public void setProgressService(ProgressService progressService) {
        this.progressService = progressService;
    }

    @Autowired
    public void setEntityManager(TestEntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    public void testAddTaskToStudent() {
        Long userId = 2L;
        Long taskId = 3L;
        User user = entityManager.find(User.class, userId);
        Task task = entityManager.find(Task.class, taskId);
        long initialProgressCount = count("Progress");
        Progress progress = progressService.addTaskForStudent(task, user);
        long currentProgressCount = count("Progress");
        assertEquals(initialProgressCount + 1, currentProgressCount);
        assertNotNull(progress);
        assertNotNull(progress.getTask());
        assertNotNull(progress.getUser());
        assertEquals(userId, progress.getUser().getId());
        assertEquals(taskId, progress.getTask().getId());
    }

    @Test
    public void testAddTaskToStudentWhenTaskAlreadyAddedToGivenStudent() {
        User user = entityManager.find(User.class, 1L);
        Task task = entityManager.find(Task.class, 1L);
        assertThrows(DataIntegrityViolationException.class, () -> {
            progressService.addTaskForStudent(task, user);
        });
    }

    @Test
    public void testSetStatus() {
        Progress.TaskStatus newStatus = Progress.TaskStatus.PASS;
        Long progressId = 1L;
        Progress progress = entityManager.find(Progress.class, progressId);
        assertNotEquals(newStatus, progress.getStatus());
        progress.setStatus(newStatus);
        boolean statusUpdated = progressService.setStatus(newStatus, progress);
        assertTrue(statusUpdated);
        Progress savedProgress = entityManager.find(Progress.class, progressId);
        assertEquals(newStatus, savedProgress.getStatus());
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