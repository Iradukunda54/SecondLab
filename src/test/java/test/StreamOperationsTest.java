package test;

import model.*;
import service.ProjectService;
import service.StreamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StreamOperationsTest — JUnit 5 tests for Streams API features.
 * Covers Feature 1: Collections & Streams (Week 3).
 */
public class StreamOperationsTest {

    private StreamService streamService;
    private List<Project> projects;

    @BeforeEach
    void setUp() {
        streamService = new StreamService();

        // Build test projects with tasks
        SoftwareProject p1 = new SoftwareProject("P001", "Alpha", "Desc", 50000.0, 4, "Java");
        p1.addTask(new Task("T001", "Design DB",   "Completed"));
        p1.addTask(new Task("T002", "Build API",   "Completed"));
        p1.addTask(new Task("T003", "Write Tests", "In Progress"));

        SoftwareProject p2 = new SoftwareProject("P002", "Beta", "Desc", 80000.0, 6, "Python");
        p2.addTask(new Task("T001", "Setup server", "Pending"));

        HardwareProject p3 = new HardwareProject("P003", "Gamma", "Desc", 30000.0, 3, "Embedded");
        p3.addTask(new Task("T001", "PCB Design",   "Completed"));
        p3.addTask(new Task("T002", "Firmware",     "Completed"));

        projects = List.of(p1, p2, p3);
    }

    // ── Test: filter completed tasks using Streams ────────────────────────────

    @Test
    void testFilterCompletedTasksUsingStreams() {
        // Manually filter using stream
        long completed = projects.stream()
                .flatMap(p -> p.getTaskList().stream())
                .filter(t -> t.getStatus().equals("Completed"))
                .count();

        assertEquals(4, completed, "Should count 4 completed tasks across all projects");
    }

    // ── Test: high-completion project filter ──────────────────────────────────

    @Test
    void testGetCompletedProjectsAboveThreshold() {
        List<Project> highCompletion = streamService.getHighCompletionProjects(projects, 60.0);
        // P001 = 2/3 = 66.7%, P003 = 2/2 = 100%
        // P002 = 0/1 = 0%
        assertEquals(2, highCompletion.size(), "Should find 2 projects above 60%");
    }

    // ── Test: average budget calculation ─────────────────────────────────────

    @Test
    void testAverageBudgetCalculation() {
        double avg = streamService.getAverageBudget(projects);
        double expected = (50000.0 + 80000.0 + 30000.0) / 3;
        assertEquals(expected, avg, 0.01, "Average budget should be correct");
    }

    // ── Test: total budget with Streams ──────────────────────────────────────

    @Test
    void testTotalBudgetUsingStreams() {
        double total = streamService.getTotalBudget(projects);
        assertEquals(160000.0, total, 0.01, "Total budget should be sum of all projects");
    }

    // ── Test: project names CSV ───────────────────────────────────────────────

    @Test
    void testGetProjectNamesCsv() {
        String csv = streamService.getProjectNamesCsv(projects);
        assertTrue(csv.contains("Alpha"), "CSV should contain 'Alpha'");
        assertTrue(csv.contains("Beta"),  "CSV should contain 'Beta'");
        assertTrue(csv.contains("Gamma"), "CSV should contain 'Gamma'");
    }

    // ── Test: group projects by type ─────────────────────────────────────────

    @Test
    void testGroupByType() {
        var groups = streamService.groupByType(projects);
        assertTrue(groups.containsKey("Software"), "Should have Software group");
        assertTrue(groups.containsKey("Hardware"), "Should have Hardware group");
        assertEquals(2, groups.get("Software").size(), "Should have 2 Software projects");
        assertEquals(1, groups.get("Hardware").size(), "Should have 1 Hardware project");
    }

    // ── Test: flatMap all tasks ───────────────────────────────────────────────

    @Test
    void testGetAllTasksFlatMap() {
        List<Task> allTasks = streamService.getAllTasks(projects);
        assertEquals(6, allTasks.size(), "Should return all 6 tasks across all projects");
    }

    // ── Test: count completed tasks across all projects ───────────────────────

    @Test
    void testCountCompletedTasksAcrossProjects() {
        long count = streamService.countCompletedTasksAcrossProjects(projects);
        assertEquals(4, count, "Should count 4 completed tasks");
    }
}
