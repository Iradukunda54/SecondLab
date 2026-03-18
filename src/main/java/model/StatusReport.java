package model;

/**
 * StatusReport — Data holder for a single project's completion statistics.
 * Used by ReportService to generate the project status table.
 */
public class StatusReport {

    // ── Fields ───────────────────────────────────────────────────────────────
    private String projectId;
    private String projectName;
    private int totalTasks;
    private int completedTasks;
    private double completionPercentage;

    // ── Constructor ───────────────────────────────────────────────────────────
    public StatusReport(String projectId, String projectName,
            int totalTasks, int completedTasks) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        // Formula: (completed / total) * 100, handle zero-task projects
        this.completionPercentage = (totalTasks == 0)
                ? 0.00
                : Math.round(((double) completedTasks / totalTasks) * 100 * 100.0) / 100.0;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public double getCompletionPercentage() {
        return completionPercentage;
    }
}
