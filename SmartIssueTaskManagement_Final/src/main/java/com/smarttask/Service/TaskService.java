package com.smarttask.Service;

import com.smarttask.enums.*;
import com.smarttask.exception.TaskValidationException;
import com.smarttask.model.Task;

import java.util.*;
import java.util.Locale;

public class TaskService {
    private final List<Task> tasks = new ArrayList<>();

    private void title(String s) {
        if (s == null || s.isBlank()) throw new TaskValidationException("Task title cannot be empty.");
    }

    public void addTask(Task t) {
        if (t == null) throw new TaskValidationException("Task cannot be null.");
        title(t.getTitle());
        if (findTaskById(t.getId()) != null) throw new TaskValidationException("Task ID already exists.");
        if (t.getPriority() == null || t.getStatus() == null)
            throw new TaskValidationException("Priority and status are required.");
        tasks.add(t);
        System.out.println("Task added successfully.");
    }

    public List<Task> viewTasks() {
        return new ArrayList<>(tasks);
    }

    public Task findTaskById(int id) {
        for (Task t : tasks) if (t.getId() == id) return t;
        return null;
    }

    public boolean updateTask(int id, String title, String description, Priority p, Status s) {
        Task t = findTaskById(id);
        if (t == null) throw new TaskValidationException("Task not found.");
        title(title);
        if (p == null || s == null) throw new TaskValidationException("Priority and status are required.");
        t.setTitle(title.trim());
        t.setDescription(description == null ? "" : description.trim());
        t.setPriority(p);
        t.setStatus(s);
        return true;
    }

    public boolean deleteTask(int id) {
        Task t = findTaskById(id);
        if (t == null) throw new TaskValidationException("Task not found.");
        tasks.remove(t);
        return true;
    }

    public List<Task> searchTaskByTitle(String k) {
        if (k == null || k.isBlank()) throw new TaskValidationException("Search keyword cannot be empty.");
        List<Task> r = new ArrayList<>();
        for (Task t : tasks)
            if (t.getTitle().toLowerCase(Locale.ROOT).contains(k.trim().toLowerCase(Locale.ROOT))) r.add(t);
        return r;
    }

    public List<Task> filterTaskByStatus(Status s) {
        List<Task> r = new ArrayList<>();
        for (Task t : tasks) if (t.getStatus() == s) r.add(t);
        return r;
    }

    public List<Task> filterTaskByPriority(Priority p) {
        List<Task> r = new ArrayList<>();
        for (Task t : tasks) if (t.getPriority() == p) r.add(t);
        return r;
    }

    public List<Task> sortTaskByPriority() {
        List<Task> r = new ArrayList<>(tasks);
        r.sort(Comparator.comparing(Task::getPriority).reversed());
        return r;
    }
}