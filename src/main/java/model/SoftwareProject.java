package model;

/**
 * SoftwareProject — Concrete subclass of Project.
 * Demonstrates: Inheritance, Method overriding, Polymorphism.
 */
public class SoftwareProject extends Project {

    // ── Extra field ──────────────────────────────────────────────────────────
    private String programmingLanguage;

    // ── Constructors (overloaded) ─────────────────────────────────────────────
    public SoftwareProject(String id, String name, String description,
                           double budget, int teamSize, String programmingLanguage) {
        super(id, name, description, budget, teamSize);
        this.programmingLanguage = programmingLanguage;
    }

    /** Overloaded constructor with a default language. */
    public SoftwareProject(String id, String name, String description,
                           double budget, int teamSize) {
        this(id, name, description, budget, teamSize, "Java");
    }

    // ── Abstract method implementation ────────────────────────────────────────
    @Override
    public String getProjectDetails() {
        return "Software | Lang: " + programmingLanguage;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────
    public String getProgrammingLanguage()               { return programmingLanguage; }
    public void   setProgrammingLanguage(String lang)    { this.programmingLanguage = lang; }
    public String getType()                              { return "Software"; }
}
