# Smart Issue & Task Management System

This repository contains two separate Java applications:

- [`SmartIssueTaskManagement_Final/`](./SmartIssueTaskManagement_Final/) — the existing console application.
- [`SmartIssueTaskAPI/`](./SmartIssueTaskAPI/) — the Spring Boot REST API.

## Spring Boot REST API

### Prerequisites

- JDK 17 or later
- MySQL 8
- The existing `smart_issue_task_db` database with the schema expected by the API

The API uses Hibernate schema validation (`spring.jpa.hibernate.ddl-auto=validate`). It does not create or update database tables. Configure the database and schema separately; do not use a reset or schema-generation mode on a database containing existing data.

### Database credentials

The API reads credentials from environment variables and does not include them in source files:

- `DB_USERNAME`
- `DB_PASSWORD`

Set both variables in your local shell or IDE run configuration before launching Gradle. Do not commit real values or local `.env` files.

### Run and test

From the `SmartIssueTaskAPI` directory:

```powershell
.\gradlew.bat bootRun
```

Compile and run tests:

```powershell
.\gradlew.bat compileJava compileTestJava
.\gradlew.bat test
```

### API routes

Projects:

- `GET /api/projects`
- `GET /api/projects/{id}`
- `POST /api/projects`
- `PUT /api/projects/{id}`
- `DELETE /api/projects/{id}`

Tasks:

- `GET /api/tasks` (optional `status` and `priority` filters)
- `GET /api/tasks/{id}`
- `POST /api/tasks`
- `PUT /api/tasks/{id}`
- `DELETE /api/tasks/{id}`

Issues:

- `GET /api/issues`
- `GET /api/issues/{id}`
- `POST /api/issues`
- `PUT /api/issues/{id}`
- `DELETE /api/issues/{id}`

Project–Task assignments:

- `GET /api/project-tasks/projects/{projectId}`
- `GET /api/project-tasks/tasks/{taskId}`
- `PUT /api/project-tasks/projects/{projectId}/tasks/{taskId}`
- `DELETE /api/project-tasks/projects/{projectId}/tasks/{taskId}`

Example requests are in [`SmartIssueTaskAPI/api-tests.http`](./SmartIssueTaskAPI/api-tests.http). Check the target IDs before running its write or delete examples.
