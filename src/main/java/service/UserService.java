package service;

import model.AdminUser;
import model.RegularUser;
import model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * UserService — Handles user-related operations and data storage.
 */
public class UserService {

    private final List<User> users;

    public UserService() {
        this.users = new ArrayList<>();
        initializeSampleUsers();
    }

    private void initializeSampleUsers() {
        users.add(new AdminUser("Alice Smith",   "alice@gmail.com"));
        users.add(new RegularUser("Bob Jones",   "bob@gmail.com"));
        users.add(new RegularUser("Carol White", "carol@gmail.com"));
        users.add(new AdminUser("Dave Brown",    "dave@gmail.com"));
    }

    /** Returns all users as an array. */
    public User[] getAllUsers() {
        return users.toArray(new User[0]);
    }

    /** Adds a new user to the system. */
    public void addUser(User user) {
        users.add(user);
    }

    /** Returns user by zero-based index, or null. */
    public User getUserByIndex(int index) {
        if (index >= 0 && index < users.size()) {
            return users.get(index);
        }
        return null;
    }

    /** Returns the total number of registered users. */
    public int getUserCount() {
        return users.size();
    }

    /** Checks if an email is already registered. */
    public boolean emailExists(String email) {
        return users.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Registers a new user and adds them to the system.
     * @param name    full name
     * @param email   must end with @gmail.com
     * @param isAdmin true = AdminUser, false = RegularUser
     */
    public void registerUser(String name, String email, boolean isAdmin) {
        User newUser = isAdmin ? new AdminUser(name, email) : new RegularUser(name, email);
        users.add(newUser);
        System.out.printf("  [✓] User '%s' registered as %s.%n",
                name, isAdmin ? "Admin" : "Regular User");
    }
}
