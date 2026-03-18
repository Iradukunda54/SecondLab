# UML Class Diagram — Task Management System

## Mermaid Diagram

```mermaid
classDiagram
    %% ── INTERFACE ──────────────────────────────────────────────────
    class Completable {
        <<interface>>
        +isCompleted() boolean
    }

    %% ── EXCEPTIONS ─────────────────────────────────────────────────
    class Exception { <<java>> }
    class RuntimeException { <<java>> }
    
    class InvalidInputException { +InvalidInputException(msg) }
    class TaskNotFoundException { +TaskNotFoundException(msg) }
    class EmptyProjectException { +EmptyProjectException(msg) }
    class InvalidProjectDataException { +InvalidProjectDataException(msg) }

    Exception <|-- InvalidInputException
    Exception <|-- TaskNotFoundException
    Exception <|-- EmptyProjectException
    RuntimeException <|-- InvalidProjectDataException

    %% ── TASK ────────────────────────────────────────────────────────
    class Task {
        -String id
        -String name
        -String status
        +Task(id, name, status)
        +isCompleted() boolean
        +displayTask()
    }
    Task ..|> Completable

    %% ── PROJECT HIERARCHY ────────────────────────────────────────────
    class Project {
        <<abstract>>
        -String id
        -String name
        -double budget
        -Task[] tasks
        -int maxTasks
        -int taskCount
        +calculateCompletionPercentage() double
        +addTask(task) boolean
    }

    class SoftwareProject {
        -String programmingLanguage
        +getProjectDetails() String
    }

    class HardwareProject {
        -String hardwareType
        +getProjectDetails() String
    }

    Project <|-- SoftwareProject
    Project <|-- HardwareProject
    Project "1" o-- "0..*" Task : contains

    %% ── USER HIERARCHY ────────────────────────────────────────────────
    class User {
        <<abstract>>
        -String name
        -String role
        +isAdmin() boolean
        +canAddProject() boolean
        +getRoleDescription()* String
    }

    class AdminUser {
        +getRoleDescription() String
    }

    class RegularUser {
        +getRoleDescription() String
    }

    User <|-- RegularUser
    User <|-- AdminUser

    %% ── SERVICES ──────────────────────────────────────────────────────
    class ProjectService {
        +createProject(project)
        +findById(id) Project
    }

    class TaskService {
        +createTask(projectId, task)
        +updateTaskStatus(projectId, taskId, status)
    }

    class ReportService {
        +generateProjectReport(project)
        +displayReport(reports)
    }

    TaskService --> ProjectService : uses
    ReportService --> Project : reads
```

---

## Inheritance Relationships

```
Object
├── Project (abstract)
│   ├── SoftwareProject
│   └── HardwareProject
└── User (abstract)
    ├── RegularUser
    └── AdminUser

Task implements Completable
```

## Association Relationships

| From | To | Relationship |
|------|----|--------------|
| `Project` | `Task` | Composition — project owns its Task[] array |
| `TaskService` | `ProjectService` | Dependency — injected via constructor |
| `ReportService` | `Project` | Association — reads project task data |
| `Main` | All Services | Uses all three services |
