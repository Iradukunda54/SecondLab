package exception;

/**
 * InvalidTaskDataException — Thrown when task validation (title, status, etc.) fails.
 */
public class InvalidTaskDataException extends RuntimeException {
    public InvalidTaskDataException(String message) {
        super(message);
    }
}
