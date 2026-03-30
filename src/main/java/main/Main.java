package main;

import model.*;
import service.*;
import exception.*;
import utils.FileUtils;
import service.ValidationUtils;
import java.util.*;

/**
 * Main — Entry point for the Task Management System.
 * Orchestrates menu flows, persistence, and simulation.
 */
public class Main {

    // ── Services ──────────────────────────────────────────────────────────────
    private static final ProjectService     projectService     = new ProjectService();
    private static final TaskService        taskService        = new TaskService(projectService);
    private static final ReportService      reportService      = new ReportService();
    private static final UserService        userService        = new UserService();
    private static final ConcurrencyService concurrencyService = new ConcurrencyService();
    private static final StreamService      streamService      = new StreamService();

    // ── Scanner and Current User ─────────────────────────────────────────────
    private static final Scanner sc = new Scanner(System.in);
    private static User currentUser = userService.getUserByIndex(0); // Default to first

    public static void main(String[] args) {
        System.out.println("\n  JAVA PROJECT MANAGEMENT SYSTEM v3.0\n");
        System.out.println("  Loading projects from file...");
        loadData();
        int choice;
        do {
            System.out.println("  [System] Logged in as: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
            ConsoleMenu.printMainMenu();
            String input = sc.nextLine().trim();
            System.out.println();

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                ConsoleMenu.error("Please enter a valid choice.");
                choice = -1;
                continue;
            }

            switch (choice) {
                case 1:  manageProjects(); break;
                case 2:  manageTasks(); break;
                case 3:  handleAdvancedFeatures(); break;
                case 4:  manageUsers(); break;
                case 0:  ConsoleMenu.info("Saving data..."); break;
                default: ConsoleMenu.error("Invalid option. Please choose 0-4.");
            }
        } while (choice != 0);

        saveData();
        sc.close();
    }

