# Design Decisions — Task Management System

## 1. Why an Abstract Class for `Project` Instead of an Interface?

`Project` holds shared **state** (id, name, budget, teamSize, task array) and a shared concrete method `displayProject()`. In Java, interfaces cannot hold mutable instance state, so an abstract class is the correct choice. The abstract method `getProjectDetails()` enforces that each subclass provides its own type-specific description without duplicating the common display logic.

---

## 2. Why `Completable` as an Interface?

`Completable` is a **capability contract**: "this object can report whether it is complete."  
It does not need state; it only defines behaviour. Using an interface allows any future class (e.g. `Milestone`, `Sprint`) to also implement `Completable` without inheriting from `Task`. This follows the **Interface Segregation Principle**.

---

## 3. Why Use Plain Arrays Instead of `ArrayList`?

The requirement explicitly mandates arrays for data storage to demonstrate:
- Manual index tracking (`projectCount`, `taskCount`)
- Bounds checking
- `System.arraycopy` for array slicing
- The discipline of fixed-capacity collections

---

## 4. Auto-generated User IDs via Static Counter

`User.idCounter` is a `static int` field that increments in the constructor.  
This guarantees **globally unique** IDs across all `User` instances (both `RegularUser` and `AdminUser`) without requiring an external ID source or database sequence.

---

## 5. Dependency Injection for `TaskService`

`TaskService` receives a `ProjectService` reference in its constructor rather than creating one internally.  
This is a lightweight form of **Dependency Injection**, making the service testable and decoupled: you can swap or mock `ProjectService` without changing `TaskService`.

---

## 6. Pre-loaded Sample Data in `ProjectService` Constructor

Five sample projects (3 Software + 2 Hardware) with tasks in mixed statuses are loaded at startup.  
This gives reviewers an immediate demonstration of **reporting**, **filtering**, and **completion calculation** without needing to manually enter data first.

---

## 7. Polymorphism Strategy

Both `Project` and `User` hierarchies use polymorphic arrays:

```java
Project[] projects = new Project[100]; // holds SoftwareProject and HardwareProject
User[]    users    = new User[10];     // holds RegularUser and AdminUser
```

Method calls like `project.getProjectDetails()` and `user.getRoleDescription()` dispatch to the correct subclass implementation at runtime — a clean demonstration of **dynamic polymorphism**.

---

## 8. Rounding Completion Percentage

```java
Math.round(((double) completed / total) * 100 * 100.0) / 100.0
```

Multiplying by 100 before rounding and dividing back preserves **2 decimal places** without importing `BigDecimal`, keeping the code dependency-free.

---

---

## 10. Custom Exception Hierarchy

The system uses a mix of checked and unchecked exceptions:
- **`InvalidProjectDataException`** (Unchecked): For programmer errors or validation failures that should be caught at the service layer.
- **`InvalidInputException`**, **`TaskNotFoundException`**, **`EmptyProjectException`** (Checked): These enforce that the calling code (Main) MUST handle potential runtime errors, ensuring a robust user experience and avoiding system crashes.

---

## 11. Separation of Concerns (SOLID)

The refactored system strictly follows SOLID:
- **`ProjectService`**: Manages the project catalog.
- **`TaskService`**: Manages task lifecycles, depending on `ProjectService` for lookups.
- **`ReportService`**: Focuses purely on data aggregation and display.
This decoupling makes the codebase significantly easier to maintain and extend.
