package com.smarttask.smartissuetaskapi.service;

import com.smarttask.smartissuetaskapi.enums.IssueStatus;
import com.smarttask.smartissuetaskapi.enums.TaskStatus;
import com.smarttask.smartissuetaskapi.repository.IssueRepository;
import com.smarttask.smartissuetaskapi.repository.ProjectRepository;
import com.smarttask.smartissuetaskapi.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final IssueRepository issueRepository;

    public DashboardService(
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            IssueRepository issueRepository
    ) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.issueRepository = issueRepository;
    }

    @Transactional(readOnly = true)
    public DashboardStatistics getStatistics() {
        return new DashboardStatistics(
                projectRepository.count(),
                taskRepository.count(),
                taskRepository.countByStatus(TaskStatus.PENDING),
                taskRepository.countByStatus(TaskStatus.IN_PROGRESS),
                taskRepository.countByStatus(TaskStatus.COMPLETED),
                issueRepository.count(),
                issueRepository.countByStatus(IssueStatus.OPEN),
                issueRepository.countByStatus(IssueStatus.IN_PROGRESS),
                issueRepository.countByStatus(IssueStatus.RESOLVED)
        );
    }
}
