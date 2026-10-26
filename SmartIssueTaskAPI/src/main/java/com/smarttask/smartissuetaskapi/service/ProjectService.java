package com.smarttask.smartissuetaskapi.service;

import com.smarttask.smartissuetaskapi.entity.Project;
import com.smarttask.smartissuetaskapi.repository.ProjectRepository;
import com.smarttask.smartissuetaskapi.repository.ProjectTaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository projectTaskRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectTaskRepository projectTaskRepository) {
        this.projectRepository = projectRepository;
        this.projectTaskRepository = projectTaskRepository;
    }

    @Transactional
    public Project create(Project project) {
        if (project.getId() == null) {
            throw new IllegalArgumentException("Project ID must be provided.");
        }
        if (projectRepository.existsById(project.getId())) {
            throw new IllegalStateException("Project already exists: " + project.getId());
        }
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public Project getById(Integer id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @Transactional
    public Project update(Integer id, Project updatedProject) {
        Project project = getById(id);
        project.setName(updatedProject.getName());
        project.setDescription(updatedProject.getDescription());
        return projectRepository.save(project);
    }

    @Transactional
    public void delete(Integer id) {
        Project project = getById(id);
        projectTaskRepository.deleteAll(projectTaskRepository.findById_ProjectId(id));
        projectRepository.delete(project);
    }
}
