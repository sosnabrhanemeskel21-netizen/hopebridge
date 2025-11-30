package com.humanitarian.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for PasswordUtil
 */
public class PasswordUtilTest {

    @Test
    public void testHashPassword() {
        String password = "testpassword123";
        String hash = PasswordUtil.hashPassword(password);
        
        assertNotNull("Hash should not be null", hash);
        assertFalse("Hash should not be empty", hash.isEmpty());
        assertEquals("Hash should be 64 characters (SHA-256 hex)", 64, hash.length());
    }

    @Test
    public void testHashPasswordConsistency() {
        String password = "testpassword123";
        String hash1 = PasswordUtil.hashPassword(password);
        String hash2 = PasswordUtil.hashPassword(password);
        
        // Same password should produce same hash
        assertEquals("Same password should produce same hash", hash1, hash2);
    }

    @Test
    public void testHashPasswordUniqueness() {
        String password1 = "password1";
        String password2 = "password2";
        
        String hash1 = PasswordUtil.hashPassword(password1);
        String hash2 = PasswordUtil.hashPassword(password2);
        
        // Different passwords should produce different hashes
        assertNotEquals("Different passwords should produce different hashes", hash1, hash2);
    }

    @Test
    public void testVerifyPassword() {
        String password = "testpassword123";
        String hash = PasswordUtil.hashPassword(password);
        
        // Correct password should verify
        assertTrue("Correct password should verify", PasswordUtil.verifyPassword(password, hash));
        
        // Wrong password should not verify
        assertFalse("Wrong password should not verify", 
                   PasswordUtil.verifyPassword("wrongpassword", hash));
        
        // Empty password should not verify
        assertFalse("Empty password should not verify", 
                   PasswordUtil.verifyPassword("", hash));
    }

    @Test
    public void testVerifyPasswordWithNull() {
        String password = "testpassword";
        String hash = PasswordUtil.hashPassword(password);
        
        // Null password should not verify
        try {
            boolean result = PasswordUtil.verifyPassword(null, hash);
            assertFalse("Null password should not verify", result);
        } catch (Exception e) {
            // Exception is acceptable for null input
            assertTrue("Exception should be thrown for null password", true);
        }
    }

    @Test
    public void testHashPasswordWithSpecialCharacters() {
        String[] passwords = {
            "password123",
            "P@ssw0rd!",
            "test-password_123",
            "password with spaces",
            "中文密码",
            "пароль"
        };
        
        for (String password : passwords) {
            String hash = PasswordUtil.hashPassword(password);
            assertNotNull("Hash should not be null for: " + password, hash);
            assertTrue("Password should verify: " + password, 
                      PasswordUtil.verifyPassword(password, hash));
        }
    }
}

