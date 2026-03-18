package service;

import model.Project;
import model.Task;
import exception.InvalidInputException;
import exception.InvalidTaskDataException;
import exception.TaskNotFoundException;

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
     * Returns the tasks array for a given project, or null if project not found.
     */
    public Task[] getTasksForProject(String projectId) {
        Project project = projectService.findById(projectId);
        if (project == null) return null;
        // Return a properly sized copy
        int   count  = project.getTaskCount();
        Task[] result = new Task[count];
        for (int i = 0; i < count; i++) {
            result[i] = project.getTask(i);
        }
        return result;
    }

    /**
     * Checks whether a task ID already exists within a project (duplicate guard).
     */
    public boolean taskIdExists(String projectId, String taskId) {
        Project project = projectService.findById(projectId);
        if (project == null) return false;
        return project.findTaskById(taskId) != null;
    }
}
