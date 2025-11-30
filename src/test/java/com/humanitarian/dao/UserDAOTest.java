package com.humanitarian.dao;

import com.humanitarian.model.User;
import com.humanitarian.util.PasswordUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for UserDAO
 */
public class UserDAOTest {
    private UserDAO userDAO;
    private User testUser;

    @Before
    public void setUp() {
        userDAO = new UserDAO();
        // Create a test user
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("testpassword123");
        testUser.setFullName("Test User");
        testUser.setPhone("1234567890");
        testUser.setUserType("survivor");
        testUser.setLocation("Test Location");
        testUser.setLanguagePreference("en");
    }

    @After
    public void tearDown() {
        // Cleanup test data if needed
    }

    @Test
    public void testRegisterUser() {
        // Ensure email doesn't exist first
        if (userDAO.emailExists(testUser.getEmail())) {
            // Skip test if user already exists
            System.out.println("Test user already exists, skipping registration test");
            return;
        }
        
        boolean result = userDAO.registerUser(testUser);
        assertTrue("User registration should succeed", result);
        
        // Verify user was created
        assertTrue("User email should exist after registration", 
                   userDAO.emailExists(testUser.getEmail()));
    }

    @Test
    public void testEmailExists() {
        // Test with non-existent email
        assertFalse("Non-existent email should return false", 
                   userDAO.emailExists("nonexistent@example.com"));
    }

    @Test
    public void testLoginUser() {
        // First register the user
        if (!userDAO.emailExists(testUser.getEmail())) {
            userDAO.registerUser(testUser);
        }
        
        // Test login with correct credentials
        User loggedInUser = userDAO.loginUser(testUser.getEmail(), testUser.getPassword());
        assertNotNull("Login should succeed with correct credentials", loggedInUser);
        assertEquals("Email should match", testUser.getEmail(), loggedInUser.getEmail());
        assertEquals("User type should match", testUser.getUserType(), loggedInUser.getUserType());
        
        // Test login with incorrect password
        User failedLogin = userDAO.loginUser(testUser.getEmail(), "wrongpassword");
        assertNull("Login should fail with incorrect password", failedLogin);
        
        // Test login with non-existent email
        User nonExistent = userDAO.loginUser("nonexistent@example.com", "password");
        assertNull("Login should fail with non-existent email", nonExistent);
    }

    @Test
    public void testGetUserById() {
        // Register user first
        if (!userDAO.emailExists(testUser.getEmail())) {
            userDAO.registerUser(testUser);
        }
        
        User loggedIn = userDAO.loginUser(testUser.getEmail(), testUser.getPassword());
        assertNotNull("User should be able to login", loggedIn);
        
        // Get user by ID
        User retrieved = userDAO.getUserById(loggedIn.getUserId());
        assertNotNull("User should be retrievable by ID", retrieved);
        assertEquals("Email should match", testUser.getEmail(), retrieved.getEmail());
        assertEquals("Full name should match", testUser.getFullName(), retrieved.getFullName());
    }

    @Test
    public void testPasswordHashing() {
        String password = "testpassword";
        String hash1 = PasswordUtil.hashPassword(password);
        String hash2 = PasswordUtil.hashPassword(password);
        
        // Same password should produce same hash
        assertEquals("Same password should produce same hash", hash1, hash2);
        
        // Verify password
        assertTrue("Password verification should succeed", 
                  PasswordUtil.verifyPassword(password, hash1));
        assertFalse("Wrong password should fail verification", 
                   PasswordUtil.verifyPassword("wrongpassword", hash1));
    }
}

