package com.smarttask.model;

import com.smarttask.enums.*;

import java.time.LocalDate;

public class Issue {
    private final int id;
    private String title, description;
    private Priority priority;
    private IssueStatus status = IssueStatus.OPEN;
    private final LocalDate createdDate = LocalDate.now();

    public Issue(int id, String title, String description, Priority priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setTitle(String v) {
        title = v;
    }

    public void setDescription(String v) {
        description = v;
    }

    public void setPriority(Priority v) {
        priority = v;
    }

    public void setStatus(IssueStatus v) {
        status = v;
    }
}