package com.smarttask.smartissuetaskapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_tasks")
public class ProjectTask {

    @EmbeddedId
    private ProjectTaskId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("projectId")
    @JoinColumn(
            name = "project_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_project_tasks_project")
    )
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("taskId")
    @JoinColumn(
            name = "task_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_project_tasks_task")
    )
    private Task task;

    @Column(name = "assigned_at", insertable = false, updatable = false)
    private LocalDateTime assignedAt;

    protected ProjectTask() {
    }

    public ProjectTask(Project project, Task task) {
        this.project = project;
        this.task = task;
        this.id = new ProjectTaskId(project.getId(), task.getId());
    }

    public ProjectTaskId getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public Task getTask() {
        return task;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }
}
