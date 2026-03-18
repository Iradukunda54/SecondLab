
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import service.ValidationUtils;

/**
 * ValidationTests — Unit tests for Validation utilities using JUnit 5.
 */
public class ValidationTests {

    @Test
    void testValidateEmail() {
        assertTrue(ValidationUtils.validateEmail("test@example.com"));
        assertFalse(ValidationUtils.validateEmail("invalid-email"));
    }

    @Test
    void testValidateBudget() {
        assertTrue(ValidationUtils.validateBudget(1500.0));
        assertFalse(ValidationUtils.validateBudget(-1.0));
        assertFalse(ValidationUtils.validateBudget(0));
    }

    @Test
    void testValidateProjectId() {
        // Updated for Requirement 2 (Positive ID check)
        assertTrue(ValidationUtils.validateProjectId("P001"));
        assertFalse(ValidationUtils.validateProjectId("P000"));
        assertFalse(ValidationUtils.validateProjectId("123"));
        assertFalse(ValidationUtils.validateProjectId("PRJ-01"));
    }

    @Test
    void testParseIntSafely() {
        assertEquals(10, ValidationUtils.parseIntSafely("10", "Field"));
        assertEquals(Integer.MIN_VALUE, ValidationUtils.parseIntSafely("invalid", "Field"));
    }
}
