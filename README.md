# Task Management System — Java 21 Console Application

A fully object-oriented, console-based **Project and Task Management System** built in Java 21.  
Demonstrates OOP principles, custom exceptions, JUnit 5 testing, and strict SOLID adherence.

---

## Prerequisites

| Requirement | Version |
|-------------|---------|
| Java JDK    | 21 (LTS) |
| JUnit 5     | 5.10+ |
| IDE         | IntelliJ IDEA Community Edition |

---

## Project Structure

```
project-management-system/
├── src/
│   ├── Main.java                  ← Entry point
│   ├── interfaces/
│   │   └── Completable.java       ← Interface: isCompleted()
│   ├── models/
│   │   ├── Project.java           ← Abstract base class
│   │   ├── software/Hardware...   ← Subclasses
│   │   └── User/Regular/Admin...  ← User hierarchy
│   ├── services/                  ← Core business logic
│   ├── utils/
│   │   ├── exceptions/            ← Custom exceptions [NEW]
│   │   └── ConsoleMenu/Valid...   ← Helpers
├── test/                          ← JUnit 5 unit tests [NEW]
├── docs/                          ← Documentation
└── README.md
```

---

## How to Run

### Main Application
```powershell
# Compile
javac -d bin src/*.java src/models/*.java src/services/*.java src/utils/*.java src/utils/exceptions/*.java src/interfaces/*.java

# Run
java -cp bin Main
```

### Unit Tests (JUnit 5)
```powershell
# Compile tests
javac -cp "lib/*;bin" -d bin/test test/*.java

# Run tests
java -jar lib/junit-platform-console-standalone.jar -cp bin/test --scan-classpath
```
*(Note: Requires JUnit JARs in `lib/` or IDE configuration)*

---

## Custom Exception System
The system implements a robust error handling framework:
- **`InvalidProjectDataException`**: Unique IDs, positive budgets.
- **`InvalidInputException`**: Input parsing and logical errors.
- **`TaskNotFoundException`**: Missing task IDs.
- **`EmptyProjectException`**: Reporting on empty projects.

---

## SOLID Principles Adherence
- **Single Responsibility**: Services handle specific domains (Tasks, Projects, Reports).
- **Open/Closed**: New project types (Software/Hardware) extend `Project` without modification.
- **Liskov Substitution**: All `User` subtypes are interchangeable in the `User[]` array.
- **Interface Segregation**: `Completable` interface is focused only on completion status.
- **Dependency Inversion**: `Main` depends on abstractions where possible.

---

## Features
- **Project/Task Management**: Full CRUD with limit enforcement.
- **User Role Check**: Admin-only project creation.
- **Dynamic Reports**: Real-time completion calculation.
- **Input Validation**: Strict regex and logical checks.

---

## Class Diagram
See [`docs/class-diagram.md`](docs/class-diagram.md)

## Design Decisions

See [`docs/design-decisions.md`](docs/design-decisions.md)
