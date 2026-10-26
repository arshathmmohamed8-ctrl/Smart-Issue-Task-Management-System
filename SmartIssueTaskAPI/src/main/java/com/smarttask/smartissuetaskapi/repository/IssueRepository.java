package com.smarttask.smartissuetaskapi.repository;

import com.smarttask.smartissuetaskapi.entity.Issue;
import com.smarttask.smartissuetaskapi.enums.IssueStatus;
import com.smarttask.smartissuetaskapi.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Integer> {

    long countByStatus(IssueStatus status);

    List<Issue> findByStatus(IssueStatus status);

    List<Issue> findByPriority(Priority priority);

    List<Issue> findByTitleContainingIgnoreCase(String title);

    List<Issue> findTop10ByOrderByUpdatedAtDesc();
}
