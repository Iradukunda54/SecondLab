package model;

import exception.EmptyProjectException;
import model.Task;

/**
 * Project — Abstract base class for all project types.
 * Demonstrates: Abstract class, Encapsulation, Polymorphism.
 */
public abstract class Project {

    // ── Fields ───────────────────────────────────────────────────────────────
    private String id;
    private String name;
    private String description;
    private double budget;
    private int    teamSize;

    // ── Task storage (array-based, no external libraries) ─────────────────────
    private Task[] tasks;
    private int    taskCount;
    private int    maxTasks;

    // ── Constructor ───────────────────────────────────────────────────────────
    public Project(String id, String name, String description,
                   double budget, int teamSize, int maxTasks) {
        this.id          = id;
        this.name        = name;
        this.description = description;
        this.budget      = budget;
        this.teamSize    = teamSize;
        this.maxTasks    = maxTasks;
        this.tasks       = new Task[maxTasks];
        this.taskCount   = 0;
    }

    // ── Abstract method ───────────────────────────────────────────────────────
    public abstract String getProjectDetails();

    // ── Business Logic ───────────────────────────────────────────────────────
    /**
     * Calculates the completion percentage of the project.
     * @throws EmptyProjectException if the project has no tasks.
     */
    public double calculateCompletionPercentage() throws EmptyProjectException {
        if (taskCount == 0) {
            throw new EmptyProjectException("Project '" + name + "' (" + id + ") has no tasks.");
        }
        int completedCount = 0;
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].isCompleted()) {
                completedCount++;
            }
        }
        double percentage = ((double) completedCount / taskCount) * 100;
        return Math.round(percentage * 100.0) / 100.0;
    }

    // ── Task management ───────────────────────────────────────────────────────
    /**
     * Adds a task to this project's task array.
     * @return false if the task array is full or a duplicate ID exists.
     */
    public boolean addTask(Task task) {
        if (taskCount >= maxTasks) {
            System.out.println("  [!] Task limit reached for project " + id);
            return false;
        }
        // Check for duplicate task IDs
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getId().equalsIgnoreCase(task.getId())) {
                System.out.println("  [!] Duplicate task ID: " + task.getId());
                return false;
            }
        }
        tasks[taskCount++] = task;
        return true;
    }

    /**
     * Returns the task at index i (0-based).
     */
    public Task getTask(int index) {
        if (index < 0 || index >= taskCount) return null;
        return tasks[index];
    }

    public Task[] getTasks()   { return tasks; }
    public int    getTaskCount() { return taskCount; }

    /**
     * Finds and returns a task by its ID, or null if not found.
     */
    public Task findTaskById(String taskId) {
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getId().equalsIgnoreCase(taskId)) {
                return tasks[i];
            }
        }
        return null;
    }

    // ── Concrete display ──────────────────────────────────────────────────────
    /**
     * Concrete method displaying common project fields + type-specific details.
     */
    public void displayProject() {
        System.out.println("  ┌─────────────────────────────────────────────┐");
        System.out.printf ("  │ ID          : %-29s│%n", id);
        System.out.printf ("  │ Name        : %-29s│%n", name);
        System.out.printf ("  │ Description : %-29s│%n",
                description.length() > 29 ? description.substring(0, 26) + "..." : description);
        System.out.printf ("  │ Budget      : $%-28.2f│%n", budget);
        System.out.printf ("  │ Team Size   : %-29d│%n", teamSize);
        System.out.printf ("  │ Type Detail : %-29s│%n", getProjectDetails());
        System.out.printf ("  │ Tasks       : %-29d│%n", taskCount);
        System.out.println("  └─────────────────────────────────────────────┘");
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getId()          { return id; }
    public String getName()        { return name; }
    public String getDescription() { return description; }
    public double getBudget()      { return budget; }
    public int    getTeamSize()    { return teamSize; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setName(String name)               { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setBudget(double budget)           { this.budget = budget; }
    public void setTeamSize(int teamSize)          { this.teamSize = teamSize; }

    @Override
    public String toString() {
        return id + " | " + name + " | Tasks: " + taskCount;
    }
}
