package com.softserve.edu.jom.service;

import com.softserve.edu.jom.model.Progress;
import com.softserve.edu.jom.model.Task;
import com.softserve.edu.jom.model.User;
import com.softserve.edu.jom.repository.ProgressRepository;
import com.softserve.edu.jom.repository.TaskRepository;
import com.softserve.edu.jom.repository.UserRepository;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProgressServiceImpl implements ProgressService {
    private ProgressRepository progressRepository;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    @Override
    public Progress getProgressById(Long progressId) {
        Validate.notNull(progressId, "Progress id must not be null!");
        return progressRepository.getOne(progressId);
    }

    @Override
    public Progress addTaskForStudent(Task task, User user) {
            User existedUser = userRepository.getOne(user.getId());
            Validate.notNull(existedUser, "User with id = %s doesn't exist!", user.getId());
            Progress newProgress = new Progress();
            newProgress.setStatus(Progress.TaskStatus.PENDING);
            newProgress.setUser(user);
            Task existedTask = taskRepository.getOne(task.getId());
            Validate.notNull(existedTask, "Task with id = %s doesn't exist!", task.getId());
            newProgress.setTask(existedTask);
            return progressRepository.save(newProgress);

    }

    @Override
    public Progress addOrUpdateProgress(Progress progress) {
        if (progress.getId() != null) {
            Optional<Progress> existedProgress = progressRepository.findById(progress.getId());
            if (existedProgress.isPresent()) {
                Progress newProgress = existedProgress.get();
                newProgress.setStatus(progress.getStatus());
                newProgress.setUpdated(progress.getUpdated());
                newProgress.setStarted(progress.getStarted());
                newProgress.setUser(progress.getUser());
                newProgress.setTask(progress.getTask());
                progressRepository.save(progress);
            }
        }
        return progressRepository.save(progress);

    }

    @Override
    public boolean setStatus(Progress.TaskStatus taskStatus, Progress progress) {
        progress.setStatus(taskStatus);
        return progressRepository.save(progress) != null;
    }

    @Override
    public List<Progress> allProgressByUserIdAndMarathonId(Long userId, Long marathonId) {
        return progressRepository.findByUserIdAndMarathonId(userId, marathonId);
    }

    @Override
    public List<Progress> allProgressByUserIdAndSprintId(Long userId, Long sprintId) {
        return progressRepository.findByUserIdAndSprintId(userId, sprintId);
    }

    @Autowired
    public void setProgressRepository(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}
