package service;

import model.*;
import exception.EmptyProjectException;

/**
 * ReportService — Generates completion reports for all projects.
 * Demonstrates: Iteration over task arrays, Completion % formula,
 *               Handling zero-task projects, Rounding to 2 decimal places.
 */
public class ReportService {

    // ── CORE METHODS REQUIRED ─────────────────────────────────────────────────

    /**
     * generateProjectReport — Generates and displays a report for a specific project.
     * Uses project.calculateCompletionPercentage() which might throw EmptyProjectException.
     */
    public void generateProjectReport(Project project) {
        System.out.println("\n  [ PROJECT REPORT: " + project.getName() + " ]");
        
        try {
            double pct = project.calculateCompletionPercentage();
            int total = project.getTaskCount();
            long completed = project.getTaskList().stream()
                    .filter(Task::isCompleted)
                    .count();

            System.out.println("  Status: ACTIVE");
            System.out.println("  Tasks: " + total + " total, " + completed + " completed");
            System.out.println("  Progress: " + pct + "% " + buildProgressBar(pct));
        } catch (EmptyProjectException e) {
            System.out.println("  Status: EMPTY");
            System.out.println("  Notice: " + e.getMessage());
            System.out.println("  Progress: 0.00% [░░░░░░░░░░]");
        }
        System.out.println("  Budget: $" + project.getBudget());
    }

    // ── Batch reporting ───────────────────────────────────────────────────────
    /**
     * Generates a StatusReport for every project in the given array.
     */
    public StatusReport[] generateReport(java.util.Collection<Project> projects) {
        return projects.stream()
                .map(project -> {
                    int totalTasks = project.getTaskCount();
                    long completedCount = project.getTaskList().stream()
                            .filter(Task::isCompleted)
                            .count();
                    return new StatusReport(project.getId(), project.getName(), totalTasks, (int) completedCount);
                })
                .toArray(StatusReport[]::new);
    }

    /**
     * Calculates the average completion percentage across all reports.
     * Returns 0.00 if the reports array is empty.
     */
    public double calculateAverageCompletion(StatusReport[] reports) {
        if (reports == null || reports.length == 0) return 0.00;
        double avg = java.util.Arrays.stream(reports)
                .mapToDouble(StatusReport::getCompletionPercentage)
                .average()
                .orElse(0.00);
        // Round to 2 decimal places
        return Math.round(avg * 100.0) / 100.0;
    }

    // ── Table display ─────────────────────────────────────────────────────────
    /**
     * Prints the formatted Project Status Report table to the console.
     */
    public void displayReport(StatusReport[] reports) {
        String separator = "  +" + repeat("-", 8) + "+" + repeat("-", 22)
                         + "+" + repeat("-", 8) + "+" + repeat("-", 11)
                         + "+" + repeat("-", 12) + "+";

        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════════════╗");
        System.out.println("  ║               PROJECT STATUS REPORT                         ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════════╝");
        System.out.println(separator);
        System.out.printf("  | %-6s | %-20s | %-6s | %-9s | %-10s |%n",
                "ID", "PROJECT NAME", "TASKS", "COMPLETED", "PROGRESS %");
        System.out.println(separator);

        for (StatusReport r : reports) {
            String progressBar = buildProgressBar(r.getCompletionPercentage());
            System.out.printf("  | %-6s | %-20s | %-6d | %-9d | %5.2f%%   |%n",
                    r.getProjectId(),
                    truncate(r.getProjectName(), 20),
                    r.getTotalTasks(),
                    r.getCompletedTasks(),
                    r.getCompletionPercentage());
            System.out.printf("  | %-6s   %-20s   %-6s   %-9s   %s%n",
                    "", "", "", "", progressBar);
        }

        System.out.println(separator);

        double avg = calculateAverageCompletion(reports);
        System.out.printf("  | %-6s | %-20s | %-6s | %-9s | %5.2f%%   |%n",
                "", "AVERAGE", "", "", avg);
        System.out.println(separator);
        System.out.println();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    /** Builds a simple ASCII progress bar (10 chars wide). */
    private String buildProgressBar(double percentage) {
        int blocks = (int) (percentage / 10);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < blocks; i++) sb.append("█");
        for (int i = blocks; i < 10; i++) sb.append("░");
        sb.append("]");
        return sb.toString();
    }

    /** Truncates a string to maxLen, appending "…" if needed. */
    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 1) + "…";
    }

    private String repeat(String s, int n) {
        if (n <= 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }
}
