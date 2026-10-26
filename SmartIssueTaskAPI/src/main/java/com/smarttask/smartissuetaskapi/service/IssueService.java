package com.smarttask.smartissuetaskapi.service;

import com.smarttask.smartissuetaskapi.entity.Issue;
import com.smarttask.smartissuetaskapi.repository.IssueRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Transactional
    public Issue create(Issue issue) {
        if (issue.getId() == null) {
            throw new IllegalArgumentException("Issue ID must be provided.");
        }
        if (issueRepository.existsById(issue.getId())) {
            throw new IllegalStateException("Issue already exists: " + issue.getId());
        }
        return issueRepository.save(issue);
    }

    @Transactional(readOnly = true)
    public Issue getById(Integer id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Issue> getAll() {
        return issueRepository.findAll();
    }

    @Transactional
    public Issue update(Integer id, Issue updatedIssue) {
        Issue issue = getById(id);
        issue.setTitle(updatedIssue.getTitle());
        issue.setDescription(updatedIssue.getDescription());
        issue.setPriority(updatedIssue.getPriority());
        issue.setStatus(updatedIssue.getStatus());
        return issueRepository.save(issue);
    }

    @Transactional
    public void delete(Integer id) {
        issueRepository.delete(getById(id));
    }
}
