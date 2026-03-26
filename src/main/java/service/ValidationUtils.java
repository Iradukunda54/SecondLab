package service;

import exception.InvalidProjectDataException;
import java.util.regex.Matcher;

import java.util.regex.Pattern;

/**
 * ValidationUtils — Centralised input validation helper.
 * Demonstrates: Static utility methods, Input validation, Error messaging.
 */
public class ValidationUtils {

    // ── Regex patterns ────────────────────────────────────────────────────────
    /** Project ID must match P followed by exactly 3 digits, e.g. P001. */
    private static final Pattern PROJECT_ID_PATTERN = Pattern.compile("^P(\\d{3})$");

    /** Task ID must match T followed by exactly 3 digits, e.g. T001. */
    private static final Pattern TASK_ID_PATTERN = Pattern.compile("^T\\d{3}$");

    /**
     * Validates Email format strictly requiring @gmail.com.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@gmail\\.com$");

    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            printError("Email cannot be empty.");
            return false;
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            printError("Invalid email format. Must end with @gmail.com (e.g. user@gmail.com).");
            return false;
        }
        return true;
    }

    /**
     * Validates Project ID format: must match P001–P999.
     * Throws InvalidProjectDataException for logic errors.
     */
    public static boolean validateProjectId(String id) {
        if (id == null || id.trim().isEmpty()) {
            printError("Project ID cannot be empty.");
            return false;
        }
        String trimmed = id.trim();
        Matcher m = PROJECT_ID_PATTERN.matcher(trimmed);
        if (!m.matches()) {
            printError("Invalid Project ID format. Expected: P001.");
            return false;
        }
        // Requirement 2: Project ID must be positive
        int numericPart = Integer.parseInt(m.group(1));
        if (numericPart <= 0) {
            printError("Project ID numeric part must be positive (P001 or greater).");
            return false;
        }
        return true;
    }

    /**
     * Validates Task ID format: must match T001–T999.
     */
    public static boolean validateTaskId(String id) {
        if (id == null || id.trim().isEmpty()) {
            printError("Task ID cannot be empty.");
            return false;
        }
        if (!TASK_ID_PATTERN.matcher(id.trim()).matches()) {
            printError("Invalid Task ID format. Expected: T001.");
            return false;
        }
        return true;
    }

    /**
     * Validates task status: must be Pending, In Progress, or Completed.
     */
    public static boolean validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            printError("Status cannot be empty.");
            return false;
        }
        String s = status.trim();
        if (!s.equals("Pending") && !s.equals("In Progress") && !s.equals("Completed")) {
            printError("Invalid status. Must be one of: Pending | In Progress | Completed");
            return false;
        }
        return true;
    }

    /**
     * Validates team size: must be greater than 0.
     */
    public static boolean validateTeamSize(int teamSize) {
        if (teamSize <= 0) {
            printError("Team size must be greater than 0.");
            return false;
        }
        return true;
    }

    /**
     * Validates budget: must be positive (> 0).
     */
    public static boolean validateBudget(double budget) {
        if (budget <= 0) {
            printError("Budget must be a positive number.");
            return false;
        }
        return true;
    }

    /**
     * Validates that a name/string field is non-empty.
     */
    public static boolean validateNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            printError(fieldName + " cannot be empty.");
            return false;
        }
        return true;
    }

    /**
     * Tries to parse an integer from the given string.
     * Prints an error and returns Integer.MIN_VALUE on failure.
     */
    public static int parseIntSafely(String input, String fieldName) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            printError(fieldName + " must be a valid whole number.");
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Tries to parse a double from the given string.
     * Prints an error and returns Double.NaN on failure.
     */
    public static double parseDoubleSafely(String input, String fieldName) {
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            printError(fieldName + " must be a valid number (e.g. 5000.00).");
            return Double.NaN;
        }
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private static void printError(String msg) {
        System.out.println("  [ERROR] " + msg);
    }
}
