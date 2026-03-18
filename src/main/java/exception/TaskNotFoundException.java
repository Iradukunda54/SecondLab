package exception;

/**
 * TaskNotFoundException — Thrown when a task cannot be found by its ID.
 */
public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
