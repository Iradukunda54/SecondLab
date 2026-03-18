package model;

import interfaces.Completable;

/**
 * Task class — represents a single unit of work within a project.
 * Implements: Completable interface.
 * Demonstrates: Encapsulation, Interface implementation.
 */
public class Task implements Completable {

    // ── Fields ───────────────────────────────────────────────────────────────
    private String id;
    private String name;
    private String status; // "Pending" | "In Progress" | "Completed"

    // ── Valid status constants ────────────────────────────────────────────────
    public static final String STATUS_PENDING     = "Pending";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_COMPLETED   = "Completed";

    // ── Constructor ───────────────────────────────────────────────────────────
    public Task(String id, String name, String status) {
        this.id     = id;
        this.name   = name;
        this.status = status;
    }

    // ── Completable interface ─────────────────────────────────────────────────
    @Override
    public boolean isCompleted() {
        return STATUS_COMPLETED.equalsIgnoreCase(this.status);
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getId()     { return id; }
    public String getName()   { return name; }
    public String getStatus() { return status; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setStatus(String status) { this.status = status; }

    // ── Display ───────────────────────────────────────────────────────────────
    public void displayTask() {
        System.out.printf("  %-10s %-30s %-15s%n", id, name, status);
    }

    @Override
    public String toString() {
        return "Task[" + id + " | " + name + " | " + status + "]";
    }
}
