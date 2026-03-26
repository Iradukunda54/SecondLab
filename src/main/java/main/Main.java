package main;

import model.*;
import service.*;
import exception.*;
import model.User; // Explicit for clarity if needed, but .* covers it

import java.util.Scanner;

public class Main {

    // ── Services ──────────────────────────────────────────────────────────────
    private static final ProjectService projectService = new ProjectService();
    private static final TaskService    taskService    = new TaskService(projectService);
    private static final ReportService  reportService  = new ReportService();
    private static final UserService    userService    = new UserService();

    // ── Scanner and Current User ─────────────────────────────────────────────
    private static final Scanner sc = new Scanner(System.in);
    private static User currentUser = userService.getUserByIndex(0); // Default to first (Alice)

    // ═════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        ConsoleMenu.printBanner();
        int choice;
        do {
            System.out.println("  [System] Logged in as: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
            ConsoleMenu.printMainMenu();
            String input = sc.nextLine().trim();
            System.out.println();

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                ConsoleMenu.error("Please enter a number between 0 and 9.");
                choice = -1;
                continue;
            }

            switch (choice) {
                case 1:  manageProjects(); break;
                case 2:  manageTasks(); break;
                case 3:  viewStatusReport(); break;
                case 4:  manageUsers(); break;
                case 0:  ConsoleMenu.info("Thank you for using Task Management System. Goodbye!"); break;
                default: ConsoleMenu.error("Invalid option. Please choose 0-4.");
            }
        } while (choice != 0);

