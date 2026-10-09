package com.smarttask.model;

import java.util.*;

public class Project {
    private final int id;
    private String name, description;
    private final List<Task> tasks = new ArrayList<>();

    public Project(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void setName(String v) {
        name = v;
    }

    public void setDescription(String v) {
        description = v;
    }

    public void addTask(Task t) {
        if (t != null && !tasks.contains(t)) tasks.add(t);
    }
}