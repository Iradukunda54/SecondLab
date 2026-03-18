package exception;

/**
 * InvalidProjectDataException — Thrown when project data (like budget or ID) fails validation.
 */
public class InvalidProjectDataException extends RuntimeException {
    public InvalidProjectDataException(String message) {
        super(message);
    }
}