        sc.close();
    }

    private static void manageProjects() {
        int choice;
        do {
            ConsoleMenu.printProjectMenu();
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                choice = -1;
            }

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
            } catch (NumberFormatException e) {
                choice = -1;
            }

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
            } catch (NumberFormatException e) {
                choice = -1;
            }

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

    // ═════════════════════════════════════════════════════════════════════════
    // FEATURE 1: PROJECT MANAGEMENT
    // ═════════════════════════════════════════════════════════════════════════

    /** Menu option 1 — Add a new project (Software or Hardware). */
    private static void addProject() {
        ConsoleMenu.printSectionHeader("ADD NEW PROJECT");

        if (!currentUser.canAddProject()) {
            ConsoleMenu.error("Access Denied: Only Admin users can add projects.");
            pauseForUser();
            return;
        }

        try {
            // Project type
            System.out.println("  Project type:");
            System.out.println("    1. Software Project");
            System.out.println("    2. Hardware Project");
            System.out.print("  Select type (1/2): ");
            int typeChoice = Integer.parseInt(sc.nextLine().trim());
            if (typeChoice != 1 && typeChoice != 2) {
                ConsoleMenu.error("Invalid type. Enter 1 or 2.");
                return;
            }

            // Project ID
            System.out.print("  Project ID (format P001): ");
            String id = sc.nextLine().trim().toUpperCase();
            if (!ValidationUtils.validateProjectId(id)) return;
            if (projectService.findById(id) != null) {
                ConsoleMenu.error("Project ID already exists: " + id);
                return;
            }

            // Name
            ConsoleMenu.prompt("Project Name");
            String name = sc.nextLine().trim();
            if (!ValidationUtils.validateNonEmpty(name, "Project Name")) return;

            // Description
            ConsoleMenu.prompt("Description");
            String desc = sc.nextLine().trim();
            if (!ValidationUtils.validateNonEmpty(desc, "Description")) return;

            // Team size
            ConsoleMenu.prompt("Team Size (number)");
            int teamSize = ValidationUtils.parseIntSafely(sc.nextLine(), "Team Size");
            if (teamSize == Integer.MIN_VALUE) return;
            if (!ValidationUtils.validateTeamSize(teamSize)) return;

            // Budget
            ConsoleMenu.prompt("Budget (e.g. 50000.00)");
            double budget = ValidationUtils.parseDoubleSafely(sc.nextLine(), "Budget");
            if (Double.isNaN(budget)) return;
            if (!ValidationUtils.validateBudget(budget)) return;

            // Max Tasks
            ConsoleMenu.prompt("Max Tasks");
            int maxTasks = Integer.parseInt(sc.nextLine().trim());
            if (maxTasks <= 0) {
                System.out.println("  [!] Max tasks must be positive.");
                return;
            }

            Project newProject;

            if (typeChoice == 1) {
                ConsoleMenu.prompt("Programming Language");
                String lang = sc.nextLine().trim();
                if (!ValidationUtils.validateNonEmpty(lang, "Programming Language")) return;
                newProject = new SoftwareProject(id, name, desc, budget, teamSize, maxTasks, lang);
            } else { // typeChoice == 2
                ConsoleMenu.prompt("Hardware Type");
                String hwType = sc.nextLine().trim();
                if (!ValidationUtils.validateNonEmpty(hwType, "Hardware Type")) return;
                newProject = new HardwareProject(id, name, desc, budget, teamSize, maxTasks, hwType);
            }

            projectService.createProject(newProject);
            ConsoleMenu.success("Project " + id + " (" + name + ") added successfully!");

        } catch (NumberFormatException e) {
            ConsoleMenu.error("Invalid numeric format. Please enter a valid number.");
        } catch (InvalidProjectDataException e) {
            ConsoleMenu.error("Error creating project: " + e.getMessage());
        }
        pauseForUser();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /** Menu option 2 — View all projects in a table. */
    private static void viewAllProjects() {
        ConsoleMenu.printSectionHeader("ALL PROJECTS");
        Project[] all = projectService.getAllProjects();
        if (all.length == 0) {
            ConsoleMenu.info("No projects found.");
            pauseForUser();
            return;
        }
        ConsoleMenu.printProjectListHeader();
        for (Project p : all) {
            printProjectRow(p);
        }
        System.out.println();
        ConsoleMenu.info("Total projects: " + all.length);
        pauseForUser();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /** Menu option 3 — Filter projects by Software or Hardware. */
    private static void viewProjectsByType() {
        ConsoleMenu.printSectionHeader("VIEW PROJECTS BY TYPE");
        System.out.println("  1. Software Projects");
        System.out.println("  2. Hardware Projects");
        System.out.print("  Select type (1/2): ");
        String typeChoice = sc.nextLine().trim();

        String typeName;
        if      (typeChoice.equals("1")) typeName = "Software";
        else if (typeChoice.equals("2")) typeName = "Hardware";
        else {
            ConsoleMenu.error("Invalid type. Enter 1 or 2.");
            return;
        }

        Project[] filtered = projectService.filterByType(typeName);
        System.out.println();
        if (filtered.length == 0) {
            ConsoleMenu.info("No " + typeName + " projects found.");
            pauseForUser();
            return;
        }
        System.out.println("  " + typeName.toUpperCase() + " PROJECTS:");
        ConsoleMenu.printProjectListHeader();
        for (Project p : filtered) {
            printProjectRow(p);
        }
        ConsoleMenu.info("Found " + filtered.length + " " + typeName + " project(s).");
        pauseForUser();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /** Menu option 4 — View detailed info for a single project. */
    private static void viewProjectDetails() {
        ConsoleMenu.printSectionHeader("PROJECT DETAILS");
        System.out.print("  Enter Project ID (e.g. P001): ");
        String id = sc.nextLine().trim().toUpperCase();
        if (!ValidationUtils.validateProjectId(id)) { pauseForUser(); return; }

        Project project = projectService.findById(id);
        if (project == null) {
            ConsoleMenu.error("Project not found: " + id);
            pauseForUser();
            return;
        }
        System.out.println();
        project.displayProject();
        pauseForUser();
    }

    // ═════════════════════════════════════════════════════════════════════════
    // FEATURE 2: TASK OPERATIONS
    // ═════════════════════════════════════════════════════════════════════════

    /** Menu option 5 — Add a task to a project. */
    private static void addTask() {
        ConsoleMenu.printSectionHeader("ADD TASK TO PROJECT");

        // Project
        System.out.print("  Enter Project ID (e.g. P001): ");
        String projectId = sc.nextLine().trim().toUpperCase();
        if (!ValidationUtils.validateProjectId(projectId)) { pauseForUser(); return; }
        if (projectService.findById(projectId) == null) {
            ConsoleMenu.error("Project not found: " + projectId);
            pauseForUser();
            return;
        }

        // Task ID
        System.out.print("  Enter Task ID (format T001): ");
        String taskId = sc.nextLine().trim().toUpperCase();
        if (!ValidationUtils.validateTaskId(taskId)) { pauseForUser(); return; }
        if (taskService.taskIdExists(projectId, taskId)) {
            ConsoleMenu.error("Task ID " + taskId + " already exists in project " + projectId);
            pauseForUser();
            return;
        }

        // Task name
        ConsoleMenu.prompt("Task Name");
        String taskName = sc.nextLine().trim();
        if (!ValidationUtils.validateNonEmpty(taskName, "Task Name")) { pauseForUser(); return; }

        // Status
        System.out.println("  Status options: Pending | In Progress | Completed");
        ConsoleMenu.prompt("Status");
        String status = sc.nextLine().trim();
        if (!ValidationUtils.validateStatus(status)) { pauseForUser(); return; }

        Task newTask = new Task(taskId, taskName, status);
        try {
            taskService.createTask(projectId, newTask);
            ConsoleMenu.success("Task " + taskId + " (" + taskName + ") added to project " + projectId);
        } catch (InvalidInputException | InvalidTaskDataException e) {
            ConsoleMenu.error("Error adding task: " + e.getMessage());
        }
        pauseForUser();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /** Menu option 6 — Update the status of an existing task. */
    private static void updateTaskStatus() {
        ConsoleMenu.printSectionHeader("UPDATE TASK STATUS");

        System.out.print("  Enter Project ID (e.g. P001): ");
        String projectId = sc.nextLine().trim().toUpperCase();
        if (!ValidationUtils.validateProjectId(projectId)) { pauseForUser(); return; }
        if (projectService.findById(projectId) == null) {
            ConsoleMenu.error("Project not found: " + projectId);
            pauseForUser();
            return;
        }

        // Show current tasks first
        Task[] tasks = taskService.getTasksForProject(projectId);
        if (tasks == null || tasks.length == 0) {
            ConsoleMenu.info("No tasks found in project " + projectId);
            pauseForUser();
            return;
        }
        ConsoleMenu.info("Current tasks in " + projectId + ":");
        ConsoleMenu.printTaskListHeader();
        for (Task t : tasks) t.displayTask();

        System.out.print("\n  Enter Task ID to update (e.g. T001): ");
        String taskId = sc.nextLine().trim().toUpperCase();
        if (!ValidationUtils.validateTaskId(taskId)) { pauseForUser(); return; }

        System.out.println("  New status options: Pending | In Progress | Completed");
        ConsoleMenu.prompt("New Status");
        String newStatus = sc.nextLine().trim();
        if (!ValidationUtils.validateStatus(newStatus)) { pauseForUser(); return; }

        try {
            taskService.updateTaskStatus(projectId, taskId, newStatus);
            ConsoleMenu.success("Task " + taskId + " updated to [" + newStatus + "]");
        } catch (TaskNotFoundException e) {
            ConsoleMenu.error("Error updating task: " + e.getMessage());
        }
        pauseForUser();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /** Menu option 7 — View all tasks within a project. */
    private static void viewTasksInProject() {
        ConsoleMenu.printSectionHeader("VIEW TASKS IN PROJECT");

        System.out.print("  Enter Project ID (e.g. P001): ");
        String projectId = sc.nextLine().trim().toUpperCase();
        if (!ValidationUtils.validateProjectId(projectId)) { pauseForUser(); return; }

        Project project = projectService.findById(projectId);
        if (project == null) {
            ConsoleMenu.error("Project not found: " + projectId);
            pauseForUser();
            return;
        }

        Task[] tasks = taskService.getTasksForProject(projectId);
        if (tasks == null || tasks.length == 0) {
            ConsoleMenu.info("No tasks found for project " + projectId);
            pauseForUser();
            return;
        }

        System.out.println("  Project: " + project.getName() + " (" + projectId + ")");
        System.out.println("  Total tasks: " + tasks.length);
        ConsoleMenu.printTaskListHeader();
        for (Task t : tasks) t.displayTask();

        // Quick inline completion %
        long completedCount = 0;
        for (Task t : tasks) { if (t.isCompleted()) completedCount++; }
        double pct = Math.round(((double) completedCount / tasks.length) * 100 * 100.0) / 100.0;
        System.out.printf("%n  Completion: %d / %d tasks (%.2f%%)%n",
                completedCount, tasks.length, pct);
        pauseForUser();
    }

    // ═════════════════════════════════════════════════════════════════════════
    // FEATURE 4: STATUS REPORT
    // ═════════════════════════════════════════════════════════════════════════

    /** Menu option 8 — Display the project status report table. */
    private static void viewStatusReport() {
        ConsoleMenu.printSectionHeader("PROJECT STATUS REPORT");
        Project[]      all     = projectService.getAllProjects();
        StatusReport[] reports = reportService.generateReport(all);
        reportService.displayReport(reports);
        pauseForUser();
    }

    // ═════════════════════════════════════════════════════════════════════════
    // FEATURE 3: USER MANAGEMENT
    // ═════════════════════════════════════════════════════════════════════════

    /** Menu option 9 — View all registered users. */
    private static void viewUsers() {
        ConsoleMenu.printUserHeader();
        User[] allUsers = userService.getAllUsers();
        // Polymorphism: User variable iterates AdminUser and RegularUser objects
        for (User u : allUsers) {
            System.out.printf("  %-5d  %-20s  %-30s  %-15s%n",
                    u.getId(), u.getName(), u.getEmail(), u.getRole());
            System.out.println("         ↳ " + u.getRoleDescription());
        }
        System.out.println();
        ConsoleMenu.info("Total users: " + allUsers.length);
        pauseForUser();
    }

    // ═════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═════════════════════════════════════════════════════════════════════════

    private static void switchUser() {
        ConsoleMenu.printSectionHeader("SWITCH USER");
        User[] allUsers = userService.getAllUsers();
        for (int i = 0; i < allUsers.length; i++) {
            System.out.println("  " + (i + 1) + ". " + allUsers[i].getName() + " (" + allUsers[i].getRole() + ")");
        }
        System.out.print("  Select user (1-" + allUsers.length + "): ");
        try {
            int choice = Integer.parseInt(sc.nextLine().trim());
            if (choice >= 1 && choice <= allUsers.length) {
                currentUser = allUsers[choice - 1];
                ConsoleMenu.success("User switched to: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
            } else {
                ConsoleMenu.error("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            ConsoleMenu.error("Please enter a number.");
        }
        pauseForUser();
    }

    /** Prints a single project row in the list table. */
    private static void printProjectRow(Project p) {
        String type = (p instanceof SoftwareProject) ? "Software" : "Hardware";
        System.out.printf("  %-6s  %-22s  %-12s  %-6d  %d tasks%n",
                p.getId(),
                truncate(p.getName(), 22),
                type,
                p.getTeamSize(),
                p.getTaskCount());
    }

    /** Menu option (Admin only) — Register a new user. */
    private static void registerUser() {
        ConsoleMenu.printSectionHeader("REGISTER NEW USER");

        if (!currentUser.isAdmin()) {
            ConsoleMenu.error("Access Denied: Only Admin users can register new users.");
            pauseForUser();
            return;
        }

        try {
            // Role type
            System.out.println("  Select user role:");
            System.out.println("    1. Regular User");
            System.out.println("    2. Admin User");
            System.out.print("  Select role (1/2): ");
            int roleChoice = Integer.parseInt(sc.nextLine().trim());
            if (roleChoice != 1 && roleChoice != 2) {
                ConsoleMenu.error("Invalid choice. Enter 1 or 2.");
                return;
            }

            // Name
            ConsoleMenu.prompt("User's Full Name");
            String name = sc.nextLine().trim();
            if (!ValidationUtils.validateNonEmpty(name, "User Name")) return;

            // Email
            ConsoleMenu.prompt("User's Email");
            String email = sc.nextLine().trim();
            if (!ValidationUtils.validateEmail(email)) return;
            if (userService.emailExists(email)) {
                ConsoleMenu.error("Email already exists: " + email);
                return;
            }

            User newUser;
            if (roleChoice == 1) {
                newUser = new RegularUser(name, email);
            } else {
                newUser = new AdminUser(name, email);
            }

            userService.addUser(newUser);
            ConsoleMenu.success("User " + name + " (" + newUser.getRole() + ") registered successfully!");

        } catch (NumberFormatException e) {
            ConsoleMenu.error("Invalid input. Selection must be a number.");
        }
        pauseForUser();
    }

    /** Waits for the user to press ENTER before returning to the menu. */
    private static void pauseForUser() {
        ConsoleMenu.pressEnterToContinue();
        sc.nextLine();
        System.out.println();
    }

    /** Truncates a string to maxLen, appending "…" if needed. */
    private static String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 1) + "…";
    }
}
