package model;

/**
 * HardwareProject — Concrete subclass of Project.
 * Demonstrates: Inheritance, Method overriding, Polymorphism.
 */
public class HardwareProject extends Project {

    // ── Extra field ──────────────────────────────────────────────────────────
    private String hardwareType;

    // ── Constructors (overloaded) ─────────────────────────────────────────────
    public HardwareProject(String id, String name, String description,
                           double budget, int teamSize, int maxTasks, String hardwareType) {
        super(id, name, description, budget, teamSize, maxTasks);
        this.hardwareType = hardwareType;
    }

    /** Overloaded constructor with a default hardware type. */
    public HardwareProject(String id, String name, String description,
                           double budget, int teamSize, int maxTasks) {
        this(id, name, description, budget, teamSize, maxTasks, "Embedded Systems");
    }

    // ── Abstract method implementation ────────────────────────────────────────
    @Override
    public String getProjectDetails() {
        return "Hardware | Type: " + hardwareType;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────
    public String getHardwareType()                  { return hardwareType; }
    public void   setHardwareType(String hardwareType) { this.hardwareType = hardwareType; }
    public String getType()                          { return "Hardware"; }
}
