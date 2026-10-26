package com.smarttask.smartissuetaskapi.controller;

import com.smarttask.smartissuetaskapi.entity.Project;
import com.smarttask.smartissuetaskapi.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Project> create(@Valid @RequestBody Project project) {
        validateProject(project, true);
        Project created = projectService.create(project);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public List<Project> getAll() {
        return projectService.getAll();
    }

    @GetMapping("/{id}")
    public Project getById(@PathVariable Integer id) {
        return projectService.getById(id);
    }

    @PutMapping("/{id}")
    public Project update(@PathVariable Integer id, @Valid @RequestBody Project project) {
        validateProject(project, false);
        return projectService.update(id, project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void validateProject(Project project, boolean requireId) {
        if (requireId && project.getId() == null) {
            throw new IllegalArgumentException("Project ID is required.");
        }
        if (project.getName() == null || project.getName().isBlank() || project.getName().length() > 150) {
            throw new IllegalArgumentException("Project name is required and must be at most 150 characters.");
        }
    }
}
