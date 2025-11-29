package com.humanitarian.model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String userType; // survivor, helper, admin
    private String location;
    private String languagePreference; // en, am
    private boolean isVerified;
    private boolean isBlocked;
    private Timestamp createdAt;
    private Timestamp lastLogin;

    public User() {}

    public User(String email, String password, String fullName, String userType) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.userType = userType;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getLanguagePreference() { return languagePreference; }
    public void setLanguagePreference(String languagePreference) { this.languagePreference = languagePreference; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getLastLogin() { return lastLogin; }
    public void setLastLogin(Timestamp lastLogin) { this.lastLogin = lastLogin; }
}

