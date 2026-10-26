package com.smarttask.smartissuetaskapi.repository;

import com.smarttask.smartissuetaskapi.entity.Task;
import com.smarttask.smartissuetaskapi.enums.Priority;
import com.smarttask.smartissuetaskapi.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    long countByStatus(TaskStatus status);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(Priority priority);

    List<Task> findByTitleContainingIgnoreCase(String title);

    List<Task> findByDeadlineGreaterThanEqualOrderByDeadlineAsc(LocalDate fromDate);

    List<Task> findTop10ByOrderByUpdatedAtDesc();
}
