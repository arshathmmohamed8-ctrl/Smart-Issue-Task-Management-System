# SmartIssueTaskAPI

Spring Boot REST API for the Smart Issue & Task Management System.

## Prerequisites

- JDK 17 or later
- MySQL 8 with the existing `smart_issue_task_db` schema expected by the application

## Database configuration

The production configuration in `src/main/resources/application.properties` connects to the local MySQL database and reads credentials from the `DB_USERNAME` and `DB_PASSWORD` environment variables. Set both variables in your shell or IDE run configuration before starting the application. Do not put credentials in source files or commit local `.env` files.

Production schema handling is set to `spring.jpa.hibernate.ddl-auto=validate`; the application validates the existing schema and does not create or update it.

The `test` profile uses an isolated in-memory H2 database. Its `create-drop` setting applies only to that ephemeral test database and does not affect production MySQL.

## Run and test

From this directory, start the API with:

```powershell
.\gradlew.bat bootRun
```

Compile and run the tests with:

```powershell
.\gradlew.bat compileJava compileTestJava
.\gradlew.bat test
```

## API routes

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

Project-task assignments:

- `GET /api/project-tasks/projects/{projectId}`
- `GET /api/project-tasks/tasks/{taskId}`
- `PUT /api/project-tasks/projects/{projectId}/tasks/{taskId}`
- `DELETE /api/project-tasks/projects/{projectId}/tasks/{taskId}`

Dashboard:

- `GET /api/dashboard/statistics`

See `api-tests.http` for example requests. Verify IDs before executing its write or delete examples.
