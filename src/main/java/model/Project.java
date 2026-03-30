package model;

import exception.EmptyProjectException;
import java.util.ArrayList;
import java.util.List;

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

    // ── Task storage (Collections-based) ─────────────────────────────────────
    private List<Task> tasks;

    // ── Constructor ───────────────────────────────────────────────────────────
    public Project(String id, String name, String description,
                   double budget, int teamSize) {
        this.id          = id;
        this.name        = name;
        this.description = description;
        this.budget      = budget;
        this.teamSize    = teamSize;
        this.tasks       = new ArrayList<>();
    }

    // ── Abstract method ───────────────────────────────────────────────────────
    public abstract String getProjectDetails();

    // ── Business Logic ───────────────────────────────────────────────────────
    /**
     * Calculates the completion percentage of the project.
     * @throws EmptyProjectException if the project has no tasks.
     */
    public double calculateCompletionPercentage()  {
        if (tasks.isEmpty()) {
            throw new EmptyProjectException("Project '" + name + "' (" + id + ") has no tasks.");
        }
        long completedCount = tasks.stream()
                .filter(Task::isCompleted)
                .count();
        double percentage = ((double) completedCount / tasks.size()) * 100;
        return Math.round(percentage * 100.0) / 100.0;
    }

    // ── Task management ───────────────────────────────────────────────────────
    /**
     * Adds a task to this project's task array.
     * @return false if the task array is full or a duplicate ID exists.
     */
    public boolean addTask(Task task) {
        // Check for duplicate task IDs using Streams
        boolean duplicate = tasks.stream()
                .anyMatch(t -> t.getId().equalsIgnoreCase(task.getId()));
        
        if (duplicate) {
            System.out.println("  [!] Duplicate task ID: " + task.getId());
            return false;
        }
        
        return tasks.add(task);
    }

    /**
     * Returns the task at index i (0-based).
     */
    public Task getTask(int index) {
        if (index < 0 || index >= tasks.size()) return null;
        return tasks.get(index);
    }

    public List<Task> getTaskList() { return tasks; }
    public Task[] getTasks()      { return tasks.toArray(new Task[0]); }
    public int    getTaskCount()  { return tasks.size(); }

    /**
     * Finds and returns a task by its ID, or null if not found.
     */
    public Task findTaskById(String taskId) {
        return tasks.stream()
                .filter(t -> t.getId().equalsIgnoreCase(taskId))
                .findFirst()
                .orElse(null);
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
        System.out.printf ("  │ Tasks       : %-29d│%n", tasks.size());
        System.out.println("  └─────────────────────────────────────────────┘");
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getId()          { return id; }
    public String getName()        { return name; }
    public String getDescription() { return description; }
    public double getBudget()      { return budget; }
    public int    getTeamSize()    { return teamSize; }

    /** Returns the project type — overridden by subclasses. */
    public String getType()        { return "Unknown"; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setName(String name)               { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setBudget(double budget)           { this.budget = budget; }
    public void setTeamSize(int teamSize)          { this.teamSize = teamSize; }

    @Override
    public String toString() {
        return id + " | " + name + " | Tasks: " + tasks.size();
    }
}
