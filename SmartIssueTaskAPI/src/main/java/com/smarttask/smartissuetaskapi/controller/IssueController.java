package com.smarttask.smartissuetaskapi.controller;

import com.smarttask.smartissuetaskapi.entity.Issue;
import com.smarttask.smartissuetaskapi.service.IssueService;
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
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping
    public ResponseEntity<Issue> create(@Valid @RequestBody Issue issue) {
        validateIssue(issue, true);
        Issue created = issueService.create(issue);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public List<Issue> getAll() {
        return issueService.getAll();
    }

    @GetMapping("/{id}")
    public Issue getById(@PathVariable Integer id) {
        return issueService.getById(id);
    }

    @PutMapping("/{id}")
    public Issue update(@PathVariable Integer id, @Valid @RequestBody Issue issue) {
        validateIssue(issue, false);
        return issueService.update(id, issue);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        issueService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void validateIssue(Issue issue, boolean requireId) {
        if (requireId && issue.getId() == null) {
            throw new IllegalArgumentException("Issue ID is required.");
        }
        if (issue.getTitle() == null || issue.getTitle().isBlank() || issue.getTitle().length() > 200) {
            throw new IllegalArgumentException("Issue title is required and must be at most 200 characters.");
        }
        if (issue.getPriority() == null || issue.getStatus() == null) {
            throw new IllegalArgumentException("Issue priority and status are required.");
        }
    }
}
