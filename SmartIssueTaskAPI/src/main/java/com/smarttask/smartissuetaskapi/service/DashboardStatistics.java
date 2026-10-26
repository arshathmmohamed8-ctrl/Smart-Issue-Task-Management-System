package com.smarttask.smartissuetaskapi.service;

public record DashboardStatistics(
        long projectCount,
        long taskCount,
        long pendingTaskCount,
        long inProgressTaskCount,
        long completedTaskCount,
        long issueCount,
        long openIssueCount,
        long inProgressIssueCount,
        long resolvedIssueCount
) {
}
