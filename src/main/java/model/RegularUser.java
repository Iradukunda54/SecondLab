package model;

/**
 * RegularUser — Concrete subclass of User.
 * Demonstrates: Inheritance, Method overriding (getRoleDescription), Polymorphism.
 */
public class RegularUser extends User {

    // ── Constructor ───────────────────────────────────────────────────────────
    public RegularUser(String name, String email) {
        super(name, email, "Regular User");
    }

    // ── Polymorphic method ────────────────────────────────────────────────────
    @Override
    public String getRoleDescription() {
        return "Can view and update tasks. Cannot create or delete projects.";
    }
}
