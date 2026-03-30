package utils;

import model.*;
import service.ProjectService;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * FileUtils — Handles file persistence using Java NIO.
 * Saves/Loads all projects and tasks to data/projects_data.json.
 * Demonstrates: Files.writeString(), Files.readString(), try-with-resources, NIO (Week 3).
 */
public class FileUtils {

    private static final Path DATA_DIR  = Paths.get("data");
    private static Path DATA_PATH = DATA_DIR.resolve("projects_data.json");

    /**
     * Overrides the default data path (used for testing).
     */
    public static void setDataPath(Path path) {
        DATA_PATH = path;
    }

    // ── SAVE ──────────────────────────────────────────────────────────────────

    /**
     * Saves all projects to data/projects_data.json in JSON-like format.
     * Uses try-with-resources and NIO Files API.
     */
    public static void saveProjects(Collection<Project> projects) throws IOException {
        // Ensure data/ directory exists
        Files.createDirectories(DATA_DIR);

        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        Iterator<Project> it = projects.iterator();
        while (it.hasNext()) {
            Project p = it.next();
            sb.append("  {\n");
            sb.append("    \"projectId\": \"").append(p.getId()).append("\",\n");
            sb.append("    \"name\": \"").append(escape(p.getName())).append("\",\n");
            sb.append("    \"type\": \"").append(p.getType()).append("\",\n");
            sb.append("    \"description\": \"").append(escape(p.getDescription())).append("\",\n");
            sb.append("    \"budget\": ").append(p.getBudget()).append(",\n");
            sb.append("    \"teamSize\": ").append(p.getTeamSize()).append(",\n");

            if (p instanceof SoftwareProject sp) {
                sb.append("    \"language\": \"").append(escape(sp.getProgrammingLanguage())).append("\",\n");
            } else if (p instanceof HardwareProject hp) {
                sb.append("    \"hardwareType\": \"").append(escape(hp.getHardwareType())).append("\",\n");
            }

            sb.append("    \"tasks\": [\n");
            List<Task> tasks = p.getTaskList();
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                sb.append("      {")
                  .append("\"id\": \"").append(t.getId()).append("\", ")
                  .append("\"name\": \"").append(escape(t.getName())).append("\", ")
                  .append("\"status\": \"").append(t.getStatus()).append("\"")
                  .append("}");
                if (i < tasks.size() - 1) sb.append(",");
                sb.append("\n");
            }
            sb.append("    ]\n");
            sb.append("  }");
            if (it.hasNext()) sb.append(",");
            sb.append("\n");
        }
        sb.append("]\n");

        Files.writeString(DATA_PATH, sb.toString(),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // ── LOAD ──────────────────────────────────────────────────────────────────

    /**
     * Loads projects from data/projects_data.json.
     * Returns empty map if file doesn't exist (graceful startup).
     */
    public static Map<String, Project> loadProjects() throws IOException {
        if (!Files.exists(DATA_PATH)) {
            return new HashMap<>();
        }

        String content = Files.readString(DATA_PATH);
        return parseJson(content);
    }

    /**
     * Applies loaded projects into the ProjectService.
     * Prints startup message per spec.
     */
    public static void loadIntoService(ProjectService service) {
        try {
            if (!Files.exists(DATA_PATH)) {
                System.out.println("  Error: Unable to load projects_data.json (File not found)");
                System.out.println("  Starting with empty catalog.");
                return;
            }
            Map<String, Project> loaded = loadProjects();
            if (loaded.isEmpty()) {
                System.out.println("  Starting with empty catalog.");
            } else {
                service.setProjects(loaded);
                System.out.printf("  [✓] %d projects loaded successfully from projects_data.json%n",
                        loaded.size());
            }
        } catch (IOException e) {
            System.out.println("  Error: Unable to load projects_data.json (" + e.getMessage() + ")");
            System.out.println("  Starting with empty catalog.");
        }
    }

    /**
     * Saves all projects from the service. Prints shutdown message per spec.
     */
    public static void saveFromService(ProjectService service) {
        try {
            System.out.println("\n  Saving project data...");
            saveProjects(service.getAllProjectsList());
            System.out.println("  [✓] Data written to projects_data.json successfully!");
        } catch (IOException e) {
            System.out.println("  [!] Failed to save data: " + e.getMessage());
        }
    }

    // ── SIMPLE JSON PARSER ────────────────────────────────────────────────────

    /**
     * Parses the JSON-like format written by saveProjects().
     * This is a field-by-field parser (no external libraries required).
     */
    private static Map<String, Project> parseJson(String content) {
        Map<String, Project> result = new LinkedHashMap<>();

        String[] blocks = content.split("\\{");
        // Each block (after first which is the outer array '[') represents a project or task
        String currentId = "", currentName = "", currentType = "",
               currentDesc = "", currentExtra = "";
        double currentBudget = 0;
        int    currentTeam   = 0;
        Project currentProject = null;
        boolean inTasksArray = false;

        for (String block : blocks) {
            if (block.isBlank() || block.equals("[\n")) continue;

            // Detect if we're in a task block (has "id" and "status" but no "projectId")
            boolean hasProjectId = block.contains("\"projectId\"");
            boolean hasTaskId    = block.contains("\"id\"") && !hasProjectId;

            if (hasProjectId) {
                // This is a project block
                currentId    = extractValue(block, "projectId");
                currentName  = extractValue(block, "name");
                currentType  = extractValue(block, "type");
                currentDesc  = extractValue(block, "description");
                currentExtra = extractValue(block, "language");
                if (currentExtra.isEmpty()) currentExtra = extractValue(block, "hardwareType");
                String budgetStr = extractValue(block, "budget");
                String teamStr   = extractValue(block, "teamSize");
                currentBudget = budgetStr.isEmpty() ? 0 : Double.parseDouble(budgetStr);
                currentTeam   = teamStr.isEmpty() ? 0 : Integer.parseInt(teamStr);

                // Build project
                if (currentType.equalsIgnoreCase("Software")) {
                    currentProject = new SoftwareProject(
                            currentId, currentName, currentDesc,
                            currentBudget, currentTeam, currentExtra);
                } else {
                    currentProject = new HardwareProject(
                            currentId, currentName, currentDesc,
                            currentBudget, currentTeam, currentExtra);
                }
                result.put(currentId, currentProject);

            } else if (hasTaskId && currentProject != null) {
                // This is a task block
                String tId     = extractValue(block, "id");
                String tName   = extractValue(block, "name");
                String tStatus = extractValue(block, "status");
                if (!tId.isEmpty()) {
                    currentProject.addTask(new Task(tId, tName, tStatus));
                }
            }
        }
        return result;
    }

    /**
     * Extracts a JSON string or numeric value for a given key.
     * Handles: "key": "value" and "key": 123
     */
    private static String extractValue(String block, String key) {
        String search = "\"" + key + "\": ";
        int idx = block.indexOf(search);
        if (idx < 0) return "";
        int start = idx + search.length();
        char first = block.charAt(start);
        if (first == '"') {
            // String value
            int end = block.indexOf('"', start + 1);
            return end < 0 ? "" : block.substring(start + 1, end);
        } else {
            // Numeric value — read until comma, newline, or ]
            int end = start;
            while (end < block.length()) {
                char c = block.charAt(end);
                if (c == ',' || c == '\n' || c == '}' || c == ']') break;
                end++;
            }
            return block.substring(start, end).trim();
        }
    }

    /** Escapes double-quotes in strings for safe JSON embedding. */
    private static String escape(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
}
