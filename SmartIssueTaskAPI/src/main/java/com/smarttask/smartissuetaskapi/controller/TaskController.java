package com.smarttask.smartissuetaskapi.controller;

import com.smarttask.smartissuetaskapi.entity.Task;
import com.smarttask.smartissuetaskapi.enums.Priority;
import com.smarttask.smartissuetaskapi.enums.TaskStatus;
import com.smarttask.smartissuetaskapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody Task task) {
        validateTask(task, true);
        Task created = taskService.create(task);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public List<Task> getAll(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Priority priority
    ) {
        if (status != null && priority != null) {
            return taskService.getByStatus(status).stream()
                    .filter(task -> priority == task.getPriority())
                    .toList();
        }
        if (status != null) {
            return taskService.getByStatus(status);
        }
        if (priority != null) {
            return taskService.getByPriority(priority);
        }
        return taskService.getAll();
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Integer id) {
        return taskService.getById(id);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Integer id, @Valid @RequestBody Task task) {
        validateTask(task, false);
        return taskService.update(id, task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void validateTask(Task task, boolean requireId) {
        if (requireId && task.getId() == null) {
            throw new IllegalArgumentException("Task ID is required.");
        }
        if (task.getTitle() == null || task.getTitle().isBlank() || task.getTitle().length() > 200) {
            throw new IllegalArgumentException("Task title is required and must be at most 200 characters.");
        }
        if (task.getPriority() == null || task.getStatus() == null) {
            throw new IllegalArgumentException("Task priority and status are required.");
        }
    }
}
