package com.humanitarian.dao;

import com.humanitarian.model.Notification;
import com.humanitarian.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for NotificationDAO
 */
public class NotificationDAOTest {
    private NotificationDAO notificationDAO;
    private UserDAO userDAO;
    private User testUser;
    private Notification testNotification;

    @Before
    public void setUp() {
        notificationDAO = new NotificationDAO();
        userDAO = new UserDAO();
        
        // Create or get test user
        testUser = new User();
        testUser.setEmail("notifuser@test.com");
        testUser.setPassword("testpass123");
        testUser.setFullName("Notification Test User");
        testUser.setUserType("survivor");
        
        if (!userDAO.emailExists(testUser.getEmail())) {
            userDAO.registerUser(testUser);
        } else {
            testUser = userDAO.loginUser(testUser.getEmail(), testUser.getPassword());
        }
        
        // Create test notification
        testNotification = new Notification();
        testNotification.setUserId(testUser.getUserId());
        testNotification.setTitle("Test Notification");
        testNotification.setMessage("This is a test notification message");
        testNotification.setType("info");
    }

    @Test
    public void testCreateNotification() {
        boolean result = notificationDAO.createNotification(testNotification);
        assertTrue("Notification creation should succeed", result);
        assertTrue("Notification ID should be set", testNotification.getNotificationId() > 0);
    }

    @Test
    public void testGetNotificationsByUser() {
        // Create notification first
        notificationDAO.createNotification(testNotification);
        
        // Get notifications for user
        java.util.List<Notification> notifications = notificationDAO.getNotificationsByUser(testUser.getUserId(), false);
        assertNotNull("Notifications list should not be null", notifications);
        assertTrue("Should have at least one notification", notifications.size() > 0);
    }

    @Test
    public void testGetUnreadNotifications() {
        // Create notification
        notificationDAO.createNotification(testNotification);
        
        // Get unread notifications
        java.util.List<Notification> unread = notificationDAO.getNotificationsByUser(testUser.getUserId(), true);
        assertNotNull("Unread notifications list should not be null", unread);
    }

    @Test
    public void testMarkAsRead() {
        // Create notification first
        notificationDAO.createNotification(testNotification);
        int notificationId = testNotification.getNotificationId();
        
        // Mark as read
        boolean marked = notificationDAO.markAsRead(notificationId);
        assertTrue("Mark as read should succeed", marked);
    }

    @Test
    public void testMarkEmailSent() {
        // Create notification first
        notificationDAO.createNotification(testNotification);
        int notificationId = testNotification.getNotificationId();
        
        // Mark email as sent
        boolean marked = notificationDAO.markEmailSent(notificationId);
        assertTrue("Mark email sent should succeed", marked);
    }

    @Test
    public void testNotificationTypes() {
        String[] types = {"info", "success", "warning", "error"};
        
        for (String type : types) {
            Notification notif = new Notification();
            notif.setUserId(testUser.getUserId());
            notif.setTitle("Test " + type);
            notif.setMessage("Test message");
            notif.setType(type);
            
            boolean result = notificationDAO.createNotification(notif);
            assertTrue("Notification with type " + type + " should be created", result);
        }
    }
}

