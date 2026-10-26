package com.smarttask.smartissuetaskapi.repository;

import com.smarttask.smartissuetaskapi.entity.ProjectTask;
import com.smarttask.smartissuetaskapi.entity.ProjectTaskId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectTaskRepository extends JpaRepository<ProjectTask, ProjectTaskId> {

    Optional<ProjectTask> findById_TaskId(Integer taskId);

    List<ProjectTask> findById_ProjectId(Integer projectId);

    boolean existsById_TaskId(Integer taskId);
}
