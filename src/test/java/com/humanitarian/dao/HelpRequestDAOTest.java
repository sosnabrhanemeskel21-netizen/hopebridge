package com.humanitarian.dao;

import com.humanitarian.model.HelpRequest;
import com.humanitarian.model.User;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Test class for HelpRequestDAO
 */
public class HelpRequestDAOTest {
    private HelpRequestDAO helpRequestDAO;
    private UserDAO userDAO;
    private User testSurvivor;
    private HelpRequest testRequest;

    @Before
    public void setUp() {
        helpRequestDAO = new HelpRequestDAO();
        userDAO = new UserDAO();
        
        // Create or get test survivor
        testSurvivor = new User();
        testSurvivor.setEmail("survivor@test.com");
        testSurvivor.setPassword("testpass123");
        testSurvivor.setFullName("Test Survivor");
        testSurvivor.setUserType("survivor");
        testSurvivor.setLocation("Test Location");
        
        if (!userDAO.emailExists(testSurvivor.getEmail())) {
            userDAO.registerUser(testSurvivor);
        } else {
            testSurvivor = userDAO.loginUser(testSurvivor.getEmail(), testSurvivor.getPassword());
        }
        
        // Create test request
        testRequest = new HelpRequest();
        testRequest.setSurvivorId(testSurvivor.getUserId());
        testRequest.setTitle("Test Help Request");
        testRequest.setDescription("This is a test help request description");
        testRequest.setHelpType("food");
        testRequest.setAmountNeeded(new BigDecimal("100.00"));
        testRequest.setLocation("Test Location");
    }

    @Test
    public void testCreateHelpRequest() {
        boolean result = helpRequestDAO.createHelpRequest(testRequest);
        assertTrue("Help request creation should succeed", result);
        assertTrue("Request ID should be set after creation", testRequest.getRequestId() > 0);
    }

    @Test
    public void testGetHelpRequestById() {
        // Create request first
        helpRequestDAO.createHelpRequest(testRequest);
        int requestId = testRequest.getRequestId();
        
        // Retrieve request
        HelpRequest retrieved = helpRequestDAO.getHelpRequestById(requestId);
        assertNotNull("Help request should be retrievable", retrieved);
        assertEquals("Title should match", testRequest.getTitle(), retrieved.getTitle());
        assertEquals("Description should match", testRequest.getDescription(), retrieved.getDescription());
        assertEquals("Help type should match", testRequest.getHelpType(), retrieved.getHelpType());
        assertEquals("Location should match", testRequest.getLocation(), retrieved.getLocation());
    }

    @Test
    public void testGetHelpRequestsBySurvivor() {
        // Create a request
        helpRequestDAO.createHelpRequest(testRequest);
        
        // Get all requests for survivor
        java.util.List<HelpRequest> requests = helpRequestDAO.getHelpRequestsBySurvivor(testSurvivor.getUserId());
        assertNotNull("Request list should not be null", requests);
        assertTrue("Should have at least one request", requests.size() > 0);
        
        // Verify the request is in the list
        boolean found = false;
        for (HelpRequest req : requests) {
            if (req.getRequestId() == testRequest.getRequestId()) {
                found = true;
                break;
            }
        }
        assertTrue("Created request should be in the list", found);
    }

    @Test
    public void testGetVerifiedHelpRequests() {
        // Create and approve a request
        helpRequestDAO.createHelpRequest(testRequest);
        helpRequestDAO.updateHelpRequestStatus(testRequest.getRequestId(), "approved", "Test approval", 1);
        helpRequestDAO.verifyHelpRequest(testRequest.getRequestId(), true, 1);
        
        // Get verified requests
        java.util.List<HelpRequest> verified = helpRequestDAO.getVerifiedHelpRequests(null, null);
        assertNotNull("Verified requests list should not be null", verified);
    }

    @Test
    public void testUpdateHelpRequestStatus() {
        // Create request
        helpRequestDAO.createHelpRequest(testRequest);
        int requestId = testRequest.getRequestId();
        
        // Update status
        boolean updated = helpRequestDAO.updateHelpRequestStatus(requestId, "approved", "Test notes", 1);
        assertTrue("Status update should succeed", updated);
        
        // Verify status was updated
        HelpRequest retrieved = helpRequestDAO.getHelpRequestById(requestId);
        assertEquals("Status should be approved", "approved", retrieved.getStatus());
        assertEquals("Admin notes should be set", "Test notes", retrieved.getAdminNotes());
    }
}

