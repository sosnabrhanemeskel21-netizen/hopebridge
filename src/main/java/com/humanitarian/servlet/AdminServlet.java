package com.humanitarian.servlet;

import com.humanitarian.dao.*;
import com.humanitarian.model.*;
import com.humanitarian.util.EmailService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private HelpRequestDAO helpRequestDAO = new HelpRequestDAO();
    private DocumentDAO documentDAO = new DocumentDAO();
    private UserDAO userDAO = new UserDAO();
    private DonationDAO donationDAO = new DonationDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"admin".equals(user.getUserType())) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        String action = request.getPathInfo();
        if (action == null || action.equals("/")) {
            action = "/dashboard";
        }
        
        switch (action) {
            case "/dashboard":
                showDashboard(request, response);
                break;
            case "/requests":
                showAllRequests(request, response);
                break;
            case "/documents":
                showDocuments(request, response);
                break;
            case "/users":
                showUsers(request, response);
                break;
            case "/reports":
                showReports(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"admin".equals(user.getUserType())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String action = request.getPathInfo();
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        switch (action) {
            case "/verify-document":
                verifyDocument(request, response, user);
                break;
            case "/approve-request":
                approveRequest(request, response, user);
                break;
            case "/reject-request":
                rejectRequest(request, response, user);
                break;
            case "/block-user":
                blockUser(request, response, user);
                break;
            case "/unblock-user":
                unblockUser(request, response, user);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> stats = getDashboardStats();
        request.setAttribute("stats", stats);
        
        List<HelpRequest> pendingRequests = helpRequestDAO.getAllHelpRequestsForAdmin();
        request.setAttribute("pendingRequests", pendingRequests.size() > 10 ? 
            pendingRequests.subList(0, 10) : pendingRequests);
        
        List<Document> unverifiedDocs = documentDAO.getUnverifiedDocuments();
        request.setAttribute("unverifiedDocs", unverifiedDocs.size() > 10 ? 
            unverifiedDocs.subList(0, 10) : unverifiedDocs);
        
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }

    private void showAllRequests(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<HelpRequest> requests = helpRequestDAO.getAllHelpRequestsForAdmin();
        request.setAttribute("requests", requests);
        request.getRequestDispatcher("/admin/requests.jsp").forward(request, response);
    }

    private void showDocuments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestIdStr = request.getParameter("requestId");
        if (requestIdStr != null) {
            try {
                int requestId = Integer.parseInt(requestIdStr);
                List<Document> documents = documentDAO.getDocumentsByRequestId(requestId);
                request.setAttribute("documents", documents);
                request.setAttribute("requestId", requestId);
            } catch (NumberFormatException e) {
                // Invalid request ID
            }
        } else {
            List<Document> unverifiedDocs = documentDAO.getUnverifiedDocuments();
            request.setAttribute("documents", unverifiedDocs);
        }
        request.getRequestDispatcher("/admin/documents.jsp").forward(request, response);
    }

    private void showUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userType = request.getParameter("type");
        if (userType == null) userType = "survivor";
        
        List<User> users = userDAO.getAllUsers(userType);
        request.setAttribute("users", users);
        request.setAttribute("selectedType", userType);
        request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
    }

    private void showReports(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> stats = getDashboardStats();
        request.setAttribute("stats", stats);
        request.getRequestDispatcher("/admin/reports.jsp").forward(request, response);
    }

    private void verifyDocument(HttpServletRequest request, HttpServletResponse response, User admin)
            throws IOException {
        String docIdStr = request.getParameter("documentId");
        String verifiedStr = request.getParameter("verified");
        String notes = request.getParameter("notes");
        
        try {
            int docId = Integer.parseInt(docIdStr);
            boolean verified = "true".equals(verifiedStr);
            
            if (documentDAO.verifyDocument(docId, verified, admin.getUserId(), notes)) {
                // Check if all documents for the request are verified
                Document doc = documentDAO.getDocumentById(docId);
                if (doc != null && verified) {
                    List<Document> allDocs = documentDAO.getDocumentsByRequestId(doc.getRequestId());
                    boolean allVerified = allDocs.stream().allMatch(Document::isVerified);
                    
                    if (allVerified) {
                        helpRequestDAO.verifyHelpRequest(doc.getRequestId(), true, admin.getUserId());
                    }
                }
                
                response.sendRedirect(request.getContextPath() + "/admin/documents?requestId=" + 
                    (doc != null ? doc.getRequestId() : ""));
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void approveRequest(HttpServletRequest request, HttpServletResponse response, User admin)
            throws IOException {
        String requestIdStr = request.getParameter("requestId");
        String notes = request.getParameter("notes");
        
        try {
            int requestId = Integer.parseInt(requestIdStr);
            if (helpRequestDAO.updateHelpRequestStatus(requestId, "approved", notes, admin.getUserId())) {
                // Notify survivor
                HelpRequest helpRequest = helpRequestDAO.getHelpRequestById(requestId);
                if (helpRequest != null) {
                    User survivor = userDAO.getUserById(helpRequest.getSurvivorId());
                    if (survivor != null) {
                        Notification notification = new Notification();
                        notification.setUserId(survivor.getUserId());
                        notification.setTitle("Help Request Approved");
                        notification.setMessage("Your help request '" + helpRequest.getTitle() + "' has been approved!");
                        notification.setType("success");
                        notificationDAO.createNotification(notification);
                        
                        EmailService.sendNotificationEmail(survivor.getEmail(),
                            "Help Request Approved",
                            "Your help request '" + helpRequest.getTitle() + "' has been approved and is now visible to helpers.");
                    }
                }
                
                response.sendRedirect(request.getContextPath() + "/admin/requests");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void rejectRequest(HttpServletRequest request, HttpServletResponse response, User admin)
            throws IOException {
        String requestIdStr = request.getParameter("requestId");
        String notes = request.getParameter("notes");
        
        try {
            int requestId = Integer.parseInt(requestIdStr);
            if (helpRequestDAO.updateHelpRequestStatus(requestId, "rejected", notes, admin.getUserId())) {
                // Notify survivor
                HelpRequest helpRequest = helpRequestDAO.getHelpRequestById(requestId);
                if (helpRequest != null) {
                    User survivor = userDAO.getUserById(helpRequest.getSurvivorId());
                    if (survivor != null) {
                        Notification notification = new Notification();
                        notification.setUserId(survivor.getUserId());
                        notification.setTitle("Help Request Rejected");
                        notification.setMessage("Your help request '" + helpRequest.getTitle() + "' has been rejected. Reason: " + notes);
                        notification.setType("warning");
                        notificationDAO.createNotification(notification);
                    }
                }
                
                response.sendRedirect(request.getContextPath() + "/admin/requests");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void blockUser(HttpServletRequest request, HttpServletResponse response, User admin)
            throws IOException {
        String userIdStr = request.getParameter("userId");
        try {
            int userId = Integer.parseInt(userIdStr);
            if (userDAO.blockUser(userId)) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void unblockUser(HttpServletRequest request, HttpServletResponse response, User admin)
            throws IOException {
        String userIdStr = request.getParameter("userId");
        try {
            int userId = Integer.parseInt(userIdStr);
            if (userDAO.unblockUser(userId)) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            Connection conn = com.humanitarian.util.DBConnection.getConnection();
            
            // Total users
            PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE user_type = ?");
            pstmt.setString(1, "survivor");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) stats.put("totalSurvivors", rs.getInt(1));
            
            pstmt.setString(1, "helper");
            rs = pstmt.executeQuery();
            if (rs.next()) stats.put("totalHelpers", rs.getInt(1));
            
            // Total requests
            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM help_requests");
            rs = pstmt.executeQuery();
            if (rs.next()) stats.put("totalRequests", rs.getInt(1));
            
            // Pending requests
            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM help_requests WHERE status = 'pending'");
            rs = pstmt.executeQuery();
            if (rs.next()) stats.put("pendingRequests", rs.getInt(1));
            
            // Approved requests
            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM help_requests WHERE status = 'approved'");
            rs = pstmt.executeQuery();
            if (rs.next()) stats.put("approvedRequests", rs.getInt(1));
            
            // Total donations
            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM donations");
            rs = pstmt.executeQuery();
            if (rs.next()) stats.put("totalDonations", rs.getInt(1));
            
            // Unverified documents
            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM documents WHERE is_verified = FALSE");
            rs = pstmt.executeQuery();
            if (rs.next()) stats.put("unverifiedDocuments", rs.getInt(1));
            
        } catch (Exception e) {
            System.err.println("Error getting stats: " + e.getMessage());
        }
        return stats;
    }
}

