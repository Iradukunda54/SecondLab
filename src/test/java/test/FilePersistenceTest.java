package test;

import model.*;
import service.ProjectService;
import utils.FileUtils;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FilePersistenceTest — JUnit 5 tests for File I/O with NIO.
 * Covers Feature 3: File Persistence (Week 3).
 */
public class FilePersistenceTest {

    private static final Path TEST_DIR  = Paths.get("data");
    private static final Path TEST_FILE = TEST_DIR.resolve("test_projects_data.json");

    @BeforeEach
    void setUp() throws IOException {
        // Configure FileUtils to use the test file
        FileUtils.setDataPath(TEST_FILE);
        // Clean up before each test
        Files.createDirectories(TEST_DIR);
        Files.deleteIfExists(TEST_FILE);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(TEST_FILE);
    }

    // ── Test Scenario 1: Load from missing file starts empty ─────────────────

    @Test
    void testLoadFromMissingFile_StartsEmpty() throws IOException {
        Map<String, Project> result = FileUtils.loadProjects();
        assertTrue(result.isEmpty(), "Should return empty map when file does not exist");
    }

    // ── Test Scenario 2: Save and reload projects ─────────────────────────────

    @Test
    void testSaveAndLoadProjects() throws IOException {
        // Create a test project
        SoftwareProject p = new SoftwareProject("P001", "Test App", "Test Desc", 5000.0, 3, "Java");
        p.addTask(new Task("T001", "Design UI", "Completed"));
        p.addTask(new Task("T002", "Write API", "In Progress"));

        // Save
        FileUtils.saveProjects(java.util.List.of(p));

        // Verify file was created
        assertTrue(Files.exists(TEST_FILE), "projects_data.json should be created");

        // Load
        Map<String, Project> loaded = FileUtils.loadProjects();

        assertFalse(loaded.isEmpty(), "Loaded map should not be empty");
        assertTrue(loaded.containsKey("P001"), "Should contain project P001");
    }

    // ── Test Scenario 3: Loaded project has correct data ─────────────────────

    @Test
    void testLoadedProjectHasTasks() throws IOException {
        SoftwareProject p = new SoftwareProject("P002", "IoT Hub", "Hardware desc", 10000.0, 5, "Python");
        p.addTask(new Task("T001", "Setup", "Pending"));
        p.addTask(new Task("T002", "Test",  "Completed"));

        FileUtils.saveProjects(java.util.List.of(p));

        Map<String, Project> loaded = FileUtils.loadProjects();
        Project loadedProject = loaded.get("P002");

        assertNotNull(loadedProject, "Project P002 should be loaded");
        assertEquals("IoT Hub", loadedProject.getName(), "Project name should match");
        assertEquals(2, loadedProject.getTaskCount(), "Project should have 2 tasks");
    }

    // ── Test Scenario 4: Save hardware project ────────────────────────────────

    @Test
    void testSaveAndLoadHardwareProject() throws IOException {
        HardwareProject h = new HardwareProject("P003", "Robot Arm", "6-axis arm", 200000.0, 10, "Mechatronics");
        h.addTask(new Task("T001", "Mechanical design", "Completed"));

        FileUtils.saveProjects(java.util.List.of(h));

        Map<String, Project> loaded = FileUtils.loadProjects();
        assertTrue(loaded.containsKey("P003"), "Should contain P003");
        assertInstanceOf(HardwareProject.class, loaded.get("P003"),
                "Loaded project should be HardwareProject");
    }

    // ── Test Scenario 5: Multiple projects saved and loaded ───────────────────

    @Test
    void testMultipleProjectsSaveLoad() throws IOException {
        SoftwareProject p1 = new SoftwareProject("P001", "Alpha", "Desc", 1000.0, 2, "Java");
        SoftwareProject p2 = new SoftwareProject("P002", "Beta",  "Desc", 2000.0, 3, "Python");

        FileUtils.saveProjects(java.util.List.of(p1, p2));

        Map<String, Project> loaded = FileUtils.loadProjects();
        assertEquals(2, loaded.size(), "Should load exactly 2 projects");
    }

    // ── Test Scenario 6: File content is valid JSON format ────────────────────

    @Test
    void testSavedFileIsJsonFormat() throws IOException {
        SoftwareProject p = new SoftwareProject("P001", "Test", "Desc", 1000.0, 1, "Java");
        FileUtils.saveProjects(java.util.List.of(p));

        String content = Files.readString(TEST_FILE);
        assertTrue(content.startsWith("["),   "JSON should start with [");
        assertTrue(content.trim().endsWith("]"), "JSON should end with ]");
        assertTrue(content.contains("\"projectId\""), "JSON should contain projectId key");
        assertTrue(content.contains("\"tasks\""),     "JSON should contain tasks key");
    }
}
