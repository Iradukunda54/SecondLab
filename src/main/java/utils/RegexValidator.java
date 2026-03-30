package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RegexValidator — Centralizes all regex-based input validation.
 * Demonstrates: Pattern, Matcher, Regex (Week 3 Feature 2).
 *
 * Rules:
 *   Task ID    → T\d{3}  e.g. T001
 *   Project ID → P\d{3}  e.g. P001
 *   Email      → must end with @gmail.com
 */
public final class RegexValidator {

    // ── Compiled patterns (compiled once for performance) ─────────────────────
    private static final Pattern TASK_ID_PATTERN    = Pattern.compile("^T(?!000)\\d{3}$");
    private static final Pattern PROJECT_ID_PATTERN = Pattern.compile("^P(?!000)\\d{3}$");
    private static final Pattern EMAIL_PATTERN      = Pattern.compile(
            "^[a-zA-Z0-9._%+\\-]+@gmail\\.com$");

    // Utility class — no instantiation
    private RegexValidator() {}

    // ── Public validation methods ─────────────────────────────────────────────

    /**
     * Validates task IDs: must match T### (e.g., T001, T999).
     * Invalid formats: TX01, TASK001, t001
     */
    public static boolean isValidTaskId(String id) {
        if (id == null) return false;
        Matcher m = TASK_ID_PATTERN.matcher(id.trim());
        return m.matches();
    }

    /**
     * Validates project IDs: must match P### (e.g., P001, P999).
     * Invalid formats: PX01, PRJ001, p001
     */
    public static boolean isValidProjectId(String id) {
        if (id == null) return false;
        Matcher m = PROJECT_ID_PATTERN.matcher(id.trim());
        return m.matches();
    }

    /**
     * Validates email addresses: must end with @gmail.com.
     * Invalid: user@example.com, user@yahoo.com
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        Matcher m = EMAIL_PATTERN.matcher(email.trim());
        return m.matches();
    }

    /**
     * Returns the task ID pattern string (useful for error messages).
     */
    public static String getTaskIdPattern()    { return "T### (e.g., T001)"; }
    public static String getProjectIdPattern() { return "P### (e.g., P001)"; }
    public static String getEmailPattern()     { return "name@gmail.com"; }
}
