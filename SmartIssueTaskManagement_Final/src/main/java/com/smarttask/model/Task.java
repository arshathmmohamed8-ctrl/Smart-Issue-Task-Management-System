package com.smarttask.model;

import com.smarttask.enums.*;

import java.time.LocalDate;

public class Task {
    private final int id;
    private String title, description;
    private Priority priority;
    private Status status;
    private LocalDate deadline;

    public Task(int id, String title, String description, Priority priority, Status status, LocalDate deadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.deadline = deadline;
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

    public Status getStatus() {
        return status;
    }

    public LocalDate getDeadline() {
        return deadline;
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

    public void setStatus(Status v) {
        status = v;
    }

    public void setDeadline(LocalDate v) {
        deadline = v;
    }

    public boolean isOverdue() {
        return deadline != null && deadline.isBefore(LocalDate.now()) && status != Status.COMPLETED;
    }
}