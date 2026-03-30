package service;

import model.Project;
import model.Task;

import java.util.*;
import java.util.stream.Collectors;

/**
 * StreamService — Demonstrates advanced Streams API operations.
 * Demonstrates: filter(), map(), reduce(), sorted(), collect(),
 *               mapToDouble(), average(), Lambdas, Method References (Week 3).
 */
public class StreamService {

    // ── Project-level Stream operations ──────────────────────────────────────

    /**
     * Returns projects whose completion rate is at or above threshold (%).
     * Uses: filter(), lambda, try-catch inside lambda.
     */
    public List<Project> getHighCompletionProjects(Collection<Project> projects, double threshold) {
        return projects.stream()
                .filter(p -> {
                    try {
                        return p.calculateCompletionPercentage() >= threshold;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns all projects sorted by completion percentage (descending).
     * Uses: sorted(), Comparator.comparingDouble(), reversed()
     */
    public List<Project> getSortedByCompletion(Collection<Project> projects) {
        return projects.stream()
                .sorted(Comparator.comparingDouble(p -> {
                    try { return -p.calculateCompletionPercentage(); }
                    catch (Exception e) { return 0.0; }
                }))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the average budget across all projects.
     * Uses: mapToDouble(), average(), OptionalDouble
     */
    public double getAverageBudget(Collection<Project> projects) {
        return projects.stream()
                .mapToDouble(Project::getBudget)   // method reference
                .average()
                .orElse(0.0);
    }

    /**
     * Returns the total budget of all projects combined.
     * Uses: mapToDouble(), sum()
     */
    public double getTotalBudget(Collection<Project> projects) {
        return projects.stream()
                .mapToDouble(Project::getBudget)
                .sum();
    }

    /**
     * Returns project names as a comma-separated string.
     * Uses: map(), Collectors.joining()
     */
    public String getProjectNamesCsv(Collection<Project> projects) {
        return projects.stream()
                .map(Project::getName)             // method reference
                .collect(Collectors.joining(", "));
    }

    /**
     * Groups projects by type (Software / Hardware).
     * Uses: Collectors.groupingBy(), instanceof pattern
     */
    public Map<String, List<Project>> groupByType(Collection<Project> projects) {
        return projects.stream()
                .collect(Collectors.groupingBy(Project::getType));
    }

    // ── Task-level Stream operations ─────────────────────────────────────────

    /**
     * Returns all tasks from all projects, flattened into a single list.
     * Uses: flatMap(), method reference
     */
    public List<Task> getAllTasks(Collection<Project> projects) {
        return projects.stream()
                .flatMap(p -> p.getTaskList().stream())
                .collect(Collectors.toList());
    }

    /**
     * Counts how many tasks across all projects are "Completed".
     * Uses: flatMap(), filter(), count()
     */
    public long countCompletedTasksAcrossProjects(Collection<Project> projects) {
        return projects.stream()
                .flatMap(p -> p.getTaskList().stream())
                .filter(Task::isCompleted)         // method reference
                .count();
    }

    /**
     * Prints a Stream-based summary report.
     */
    public void printStreamSummary(Collection<Project> projects) {
        System.out.println("\n  ╔══════════════════ STREAM SUMMARY ══════════════════╗");
        System.out.printf ("  ║  Total Projects  : %-33d║%n", projects.size());
        System.out.printf ("  ║  Average Budget  : $%-32.2f║%n", getAverageBudget(projects));
        System.out.printf ("  ║  Total Budget    : $%-32.2f║%n", getTotalBudget(projects));
        System.out.printf ("  ║  Completed Tasks : %-33d║%n",
                countCompletedTasksAcrossProjects(projects));

        List<Project> highCompletion = getHighCompletionProjects(projects, 70.0);
        System.out.printf ("  ║  Projects > 70%% : %-33d║%n", highCompletion.size());
        System.out.println("  ╚═════════════════════════════════════════════════════╝");

        if (!highCompletion.isEmpty()) {
            System.out.println("\n  Projects with completion > 70%:");
            highCompletion.forEach(p ->
                    System.out.printf("    ► %-20s│ %.0f%%\n",
                            p.getName(),
                            safeCompletion(p)));
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────
    private double safeCompletion(Project p) {
        try { return p.calculateCompletionPercentage(); }
        catch (Exception e) { return 0.0; }
    }
}
