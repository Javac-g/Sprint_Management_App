package com.softserve.edu.jom.service;

import com.softserve.edu.jom.model.Sprint;
import com.softserve.edu.jom.model.Task;

public interface TaskService {
    Task addTaskToSprint(Task task, Sprint sprint);
    Task getTaskById(Long id);
}
