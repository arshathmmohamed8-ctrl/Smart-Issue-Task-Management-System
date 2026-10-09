package com.smarttask.Service;

import com.smarttask.exception.TaskValidationException;
import com.smarttask.model.*;

import java.util.*;

public class ProjectService {
    private final List<Project> projects = new ArrayList<>();

    public void addProject(Project p) {
        if (p == null) throw new TaskValidationException("Project cannot be null.");
        valid(p.getName());
        if (findProjectById(p.getId()) != null) throw new TaskValidationException("Project ID already exists.");
        projects.add(p);
        System.out.println("Project added successfully.");
    }

    private void valid(String n) {
        if (n == null || n.isBlank()) throw new TaskValidationException("Project name cannot be empty.");
    }

    public List<Project> viewProjects() {
        return new ArrayList<>(projects);
    }

    public Project findProjectById(int id) {
        for (Project p : projects) if (p.getId() == id) return p;
        return null;
    }

    public void assignTaskToProject(int pid, Task t) {
        Project p = findProjectById(pid);
        if (p == null) throw new TaskValidationException("Project not found.");
        if (t == null) throw new TaskValidationException("Task not found.");
        for (Project old : projects)
            if (old.getTasks().contains(t))
                throw new TaskValidationException(old.getId() == pid ? "Task is already assigned to this project." : "Task is already assigned to another project.");
        p.addTask(t);
        System.out.println("Task assigned to project successfully.");
    }

    public void updateProject(int id, String n, String d) {
        Project p = findProjectById(id);
        if (p == null) throw new TaskValidationException("Project not found.");
        valid(n);
        p.setName(n.trim());
        p.setDescription(d == null ? "" : d.trim());
        System.out.println("Project updated successfully.");
    }

    public void deleteProject(int id) {
        Project p = findProjectById(id);
        if (p == null) throw new TaskValidationException("Project not found.");
        projects.remove(p);
        System.out.println("Project deleted successfully.");
    }
}