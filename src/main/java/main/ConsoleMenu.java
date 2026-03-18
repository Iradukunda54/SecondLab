package main;

/**
 * ConsoleMenu — Prints the banner and numbered menu options.
 * Demonstrates: Separation of UI concerns, Reusable display methods.
 */
public class ConsoleMenu {

    // ── Banner ────────────────────────────────────────────────────────────────
    public static void printBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("  ║                   TASK MANAGEMENT SYSTEM                         ║");
        System.out.println("  ║                                                                  ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    // ── Main menu ─────────────────────────────────────────────────────────────
    public static void printMainMenu() {
        System.out.println("  ┌─────────────────────────────────────────────────┐");
        System.out.println("  │                    MAIN MENU                    │");
        System.out.println("  ├─────────────────────────────────────────────────┤");
        System.out.println("  │  1. Manage Projects                             │");
        System.out.println("  │  2. Manage Tasks                                │");
        System.out.println("  │  3. View Status Reports                         │");
        System.out.println("  │  4. Manage Users                                │");
        System.out.println("  │  0. Exit                                        │");
        System.out.println("  └─────────────────────────────────────────────────┘");
        System.out.print("  Enter choice: ");
    }

    public static void printProjectMenu() {
        System.out.println("  ┌─────────────────────────────────────────────────┐");
        System.out.println("  │               PROJECT MANAGEMENT                │");
        System.out.println("  ├─────────────────────────────────────────────────┤");
        System.out.println("  │  1. Add Project                                 │");
        System.out.println("  │  2. View All Projects                           │");
        System.out.println("  │  3. View Projects by Type                       │");
        System.out.println("  │  4. View Project Details                        │");
        System.out.println("  │  0. Back to Main Menu                           │");
        System.out.println("  └─────────────────────────────────────────────────┘");
        System.out.print("  Enter choice: ");
    }

    public static void printTaskMenu() {
        System.out.println("  ┌─────────────────────────────────────────────────┐");
        System.out.println("  │                 TASK MANAGEMENT                 │");
        System.out.println("  ├─────────────────────────────────────────────────┤");
        System.out.println("  │  1. Add Task to Project                         │");
        System.out.println("  │  2. Update Task Status                          │");
        System.out.println("  │  3. View Tasks in Project                       │");
        System.out.println("  │  0. Back to Main Menu                           │");
        System.out.println("  └─────────────────────────────────────────────────┘");
        System.out.print("  Enter choice: ");
    }

    public static void printUserMenu() {
        System.out.println("  ┌─────────────────────────────────────────────────┐");
        System.out.println("  │                 USER MANAGEMENT                 │");
        System.out.println("  ├─────────────────────────────────────────────────┤");
        System.out.println("  │  1. View Registered Users                       │");
        System.out.println("  │  2. Switch Current User                         │");
        System.out.println("  │  0. Back to Main Menu                           │");
        System.out.println("  └─────────────────────────────────────────────────┘");
        System.out.print("  Enter choice: ");
    }

    // ── Section headers ───────────────────────────────────────────────────────
    public static void printSectionHeader(String title) {
        System.out.println();
        System.out.println("  ══════════════════════════════════════════════════");
        System.out.println("  " + title);
        System.out.println("  ══════════════════════════════════════════════════");
    }

    // ── Project list table header ─────────────────────────────────────────────
    public static void printProjectListHeader() {
        System.out.println();
        System.out.printf("  %-6s  %-22s  %-12s  %-10s  %s%n",
                "ID", "NAME", "TYPE", "TEAM", "TASKS");
        System.out.println("  " + repeat("-", 45));
    }

    // ── Task list table header ─────────────────────────────────────────────────
    public static void printTaskListHeader() {
        System.out.println();
        System.out.printf("  %-10s %-30s %-15s%n",
                "TASK ID", "TASK NAME", "STATUS");
        System.out.println("  " + repeat("═", 50));
    }

    // ── Divider ───────────────────────────────────────────────────────────────
    public static void printDivider() {
        System.out.println("  " + repeat("·", 40));
    }

    // ── User list table header ────────────────────────────────────────────────
    public static void printUserListHeader() {
        System.out.println();
        System.out.printf("  %-5s  %-20s  %-30s  %-15s%n",
                "ID", "NAME", "EMAIL", "ROLE");
        System.out.println("  " + repeat("-", 72));
    }

    // ── Generic prompt ────────────────────────────────────────────────────────
    public static void prompt(String label) {
        System.out.print("  Enter " + label + ": ");
    }

    public static void success(String msg) {
        System.out.println("  [✓] " + msg);
    }

    public static void error(String msg) {
        System.out.println("  [✗] " + msg);
    }

    public static void info(String msg) {
        System.out.println("  [i] " + msg);
    }

    public static void pressEnterToContinue() {
        System.out.println();
        System.out.print("  Press ENTER to return to menu...");
    }

    private static String repeat(String s, int n) {
        if (n <= 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }
}
