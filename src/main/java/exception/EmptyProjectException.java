package exception;

/**
 * EmptyProjectException — Thrown when attempting an operation on a project with no tasks.
 */
public class EmptyProjectException extends RuntimeException {
    public EmptyProjectException(String message) {
        super(message);
    }
}
