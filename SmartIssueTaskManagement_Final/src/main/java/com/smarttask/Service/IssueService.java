package com.smarttask.Service;

import com.smarttask.enums.*;
import com.smarttask.exception.TaskValidationException;
import com.smarttask.model.Issue;

import java.util.*;

public class IssueService {
    private final List<Issue> issues = new ArrayList<>();

    public void addIssue(Issue i) {
        if (i == null) throw new TaskValidationException("Issue cannot be null.");
        if (i.getTitle() == null || i.getTitle().isBlank())
            throw new TaskValidationException("Issue title cannot be empty.");
        if (findIssueById(i.getId()) != null) throw new TaskValidationException("Issue ID already exists.");
        if (i.getPriority() == null) throw new TaskValidationException("Priority is required.");
        issues.add(i);
        System.out.println("Issue created successfully.");
    }

    public List<Issue> viewIssues() {
        return new ArrayList<>(issues);
    }

    public Issue findIssueById(int id) {
        for (Issue i : issues) if (i.getId() == id) return i;
        return null;
    }

    public void updateIssueStatus(int id, IssueStatus s) {
        Issue i = findIssueById(id);
        if (i == null) throw new TaskValidationException("Issue not found.");
        if (s == null) throw new TaskValidationException("Issue status is required.");
        i.setStatus(s);
        System.out.println("Issue status updated successfully.");
    }

    public List<Issue> filterIssuesByPriority(Priority p) {
        List<Issue> r = new ArrayList<>();
        for (Issue i : issues) if (i.getPriority() == p) r.add(i);
        return r;
    }
}