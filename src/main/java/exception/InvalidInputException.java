package exception;

/**
 * InvalidInputException — Thrown when user input is invalid or improperly formatted.
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
