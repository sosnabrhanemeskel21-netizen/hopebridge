package com.humanitarian.dao;

import com.humanitarian.model.Donation;
import com.humanitarian.model.HelpRequest;
import com.humanitarian.model.User;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Test class for DonationDAO
 */
public class DonationDAOTest {
    private DonationDAO donationDAO;
    private HelpRequestDAO helpRequestDAO;
    private UserDAO userDAO;
    private HelpRequest testRequest;
    private User testHelper;
    private Donation testDonation;

    @Before
    public void setUp() {
        donationDAO = new DonationDAO();
        helpRequestDAO = new HelpRequestDAO();
        userDAO = new UserDAO();
        
        // Create test survivor and request
        User testSurvivor = new User();
        testSurvivor.setEmail("donsurvivor@test.com");
        testSurvivor.setPassword("testpass123");
        testSurvivor.setFullName("Donation Test Survivor");
        testSurvivor.setUserType("survivor");
        
        if (!userDAO.emailExists(testSurvivor.getEmail())) {
            userDAO.registerUser(testSurvivor);
        } else {
            testSurvivor = userDAO.loginUser(testSurvivor.getEmail(), testSurvivor.getPassword());
        }
        
        testRequest = new HelpRequest();
        testRequest.setSurvivorId(testSurvivor.getUserId());
        testRequest.setTitle("Donation Test Request");
        testRequest.setDescription("Test description");
        testRequest.setHelpType("money");
        testRequest.setLocation("Test Location");
        helpRequestDAO.createHelpRequest(testRequest);
        helpRequestDAO.updateHelpRequestStatus(testRequest.getRequestId(), "approved", "Test", 1);
        helpRequestDAO.verifyHelpRequest(testRequest.getRequestId(), true, 1);
        
        // Create test helper
        testHelper = new User();
        testHelper.setEmail("donhelper@test.com");
        testHelper.setPassword("testpass123");
        testHelper.setFullName("Donation Test Helper");
        testHelper.setUserType("helper");
        
        if (!userDAO.emailExists(testHelper.getEmail())) {
            userDAO.registerUser(testHelper);
        } else {
            testHelper = userDAO.loginUser(testHelper.getEmail(), testHelper.getPassword());
        }
        
        // Create test donation
        testDonation = new Donation();
        testDonation.setRequestId(testRequest.getRequestId());
        testDonation.setHelperId(testHelper.getUserId());
        testDonation.setDonationType("money");
        testDonation.setAmount(new BigDecimal("50.00"));
        testDonation.setNotes("Test donation");
    }

    @Test
    public void testCreateDonation() {
        boolean result = donationDAO.createDonation(testDonation);
        assertTrue("Donation creation should succeed", result);
        assertTrue("Donation ID should be set", testDonation.getDonationId() > 0);
    }

    @Test
    public void testGetDonationsByHelper() {
        // Create donation first
        donationDAO.createDonation(testDonation);
        
        // Get donations by helper
        java.util.List<Donation> donations = donationDAO.getDonationsByHelper(testHelper.getUserId());
        assertNotNull("Donations list should not be null", donations);
        assertTrue("Should have at least one donation", donations.size() > 0);
    }

    @Test
    public void testGetDonationsByRequest() {
        // Create donation first
        donationDAO.createDonation(testDonation);
        
        // Get donations by request
        java.util.List<Donation> donations = donationDAO.getDonationsByRequest(testRequest.getRequestId());
        assertNotNull("Donations list should not be null", donations);
        assertTrue("Should have at least one donation", donations.size() > 0);
    }

    @Test
    public void testUpdateDonationStatus() {
        // Create donation first
        donationDAO.createDonation(testDonation);
        int donationId = testDonation.getDonationId();
        
        // Update status
        boolean updated = donationDAO.updateDonationStatus(donationId, "confirmed");
        assertTrue("Status update should succeed", updated);
    }

    @Test
    public void testDonationWithItem() {
        Donation itemDonation = new Donation();
        itemDonation.setRequestId(testRequest.getRequestId());
        itemDonation.setHelperId(testHelper.getUserId());
        itemDonation.setDonationType("item");
        itemDonation.setItemDescription("Test item donation");
        
        boolean result = donationDAO.createDonation(itemDonation);
        assertTrue("Item donation creation should succeed", result);
    }

    @Test
    public void testDonationWithService() {
        Donation serviceDonation = new Donation();
        serviceDonation.setRequestId(testRequest.getRequestId());
        serviceDonation.setHelperId(testHelper.getUserId());
        serviceDonation.setDonationType("service");
        serviceDonation.setServiceDescription("Test service donation");
        
        boolean result = donationDAO.createDonation(serviceDonation);
        assertTrue("Service donation creation should succeed", result);
    }
}

