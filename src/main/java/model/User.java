package model;

/**
 * User — Abstract base class for all user types.
 * Demonstrates: Abstract class, Encapsulation, Auto-generated IDs via static counter.
 */
public abstract class User {

    // ── Static ID counter (auto-increments across all User instances) ─────────
    private static int idCounter = 1;

    // ── Fields ───────────────────────────────────────────────────────────────
    private int    id;
    private String name;
    private String email;
    private String role;

    // ── Constructor ───────────────────────────────────────────────────────────
    public User(String name, String email, String role) {
        this.id    = idCounter++;
        this.name  = name;
        this.email = email;
        this.role  = role;
    }

    // ── Abstract method ───────────────────────────────────────────────────────
    public abstract String getRoleDescription();

    // ── Role Authorization Helpers ────────────────────────────────────────────
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(getRole());
    }

    public boolean canAddProject() {
        return isAdmin();
    }

    // ── Concrete display ──────────────────────────────────────────────────────
    public void displayUser() {
        System.out.printf("  %-5d %-20s %-30s %-15s%n", id, name, email, role);
        System.out.println("  Role Detail: " + getRoleDescription());
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public int    getId()    { return id; }
    public String getName()  { return name; }
    public String getEmail() { return email; }
    public String getRole()  { return role; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setName(String name)   { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "[" + id + "] " + name + " <" + email + "> (" + role + ")";
    }
}
