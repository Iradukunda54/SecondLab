package service;

import model.*;
import exception.InvalidProjectDataException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ProjectService — Manages the project catalog using a HashMap.
 * Demonstrates: HashMap for O(1) access, Streams API for processing.
 */
public class ProjectService {

    // ── Storage (HashMap for fast ID-based lookup) ────────────────────────────
    private Map<String, Project> projects;

    // ── Constructor — pre-loads empty projects ──────────────────────────────
    public ProjectService() {
        this.projects = new LinkedHashMap<>();
    }

    // ── CORE METHODS ──────────────────────────────────────────────────────────

    /**
     * createProject — Enforces unique project IDs and validates input.
     * @throws InvalidProjectDataException if validation fails.
     */
    public void createProject(Project project) {
        if (projects.containsKey(project.getId())) {
            throw new InvalidProjectDataException("Project ID '" + project.getId() + "' already exists.");
        }
        if (project.getBudget() <= 0) {
            throw new InvalidProjectDataException("Project budget must be positive.");
        }
        projects.put(project.getId(), project);
    }

    /**
     * listProjects — Returns an array of projects (for compatibility with existing UI).
     * Uses Streams to filter and collect.
     */
    public Project[] listProjects() {
        return projects.values().stream()
                .toArray(Project[]::new);
    }

    // ── CRUD operations ───────────────────────────────────────────────────────
    /**
     * Adds a new project.
     * @deprecated Use createProject for exception-based flow.
     */
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
     * Returns all projects as a List.
     */
    public List<Project> getAllProjectsList() {
        return new ArrayList<>(projects.values());
    }

    /**
     * Finds a project by ID. Returns null if not found.
     */
    public Project findById(String id) {
        if (id == null) return null;
        return projects.get(id);
    }

    /**
     * Filters projects by type using Streams.
     */
    public Project[] filterByType(String type) {
        return projects.values().stream()
                .filter(p -> {
                    if (type.equalsIgnoreCase("Software")) return p instanceof SoftwareProject;
                    if (type.equalsIgnoreCase("Hardware")) return p instanceof HardwareProject;
                    return false;
                })
                .toArray(Project[]::new);
    }

    /**
     * Find completed projects using Streams (Demonstration for US-1.2).
     */
    public List<Project> findHighCompletionProjects(double threshold) {
        return projects.values().stream()
                .filter(p -> {
                    try {
                        return p.calculateCompletionPercentage() >= threshold;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public int getProjectCount() { return projects.size(); }

    /** Replaces current projects (used by FileUtils loader). */
    public void setProjects(Map<String, Project> loaded) {
        this.projects = loaded;
    }
}
