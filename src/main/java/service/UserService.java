package service;

import model.AdminUser;
import model.RegularUser;
import model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * UserService — Handles user-related operations and data storage.
 * In a real app, this would interact with a database.
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

    /** Returns a copy of the users list. */
    public User[] getAllUsers() {
        return users.toArray(new User[0]);
    }

    /** Adds a new user to the system. */
    public void addUser(User user) {
        users.add(user);
    }

    /** Finds a user by their list index (1-based choice in UI). */
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
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
}
