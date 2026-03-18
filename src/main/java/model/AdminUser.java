package model;

/**
 * AdminUser — Concrete subclass of User.
 * Demonstrates: Inheritance, Method overriding, Extra admin-specific field.
 */
public class AdminUser extends User {

    // ── Additional admin field ────────────────────────────────────────────────
    private boolean canDeleteProject;

    // ── Constructor ───────────────────────────────────────────────────────────
    public AdminUser(String name, String email) {
        super(name, email, "Admin");
        this.canDeleteProject = true;
    }

    // ── Polymorphic method ────────────────────────────────────────────────────
    @Override
    public String getRoleDescription() {
        return "Full access: create, update, delete projects and tasks."
             + (canDeleteProject ? " [DELETE enabled]" : " [DELETE disabled]");
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────
    public boolean canDeleteProject()              { return canDeleteProject; }
    public void    setCanDeleteProject(boolean b)  { this.canDeleteProject = b; }
}
