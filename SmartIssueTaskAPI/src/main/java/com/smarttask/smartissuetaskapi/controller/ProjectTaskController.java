package com.smarttask.smartissuetaskapi.controller;

import com.smarttask.smartissuetaskapi.entity.ProjectTask;
import com.smarttask.smartissuetaskapi.service.ProjectTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/project-tasks")
public class ProjectTaskController {

    private final ProjectTaskService projectTaskService;

    public ProjectTaskController(ProjectTaskService projectTaskService) {
        this.projectTaskService = projectTaskService;
    }

    @GetMapping("/projects/{projectId}")
    public List<ProjectTask> getAssignmentsForProject(@PathVariable Integer projectId) {
        validateId(projectId, "Project");
        return projectTaskService.getAssignmentsForProject(projectId);
    }

    @GetMapping("/tasks/{taskId}")
    public ProjectTask getAssignmentForTask(@PathVariable Integer taskId) {
        validateId(taskId, "Task");
        return projectTaskService.getAssignmentForTask(taskId);
    }

    @PutMapping("/projects/{projectId}/tasks/{taskId}")
    public ProjectTask assignTask(
            @PathVariable Integer projectId,
            @PathVariable Integer taskId
    ) {
        validateId(projectId, "Project");
        validateId(taskId, "Task");
        return projectTaskService.assignTask(projectId, taskId);
    }

    @DeleteMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<Void> unassignTask(
            @PathVariable Integer projectId,
            @PathVariable Integer taskId
    ) {
        validateId(projectId, "Project");
        validateId(taskId, "Task");
        projectTaskService.unassignTask(projectId, taskId);
        return ResponseEntity.noContent().build();
    }

    private void validateId(Integer id, String entityName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(entityName + " ID must be a positive integer.");
        }
    }
}
