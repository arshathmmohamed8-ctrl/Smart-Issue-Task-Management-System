# Smart Issue & Task Management System

Java 17 console application to manage tasks, projects, and issues.

## Features
- Task CRUD, title search, status/priority filtering, priority sorting, deadlines/overdue detection
- Project CRUD and task assignment (one task can belong to one project)
- Issue creation, listing, status updates, priority filtering
- Input validation for IDs, dates, priorities, statuses, and missing records

## Run
Open as a Maven project in IntelliJ, select JDK 17+, and run `com.smarttask.Main`.

## Limitation
Data is stored in memory and is cleared when the application exits. The next phase is Spring Boot REST APIs with MySQL/JPA.