    private static void handleAdvancedFeatures() {
        int choice;
        do {
            ConsoleMenu.printAdvancedMenu();
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) { choice = -1; }

            switch (choice) {
                case 1: viewStatusReport(); break;
                case 2: streamsDemo(); break;
                case 3: simulateConcurrency(); break;
                case 0: return;
                default: ConsoleMenu.error("Invalid choice 0-3.");
            }
            if (choice != 0) pauseForUser();
        } while (choice != 0);
    }

    private static void manageProjects() {
        int choice;
        do {
            ConsoleMenu.printProjectMenu();
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) { choice = -1; }

            switch (choice) {
                case 1: addProject(); break;
                case 2: viewAllProjects(); break;
                case 3: viewProjectsByType(); break;
                case 4: viewProjectDetails(); break;
                case 0: return;
                default: ConsoleMenu.error("Invalid choice 0-4.");
            }
            if (choice != 0) pauseForUser();
        } while (choice != 0);
    }

    private static void manageTasks() {
        int choice;
        do {
            ConsoleMenu.printTaskMenu();
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) { choice = -1; }

            switch (choice) {
                case 1: addTask(); break;
                case 2: updateTaskStatus(); break;
                case 3: viewTasksInProject(); break;
                case 0: return;
                default: ConsoleMenu.error("Invalid choice 0-3.");
            }
            if (choice != 0) pauseForUser();
        } while (choice != 0);
    }

    private static void manageUsers() {
        int choice;
        do {
            ConsoleMenu.printUserMenu();
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) { choice = -1; }

            switch (choice) {
                case 1: viewUsers(); break;
                case 2: switchUser(); break;
                case 3: registerUser(); break;
                case 0: return;
                default: ConsoleMenu.error("Invalid choice 0-3.");
            }
            if (choice != 0) pauseForUser();
        } while (choice != 0);
    }

    // ─────────────────────────────────────────────────────────────────────────

    private static void addProject() {
        ConsoleMenu.printSectionHeader("ADD NEW PROJECT");
        try {
            System.out.println("  1. Software Project");
            System.out.println("  2. Hardware Project");
            System.out.print("  Select type (1/2): ");
            int typeChoice = Integer.parseInt(sc.nextLine().trim());

            ConsoleMenu.prompt("Project ID (e.g. P001)");
            String id = sc.nextLine().trim().toUpperCase();
            if (!ValidationUtils.validateProjectId(id)) return;

            ConsoleMenu.prompt("Project Name");
            String name = sc.nextLine().trim();
            if (!ValidationUtils.validateNonEmpty(name, "Project Name")) return;

            ConsoleMenu.prompt("Description");
            String desc = sc.nextLine().trim();
            if (!ValidationUtils.validateNonEmpty(desc, "Description")) return;

            ConsoleMenu.prompt("Team Size");
            int teamSize = Integer.parseInt(sc.nextLine().trim());

            ConsoleMenu.prompt("Budget");
            double budget = Double.parseDouble(sc.nextLine().trim());

            Project newProject;
            if (typeChoice == 1) {
                ConsoleMenu.prompt("Programming Language");
                String lang = sc.nextLine().trim();
                newProject = new SoftwareProject(id, name, desc, budget, teamSize, lang);
            } else {
                ConsoleMenu.prompt("Hardware Type");
                String hwType = sc.nextLine().trim();
                newProject = new HardwareProject(id, name, desc, budget, teamSize, hwType);
            }

            projectService.createProject(newProject);
            ConsoleMenu.success("Project " + id + " added successfully.");
        } catch (Exception e) {
            ConsoleMenu.error("Error creating project: " + e.getMessage());
        }
    }

    private static void viewAllProjects() {
        ConsoleMenu.printSectionHeader("ALL PROJECTS");
        Project[] all = projectService.listProjects();
        if (all.length == 0) {
            ConsoleMenu.info("No projects found.");
            return;
        }
        ConsoleMenu.printProjectListHeader();
        for (Project p : all) printProjectRow(p);
        System.out.println("\n  Total projects: " + all.length);
    }

    private static void printProjectRow(Project p) {
        System.out.printf("  %-6s  %-22s  %-12s  %-10d  %d tasks%n",
                p.getId(), truncate(p.getName(), 20), p.getType(), p.getTeamSize(), p.getTaskCount());
    }

    private static void viewProjectsByType() {
        ConsoleMenu.printSectionHeader("VIEW BY TYPE");
        System.out.print("  Type (Software/Hardware): ");
        String type = sc.nextLine().trim();
        Project[] filtered = projectService.filterByType(type);
        if (filtered.length == 0) {
            ConsoleMenu.info("No projects of type " + type + " found.");
            return;
        }
        ConsoleMenu.printProjectListHeader();
        for (Project p : filtered) printProjectRow(p);
    }

    private static void viewProjectDetails() {
        System.out.print("  Enter Project ID: ");
        String id = sc.nextLine().trim().toUpperCase();
        Project p = projectService.findById(id);
        if (p == null) ConsoleMenu.error("Project not found.");
        else p.displayProject();
    }

    private static void addTask() {
        ConsoleMenu.printSectionHeader("ADD TASK");
        System.out.print("  Project ID: ");
        String pid = sc.nextLine().trim().toUpperCase();
        if (projectService.findById(pid) == null) {
            ConsoleMenu.error("Project not found.");
            return;
        }
        ConsoleMenu.prompt("Task ID (e.g. T001)");
        String tid = sc.nextLine().trim().toUpperCase();
        if (!ValidationUtils.validateTaskId(tid)) return;

        ConsoleMenu.prompt("Task Name");
        String name = sc.nextLine().trim();
        if (!ValidationUtils.validateNonEmpty(name, "Task Name")) return;

        ConsoleMenu.prompt("Status");
        String status = sc.nextLine().trim();
        if (!ValidationUtils.validateStatus(status)) return;
        
        try {
            taskService.createTask(pid, new Task(tid, name, status));
            ConsoleMenu.success("Task added.");
        } catch (Exception e) {
            ConsoleMenu.error("Error: " + e.getMessage());
        }
    }

    private static void updateTaskStatus() {
        System.out.print("  Project ID: ");
        String pid = sc.nextLine().trim().toUpperCase();
        System.out.print("  Task ID: ");
        String tid = sc.nextLine().trim().toUpperCase();
        System.out.print("  New Status: ");
        String status = sc.nextLine().trim();
        try {
            taskService.updateTaskStatus(pid, tid, status);
            ConsoleMenu.success("Status updated.");
        } catch (Exception e) {
            ConsoleMenu.error("Error: " + e.getMessage());
        }
    }

    private static void viewTasksInProject() {
        System.out.print("  Project ID: ");
        String pid = sc.nextLine().trim().toUpperCase();
        List<Task> tasks = taskService.getTasksForProject(pid);
        if (tasks == null || tasks.isEmpty()) {
            ConsoleMenu.info("No tasks found.");
            return;
        }
        ConsoleMenu.printTaskListHeader();
        tasks.forEach(Task::displayTask);
    }

    private static void viewStatusReport() {
        ConsoleMenu.printSectionHeader("STATUS REPORT");
        reportService.displayReport(reportService.generateReport(projectService.getAllProjectsList()));
    }

    private static void simulateConcurrency() {
        ConsoleMenu.printSectionHeader("PARALLEL TASK UPDATE SIMULATION");
        List<Project> projects = projectService.getAllProjectsList();
        if (projects.isEmpty()) {
            ConsoleMenu.error("No projects to simulate.");
            return;
        }
        concurrencyService.runConcurrentSimulation(projects);
        System.out.println();
        concurrencyService.runParallelStreamDemo(projects);
    }

    private static void streamsDemo() {
        ConsoleMenu.printSectionHeader("STREAMS & FUNCTIONAL PROGRAMMING DEMO");
        streamService.printStreamSummary(projectService.getAllProjectsList());
    }

    private static void viewUsers() {
        ConsoleMenu.printUserHeader();
        for (User u : userService.getAllUsers()) {
            System.out.printf("  %-5d  %-20s  %-30s  %-15s%n",
                    u.getId(), u.getName(), u.getEmail(), u.getRole());
        }
    }

    private static void switchUser() {
        ConsoleMenu.printSectionHeader("SWITCH USER");
        User[] users = userService.getAllUsers();
        for (int i = 0; i < users.length; i++) {
            System.out.println("  " + i + ". " + users[i].getName() + " (" + users[i].getRole() + ")");
        }
        System.out.print("  Select user index: ");
        try {
            int idx = Integer.parseInt(sc.nextLine().trim());
            currentUser = userService.getUserByIndex(idx);
            ConsoleMenu.success("Switched to " + currentUser.getName());
        } catch (Exception e) {
            ConsoleMenu.error("Invalid index.");
        }
    }

    private static void registerUser() {
        if (!currentUser.isAdmin()) {
            ConsoleMenu.error("Permission denied. Admin only.");
            return;
        }
        ConsoleMenu.printSectionHeader("REGISTER NEW USER");
        ConsoleMenu.prompt("Name");
        String name = sc.nextLine().trim();
        ConsoleMenu.prompt("Email");
        String email = sc.nextLine().trim();
        
        System.out.println("  1. Regular User");
        System.out.println("  2. Admin User");
        System.out.print("  Role (1/2): ");
        String role = sc.nextLine().trim();
        
        try {
            userService.registerUser(name, email, role.equals("2"));
            ConsoleMenu.success("User registered.");
        } catch (Exception e) {
            ConsoleMenu.error("Error: " + e.getMessage());
        }
    }

    private static void pauseForUser() {
        System.out.print("\n  Press Enter to continue...");
        sc.nextLine();
    }

    private static void loadData() {
        FileUtils.loadIntoService(projectService);
    }

    private static void saveData() {
        FileUtils.saveFromService(projectService);
    }

    private static String truncate(String s, int max) {
        if (s.length() <= max) return s;
        return s.substring(0, max - 1) + "…";
    }
}
