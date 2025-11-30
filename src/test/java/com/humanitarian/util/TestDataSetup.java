package com.humanitarian.util;

import com.humanitarian.dao.UserDAO;
import com.humanitarian.model.User;

/**
 * Utility class for setting up test data
 */
public class TestDataSetup {
    
    /**
     * Creates a test user if it doesn't exist
     */
    public static User createTestUser(String email, String userType) {
        UserDAO userDAO = new UserDAO();
        
        if (userDAO.emailExists(email)) {
            // User exists, try to login and return
            User existing = userDAO.loginUser(email, "testpass123");
            if (existing != null) {
                return existing;
            }
        }
        
        // Create new user
        User user = new User();
        user.setEmail(email);
        user.setPassword("testpass123");
        user.setFullName("Test " + userType);
        user.setUserType(userType);
        user.setLocation("Test Location");
        user.setLanguagePreference("en");
        
        if (userDAO.registerUser(user)) {
            return userDAO.loginUser(email, "testpass123");
        }
        
        return null;
    }
    
    /**
     * Cleans up test data (optional - implement as needed)
     */
    public static void cleanupTestData() {
        // Implement cleanup logic if needed
        // Be careful not to delete production data!
    }
}

