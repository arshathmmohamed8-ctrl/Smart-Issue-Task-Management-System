# Smart Issue & Task Management System

A Java-based console application designed to manage tasks, projects, and issues efficiently. This project demonstrates object-oriented programming, service-layer logic, input validation, and basic project management workflows.

## 🚀 Features

### Task Management
- Create, view, search, update, and delete tasks
- Search tasks by title using keywords
- Filter tasks by status and priority
- Sort tasks by priority
- Track task deadlines and identify overdue tasks
- Validate task IDs and required fields

### Project Management
- Create, view, update, and delete projects
- Assign tasks to projects
- Prevent duplicate task assignments
- Restrict a task from being assigned to multiple projects

### Issue Management
- Create and view issues
- Update issue status
- Filter issues by priority
- Track issue creation dates
- Validate issue details and duplicate IDs

## 🛠️ Technologies Used

- **Language:** Java
- **Concepts:** Object-Oriented Programming (OOP), Collections, Enums, Exception Handling
- **Date Handling:** Java Time API (`LocalDate`)
- **Build Tool:** Maven
- **IDE:** IntelliJ IDEA
- **Version Control:** Git and GitHub

## 📂 Project Structure

```text
SmartIssueTaskManagement/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── com/
                └── smarttask/
                    ├── Main.java
                    ├── enums/
                    ├── exception/
                    ├── model/
                    ├── Service/
                    └── ui/
                        └── TaskMenu.java
```

## ⚙️ Getting Started

### Prerequisites
- JDK 17 or later
- IntelliJ IDEA or another Java IDE
- Git (optional, for cloning the repository)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/YOUR-USERNAME/Smart-Issue-Task-Management-System.git
   ```

2. Open the project in IntelliJ IDEA.
3. Allow Maven to load the project.
4. Ensure the project SDK is set to JDK 17 or later.
5. Run `Main.java`.
6. Use the console menu to manage tasks, projects, and issues.

## 🧪 Example Workflow

1. Create a task with a title, description, priority, status, and deadline.
2. View or search for the task.
3. Create a project and assign the task to it.
4. Create an issue and update its status.
5. Filter tasks or issues based on priority and status.

## ⚠️ Current Limitations

- Data is stored in memory and is lost when the application exits.
- The application currently uses a console-based interface.
- Database persistence, REST APIs, authentication, and a web frontend are not included in this version.

## 🔮 Future Enhancements

- Migrate the application to **Spring Boot**
- Build RESTful APIs for tasks, projects, and issues
- Integrate MySQL using Spring Data JPA and Hibernate
- Add user authentication and role-based authorization
- Implement automated tests using JUnit and Mockito
- Add API testing, documentation, and deployment
- Build a web frontend using React

## 🎯 Project Objective

The goal of this project is to build a foundation in Java application development and evolve it into a full-stack project management system with a professional backend architecture.

## 👨‍💻 Author

**Mohamed Arshath**

