package com.example.myapplication.ModelClass;

public class User {
    private String id;  // This will store the unique Firebase key for each user
    private String username;
    private String email;
    private String role;

    // Default constructor (needed for Firebase)
    public User() {}

    // Constructor with fields
    public User(String id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // Getter and setter for 'id' (Firebase ID)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and setter for 'username'
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and setter for 'email'
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter for 'role'
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
