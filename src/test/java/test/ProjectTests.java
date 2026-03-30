package test;

import model.HardwareProject;
import model.Project;
import model.SoftwareProject;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import exception.EmptyProjectException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ProjectTests — Unit tests for the Project model using JUnit 5.
 */
public class ProjectTests {

    private Project softwareProject;
    private Project hardwareProject;

    @BeforeEach
    void setUp() {
        softwareProject = new SoftwareProject("P001", "Web App", "Java project", 50000.0, 5, "Java");
        hardwareProject = new HardwareProject("P002", "IoT Hub", "Embedded project", 30000.0, 3, "ESP32");
    }

    @Test
    void testCalculateCompletionPercentage_EmptyProject_ThrowsException() {
        EmptyProjectException exc = assertThrows(EmptyProjectException.class, () -> {
            softwareProject.calculateCompletionPercentage();
        });
        System.out.println(exc.getMessage());

    }

    @Test
    void testCalculateCompletionPercentage_PartialCompletion() throws Exception {
        softwareProject.addTask(new Task("T001", "Task 1", "Completed"));
        softwareProject.addTask(new Task("T002", "Task 2", "Pending"));

        assertEquals(50.0, softwareProject.calculateCompletionPercentage(), "Should be 50%");
    }

    @Test
    void testCalculateCompletionPercentage_FullCompletion() throws Exception {
        softwareProject.addTask(new Task("T001", "Task 1", "Completed"));
        assertEquals(100.0, softwareProject.calculateCompletionPercentage());
    }

    @Test
    void testAddTask_DuplicateId() {
        Task t1 = new Task("T001", "T1", "Pending");
        assertTrue(softwareProject.addTask(t1));
        assertFalse(softwareProject.addTask(new Task("T001", "T1-Dup", "Pending")), "Should not add duplicate ID");
    }

    @Test
    void testHardwareProjectDetails() {
        assertTrue(hardwareProject.getProjectDetails().contains("Hardware"));
        assertEquals("IoT Hub", hardwareProject.getName());
    }
}