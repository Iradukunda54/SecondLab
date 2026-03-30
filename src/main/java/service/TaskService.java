package service;

import model.Project;
import model.Task;
import exception.InvalidInputException;
import exception.TaskNotFoundException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * TaskService — Handles task-level operations within projects.
 * Delegates project lookup to ProjectService.
 */
public class TaskService {

    // ── Dependencies ──────────────────────────────────────────────────────────
    private ProjectService projectService;

    // ── Constructor ───────────────────────────────────────────────────────────
    public TaskService(ProjectService projectService) {
        this.projectService = projectService;
    }

    // ── CORE METHODS REQUIRED ─────────────────────────────────────────────────

    /**
     * createTask — Adds a task to the specified project.
     * @throws InvalidInputException if project not found or limit reached.
     */
    public void createTask(String projectId, Task task) throws InvalidInputException {
        Project project = projectService.findById(projectId);
        if (project == null) {
            throw new InvalidInputException("Project not found: " + projectId);
        }
        if (!project.addTask(task)) {
            throw new InvalidInputException("Could not add task. Check for duplicate ID or project task limit.");
        }
    }

    /**
     * updateTaskStatus — Updates status and throws exception if task missing.
     * @throws TaskNotFoundException if task not found.
     */
    public void updateTaskStatus(String projectId, String taskId, String newStatus) 
            throws TaskNotFoundException {
        Project project = projectService.findById(projectId);
        if (project == null) {
            throw new TaskNotFoundException("Project not found: " + projectId);
        }
        Task task = project.findTaskById(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task '" + taskId + "' not found in project '" + projectId + "'.");
        }
        task.setStatus(newStatus);
    }

    /**
     * getTaskById — Returns task or throws exception.
     * @throws TaskNotFoundException if task not found.
     */
    public Task getTaskById(String projectId, String taskId) throws TaskNotFoundException {
        Project project = projectService.findById(projectId);
        if (project == null) {
            throw new TaskNotFoundException("Project not found: " + projectId);
        }
        Task task = project.findTaskById(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task '" + taskId + "' not found.");
        }
        return task;
    }

    // ── Legacy / Helper methods ───────────────────────────────────────────────
    /** @deprecated Use createTask for exception-based flow. */
    @Deprecated
    public boolean addTask(String projectId, Task task) {
        try {
            createTask(projectId, task);
            return true;
        } catch (InvalidInputException e) {
            System.out.println("  [!] " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns the tasks for a given project, or null if project not found.
     * Uses Streams to return a list.
     */
    public java.util.List<Task> getTasksForProject(String projectId) {
        Project project = projectService.findById(projectId);
        if (project == null) return null;
        return project.getTaskList();
    }

    /**
     * Checks whether a task ID already exists within a project (duplicate guard).
     */
    public boolean taskIdExists(String projectId, String taskId) {
        Project project = projectService.findById(projectId);
        if (project == null) return false;
        return project.findTaskById(taskId) != null;
    }

    /**
     * Filters tasks in a project by status using Predicate<Task>.
     * Demonstrates: Functional Programming with Predicate (Week 3).
     */
    public List<Task> filterByStatus(String projectId, String status) {
        Predicate<Task> byStatus = t -> t.getStatus().equalsIgnoreCase(status);
        return getTasksForProject(projectId).stream()
                .filter(byStatus)
                .collect(Collectors.toList());
    }

    /**
     * Returns tasks using a custom Predicate for flexible filtering.
     */
    public List<Task> filterTasks(String projectId, Predicate<Task> predicate) {
        List<Task> tasks = getTasksForProject(projectId);
        if (tasks == null) return List.of();
        return tasks.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
