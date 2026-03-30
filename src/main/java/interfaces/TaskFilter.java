package interfaces;

import model.Task;

/**
 * TaskFilter — Custom functional interface for filtering tasks.
 * Demonstrates: Functional Interfaces (Week 3 Feature 2).
 *
 * This is equivalent to Predicate<Task> but named for domain clarity.
 */
@FunctionalInterface
public interface TaskFilter {

    /**
     * Tests whether the given task matches the filter condition.
     * @param task  the task to evaluate
     * @return true if the task passes the filter
     */
    boolean test(Task task);

    // ── Default combinator methods ─────────────────────────────────────────────

    /** Returns a filter that passes only if BOTH conditions are true (AND). */
    default TaskFilter and(TaskFilter other) {
        return task -> this.test(task) && other.test(task);
    }

    /** Returns a filter that passes if EITHER condition is true (OR). */
    default TaskFilter or(TaskFilter other) {
        return task -> this.test(task) || other.test(task);
    }

    /** Returns the logical negation of this filter (NOT). */
    default TaskFilter negate() {
        return task -> !this.test(task);
    }
}
