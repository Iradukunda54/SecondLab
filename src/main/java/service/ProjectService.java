package service;

import model.*;
import exception.InvalidProjectDataException;

/**
 * ProjectService — Manages the in-memory array of projects.
 * Demonstrates: Service layer, Array management, Pre-loaded sample data.
 */
public class ProjectService {

    // ── Storage ───────────────────────────────────────────────────────────────
    private static final int MAX_PROJECTS = 100;
    private Project[] projects;
    private int       projectCount;

    // ── Constructor — pre-loads 5 sample projects ─────────────────────────────
    public ProjectService() {
        projects     = new Project[MAX_PROJECTS];
        projectCount = 0;
        loadSampleProjects();
    }

    // ── Sample data ───────────────────────────────────────────────────────────
    private void loadSampleProjects() {
        // 3 Software projects
        SoftwareProject p1 = new SoftwareProject(
                "P001", "Alpha Tracker",
                "Employee time-tracking web app", 45000.00, 4, 10, "Java");
        addSampleTask(p1, "T001", "Design DB schema",  "Completed");
        addSampleTask(p1, "T002", "Build REST API",    "Completed");
        addSampleTask(p1, "T003", "Frontend UI",       "In Progress");
        addSampleTask(p1, "T004", "Write unit tests",  "Pending");
        projects[projectCount++] = p1;

        SoftwareProject p2 = new SoftwareProject(
                "P002", "Beta Commerce",
                "E-commerce platform with cart and payments", 120000.00, 8, 15, "Python");
        addSampleTask(p2, "T001", "Authentication module", "Completed");
        addSampleTask(p2, "T002", "Product catalogue",     "Completed");
        addSampleTask(p2, "T003", "Shopping cart",         "Completed");
        addSampleTask(p2, "T004", "Payment gateway",       "In Progress");
        addSampleTask(p2, "T005", "Order history",         "Pending");
        projects[projectCount++] = p2;

        SoftwareProject p3 = new SoftwareProject(
                "P003", "Delta Analytics",
                "Real-time data analytics dashboard", 75000.00, 5, 10, "TypeScript");
        addSampleTask(p3, "T001", "Data ingestion pipeline", "Completed");
        addSampleTask(p3, "T002", "Dashboard charts",        "Completed");
        addSampleTask(p3, "T003", "Export to PDF",           "Pending");
        projects[projectCount++] = p3;

        // 2 Hardware projects
        HardwareProject p4 = new HardwareProject(
                "P004", "Gamma IoT Hub",
                "Industrial IoT sensor gateway device", 90000.00, 6, 8, "Embedded Systems");
        addSampleTask(p4, "T001", "PCB schematic design",  "Completed");
        addSampleTask(p4, "T002", "Firmware development",  "In Progress");
        addSampleTask(p4, "T003", "Field testing",         "Pending");
        addSampleTask(p4, "T004", "Certification process", "Pending");
        projects[projectCount++] = p4;

        HardwareProject p5 = new HardwareProject(
                "P005", "Epsilon Robot Arm",
                "6-axis robotic arm for assembly lines", 200000.00, 10, 12, "Mechatronics");
        addSampleTask(p5, "T001", "Mechanical design",     "Completed");
        addSampleTask(p5, "T002", "Motor driver board",    "Completed");
        addSampleTask(p5, "T003", "Motion control code",   "Completed");
        addSampleTask(p5, "T004", "Safety interlocks",     "In Progress");
        addSampleTask(p5, "T005", "User manual",           "Pending");
        projects[projectCount++] = p5;
    }

    private void addSampleTask(Project project, String taskId,
                               String taskName, String status) {
        project.addTask(new Task(taskId, taskName, status));
    }

    // ── CORE METHODS REQUIRED ─────────────────────────────────────────────────

    /**
     * createProject — Enforces unique project IDs and validates input.
     * @throws InvalidProjectDataException if validation fails.
     */
    public void createProject(Project project) {
        if (findById(project.getId()) != null) {
            throw new InvalidProjectDataException("Project ID '" + project.getId() + "' already exists.");
        }
        if (project.getBudget() <= 0) {
            throw new InvalidProjectDataException("Project budget must be positive.");
        }
        if (projectCount >= MAX_PROJECTS) {
            throw new InvalidProjectDataException("Project limit reached (Max: " + MAX_PROJECTS + ").");
        }
        projects[projectCount++] = project;
    }

    /**
     * listProjects — Returns an array of projects.
     */
    public Project[] listProjects() {
        return getAllProjects();
    }

    // ── CRUD operations ───────────────────────────────────────────────────────
    /**
     * Adds a new project.
     /** @deprecated Use createProject for exception-based flow. */
    @Deprecated
    public boolean addProject(Project project) {
        try {
            createProject(project);
            return true;
        } catch (InvalidProjectDataException e) {
            System.out.println("  [!] " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns all projects (only populated slots).
     */
    public Project[] getAllProjects() {
        Project[] result = new Project[projectCount];
        System.arraycopy(projects, 0, result, 0, projectCount);
        return result;
    }

    /**
     * Finds a project by ID (case-insensitive). Returns null if not found.
     */
    public Project findById(String id) {
        for (int i = 0; i < projectCount; i++) {
            if (projects[i].getId().equalsIgnoreCase(id)) {
                return projects[i];
            }
        }
        return null;
    }

    /**
     * Filters projects by type string ("Software" or "Hardware").
     */
    public Project[] filterByType(String type) {
        Project[] temp  = new Project[projectCount];
        int       count = 0;
        for (int i = 0; i < projectCount; i++) {
            boolean isSoftware = (projects[i] instanceof SoftwareProject)
                                 && type.equalsIgnoreCase("Software");
            boolean isHardware = (projects[i] instanceof HardwareProject)
                                 && type.equalsIgnoreCase("Hardware");
            if (isSoftware || isHardware) {
                temp[count++] = projects[i];
            }
        }
        Project[] result = new Project[count];
        System.arraycopy(temp, 0, result, 0, count);
        return result;
    }

    public int getProjectCount() { return projectCount; }
}
