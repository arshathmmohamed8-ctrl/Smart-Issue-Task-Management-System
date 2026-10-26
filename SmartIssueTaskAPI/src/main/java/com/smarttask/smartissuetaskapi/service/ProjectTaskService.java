package com.smarttask.smartissuetaskapi.service;

import com.smarttask.smartissuetaskapi.entity.Project;
import com.smarttask.smartissuetaskapi.entity.ProjectTask;
import com.smarttask.smartissuetaskapi.entity.ProjectTaskId;
import com.smarttask.smartissuetaskapi.entity.Task;
import com.smarttask.smartissuetaskapi.repository.ProjectRepository;
import com.smarttask.smartissuetaskapi.repository.ProjectTaskRepository;
import com.smarttask.smartissuetaskapi.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectTaskService {

    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectTaskService(
            ProjectTaskRepository projectTaskRepository,
            ProjectRepository projectRepository,
            TaskRepository taskRepository
    ) {
        this.projectTaskRepository = projectTaskRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public ProjectTask assignTask(Integer projectId, Integer taskId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + taskId));

        ProjectTask existingAssignment = projectTaskRepository.findById_TaskId(taskId).orElse(null);
        if (existingAssignment != null) {
            if (existingAssignment.getId().getProjectId().equals(projectId)) {
                return existingAssignment;
            }
            throw new IllegalStateException("Task " + taskId + " is already assigned to another project.");
        }

        return projectTaskRepository.save(new ProjectTask(project, task));
    }

    @Transactional(readOnly = true)
    public List<ProjectTask> getAssignmentsForProject(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException("Project not found: " + projectId);
        }
        return projectTaskRepository.findById_ProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public ProjectTask getAssignmentForTask(Integer taskId) {
        return projectTaskRepository.findById_TaskId(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task assignment not found for task: " + taskId));
    }

    @Transactional
    public void unassignTask(Integer projectId, Integer taskId) {
        ProjectTaskId id = new ProjectTaskId(projectId, taskId);
        ProjectTask assignment = projectTaskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Assignment not found for project " + projectId + " and task " + taskId));
        projectTaskRepository.delete(assignment);
    }
}
