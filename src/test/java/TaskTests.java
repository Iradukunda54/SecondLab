


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Task;
import model.SoftwareProject;
import service.ProjectService;
import service.TaskService;
import exception.InvalidInputException;
import exception.TaskNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TaskTests — Unit tests for Task operations using JUnit 5.
 */
public class TaskTests {

    private ProjectService projectService;
    private TaskService taskService;

    @BeforeEach
    void setUp() throws Exception {
        projectService = new ProjectService();
        taskService = new TaskService(projectService);

        // Add a base project for testing
        projectService.createProject(new SoftwareProject("P006", "Test", "Desc", 1000.0, 5, 5, "Java"));
    }

    @Test
    void testCreateTask_Success() throws Exception {
        Task task = new Task("T001", "New Task", "Pending");
        taskService.createTask("P006", task);

        Task retrieved = taskService.getTaskById("P006", "T001");
        assertNotNull(retrieved);
        assertEquals("New Task", retrieved.getName());
    }

    @Test
    void testUpdateTaskStatus_Success() throws Exception {
        taskService.createTask("P006", new Task("T001", "Task", "Pending"));
        taskService.updateTaskStatus("P006", "T001", "Completed");

        Task retrieved = taskService.getTaskById("P006", "T001");
        assertTrue(retrieved.isCompleted());
    }

    @Test
    void testUpdateTaskStatus_TaskNotFound_ThrowsException() {
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTaskStatus("P006", "NON-EXISTENT", "Completed");
        });
    }

   @Test
   void testCreateTask_InvalidInput_ThrowsException() {
       assertThrows(InvalidInputException.class, () -> {
           taskService.createTask("P007", null);
       });
    }
}
