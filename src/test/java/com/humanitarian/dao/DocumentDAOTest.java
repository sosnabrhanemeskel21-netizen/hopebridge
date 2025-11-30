package com.humanitarian.dao;

import com.humanitarian.model.Document;
import com.humanitarian.model.HelpRequest;
import com.humanitarian.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for DocumentDAO
 */
public class DocumentDAOTest {
    private DocumentDAO documentDAO;
    private HelpRequestDAO helpRequestDAO;
    private UserDAO userDAO;
    private HelpRequest testRequest;
    private Document testDocument;

    @Before
    public void setUp() {
        documentDAO = new DocumentDAO();
        helpRequestDAO = new HelpRequestDAO();
        userDAO = new UserDAO();
        
        // Create test survivor and request
        User testSurvivor = new User();
        testSurvivor.setEmail("docsurvivor@test.com");
        testSurvivor.setPassword("testpass123");
        testSurvivor.setFullName("Document Test Survivor");
        testSurvivor.setUserType("survivor");
        
        if (!userDAO.emailExists(testSurvivor.getEmail())) {
            userDAO.registerUser(testSurvivor);
        } else {
            testSurvivor = userDAO.loginUser(testSurvivor.getEmail(), testSurvivor.getPassword());
        }
        
        testRequest = new HelpRequest();
        testRequest.setSurvivorId(testSurvivor.getUserId());
        testRequest.setTitle("Document Test Request");
        testRequest.setDescription("Test description");
        testRequest.setHelpType("medical");
        testRequest.setLocation("Test Location");
        helpRequestDAO.createHelpRequest(testRequest);
        
        // Create test document
        testDocument = new Document();
        testDocument.setRequestId(testRequest.getRequestId());
        testDocument.setDocumentType("id");
        testDocument.setFileName("test_id.pdf");
        testDocument.setFilePath("/uploads/test_id.pdf");
        testDocument.setFileSize(1024L);
        testDocument.setMimeType("application/pdf");
    }

    @Test
    public void testSaveDocument() {
        boolean result = documentDAO.saveDocument(testDocument);
        assertTrue("Document save should succeed", result);
        assertTrue("Document ID should be set", testDocument.getDocumentId() > 0);
    }

    @Test
    public void testGetDocumentById() {
        // Save document first
        documentDAO.saveDocument(testDocument);
        int docId = testDocument.getDocumentId();
        
        // Retrieve document
        Document retrieved = documentDAO.getDocumentById(docId);
        assertNotNull("Document should be retrievable", retrieved);
        assertEquals("Document type should match", testDocument.getDocumentType(), retrieved.getDocumentType());
        assertEquals("File name should match", testDocument.getFileName(), retrieved.getFileName());
    }

    @Test
    public void testGetDocumentsByRequestId() {
        // Save document
        documentDAO.saveDocument(testDocument);
        
        // Get all documents for request
        java.util.List<Document> documents = documentDAO.getDocumentsByRequestId(testRequest.getRequestId());
        assertNotNull("Documents list should not be null", documents);
        assertFalse("Should have at least one document", documents.isEmpty());
    }

    @Test
    public void testVerifyDocument() {
        // Save document first
        documentDAO.saveDocument(testDocument);
        int docId = testDocument.getDocumentId();
        
        // Verify document
        boolean verified = documentDAO.verifyDocument(docId, true, 1, "Test verification");
        assertTrue("Document verification should succeed", verified);
        
        // Check verification status
        Document retrieved = documentDAO.getDocumentById(docId);
        assertTrue("Document should be verified", retrieved.isVerified());
        assertEquals("Verified by should be set", Integer.valueOf(1), retrieved.getVerifiedBy());
    }

    @Test
    public void testGetUnverifiedDocuments() {
        // Save unverified document
        documentDAO.saveDocument(testDocument);
        
        // Get unverified documents
        java.util.List<Document> unverified = documentDAO.getUnverifiedDocuments();
        assertNotNull("Unverified documents list should not be null", unverified);
    }
}

