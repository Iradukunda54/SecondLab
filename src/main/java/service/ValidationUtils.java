package service;

import exception.InvalidProjectDataException;
import utils.RegexValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ValidationUtils — Centralised input validation helper.
 * Delegates ID and email validation to RegexValidator (Week 3).
 * Demonstrates: SOLID (Single Responsibility), delegation pattern.
 */
public class ValidationUtils {

    // Utility class — prevent instantiation
    private ValidationUtils() {}

    // ── ID Validation (delegates to RegexValidator) ───────────────────────────

    /**
     * Validates Project ID format: must match P### (e.g., P001).
     * Uses RegexValidator (Pattern/Matcher delegation).
     */
    public static boolean validateProjectId(String id) {
        if (id == null || id.trim().isEmpty()) {
            printError("Project ID cannot be empty.");
            return false;
        }
        if (!RegexValidator.isValidProjectId(id)) {
            printError("Invalid Project ID format. Expected: "
                    + RegexValidator.getProjectIdPattern()
                    + " (e.g., P001)");
            return false;
        }
        return true;
    }

    /**
     * Validates Task ID format: must match T### (e.g., T001).
     * Uses RegexValidator (Pattern/Matcher delegation).
     */
    public static boolean validateTaskId(String id) {
        if (id == null || id.trim().isEmpty()) {
            printError("Task ID cannot be empty.");
            return false;
        }
        if (!RegexValidator.isValidTaskId(id)) {
            printError("Invalid Task ID format. Expected: "
                    + RegexValidator.getTaskIdPattern()
                    + " (e.g., T001)");
            return false;
        }
        return true;
    }

    /**
     * Validates email: must end with @gmail.com.
     * Uses RegexValidator (Pattern/Matcher delegation).
     */
    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            printError("Email cannot be empty.");
            return false;
        }
        if (!RegexValidator.isValidEmail(email)) {
            printError("Invalid email. Must be " + RegexValidator.getEmailPattern()
                    + " (e.g., user@gmail.com).");
            return false;
        }
        return true;
    }

    // ── Other field validators ────────────────────────────────────────────────

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

    /** Validates team size > 0. */
    public static boolean validateTeamSize(int teamSize) {
        if (teamSize <= 0) {
            printError("Team size must be greater than 0.");
            return false;
        }
        return true;
    }

    /** Validates budget > 0. */
    public static boolean validateBudget(double budget) {
        if (budget <= 0) {
            printError("Budget must be a positive number.");
            return false;
        }
        return true;
    }

    /** Validates a non-empty string field. */
    public static boolean validateNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            printError(fieldName + " cannot be empty.");
            return false;
        }
        return true;
    }

    /** Parses int safely; returns Integer.MIN_VALUE on failure. */
    public static int parseIntSafely(String input, String fieldName) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            printError(fieldName + " must be a valid whole number.");
            return Integer.MIN_VALUE;
        }
    }

    /** Parses double safely; returns Double.NaN on failure. */
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
