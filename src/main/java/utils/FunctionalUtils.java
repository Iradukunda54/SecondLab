package utils;

import interfaces.TaskFilter;
import model.Task;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * FunctionalUtils — Demonstrates Functional Programming constructs.
 * Demonstrates: Lambdas, Functional Interfaces, Method References, Streams (Week 3).
 */
public final class FunctionalUtils {

    private FunctionalUtils() {}

    // ── Using custom TaskFilter functional interface ───────────────────────────

    /**
     * Filters a list of tasks using the TaskFilter functional interface.
     * Usage: filterTasks(tasks, t -> t.getStatus().equals("Completed"))
     */
    public static List<Task> filterTasks(List<Task> tasks, TaskFilter filter) {
        return tasks.stream()
                .filter(filter::test)   // method reference to functional interface
                .collect(Collectors.toList());
    }

    // ── Using java.util.function.Predicate ────────────────────────────────────

    /**
     * Filters tasks using a standard java.util.function.Predicate.
     */
    public static List<Task> filterWithPredicate(List<Task> tasks, Predicate<Task> predicate) {
        return tasks.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    // ── Counting with Streams ─────────────────────────────────────────────────

    /**
     * Counts tasks that match a given status using Streams and lambdas.
     */
    public static long countByStatus(List<Task> tasks, String status) {
        return tasks.stream()
                .filter(t -> t.getStatus().equalsIgnoreCase(status))
                .count();
    }

    /**
     * Counts completed tasks using a Method Reference.
     */
    public static long countCompleted(List<Task> tasks) {
        return tasks.stream()
                .filter(Task::isCompleted)   // method reference
                .count();
    }

    // ── Grouping with Streams ─────────────────────────────────────────────────

    /**
     * Groups tasks by their status using Collectors.groupingBy.
     */
    public static Map<String, List<Task>> groupByStatus(List<Task> tasks) {
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus));
    }

    // ── Transforming with Function ────────────────────────────────────────────

    /**
     * Extracts task names using Function<Task, String> and method reference.
     */
    public static List<String> extractNames(List<Task> tasks) {
        Function<Task, String> getName = Task::getName;
        return tasks.stream()
                .map(getName)
                .collect(Collectors.toList());
    }

    // ── Pre-built TaskFilter lambdas for common use cases ─────────────────────

    /** Filter: only completed tasks. */
    public static final TaskFilter IS_COMPLETED = task ->
            "Completed".equalsIgnoreCase(task.getStatus());

    /** Filter: only pending tasks. */
    public static final TaskFilter IS_PENDING = task ->
            "Pending".equalsIgnoreCase(task.getStatus());

    /** Filter: only in-progress tasks. */
    public static final TaskFilter IS_IN_PROGRESS = task ->
            "In Progress".equalsIgnoreCase(task.getStatus());
}
