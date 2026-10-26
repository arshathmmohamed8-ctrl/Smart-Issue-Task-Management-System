package com.smarttask.smartissuetaskapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key for {@code project_tasks}: ({@code project_id}, {@code task_id}).
 */
@Embeddable
public class ProjectTaskId implements Serializable {

    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    @Column(name = "task_id", nullable = false)
    private Integer taskId;

    protected ProjectTaskId() {
    }

    public ProjectTaskId(Integer projectId, Integer taskId) {
        this.projectId = projectId;
        this.taskId = taskId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ProjectTaskId that)) {
            return false;
        }
        return Objects.equals(projectId, that.projectId)
                && Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, taskId);
    }
}
