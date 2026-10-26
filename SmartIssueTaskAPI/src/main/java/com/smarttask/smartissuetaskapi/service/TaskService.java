package com.smarttask.smartissuetaskapi.service;

import com.smarttask.smartissuetaskapi.entity.Task;
import com.smarttask.smartissuetaskapi.enums.Priority;
import com.smarttask.smartissuetaskapi.enums.TaskStatus;
import com.smarttask.smartissuetaskapi.repository.ProjectTaskRepository;
import com.smarttask.smartissuetaskapi.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectTaskRepository projectTaskRepository;

    public TaskService(TaskRepository taskRepository, ProjectTaskRepository projectTaskRepository) {
        this.taskRepository = taskRepository;
        this.projectTaskRepository = projectTaskRepository;
    }

    @Transactional
    public Task create(Task task) {
        if (task.getId() == null) {
            throw new IllegalArgumentException("Task ID must be provided.");
        }
        if (taskRepository.existsById(task.getId())) {
            throw new IllegalStateException("Task already exists: " + task.getId());
        }
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Task getById(Integer id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Task> getByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Task> getByPriority(Priority priority) {
        return taskRepository.findByPriority(priority);
    }

    @Transactional
    public Task update(Integer id, Task updatedTask) {
        Task task = getById(id);
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setPriority(updatedTask.getPriority());
        task.setStatus(updatedTask.getStatus());
        task.setDeadline(updatedTask.getDeadline());
        return taskRepository.save(task);
    }

    @Transactional
    public void delete(Integer id) {
        Task task = getById(id);
        projectTaskRepository.findById_TaskId(id)
                .ifPresent(projectTaskRepository::delete);
        taskRepository.delete(task);
    }
}
