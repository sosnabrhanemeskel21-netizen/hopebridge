package com.humanitarian.servlet;

import com.humanitarian.dao.*;
import com.humanitarian.model.*;
import com.humanitarian.util.EmailService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/helper/*")
public class HelperServlet extends HttpServlet {
    private HelpRequestDAO helpRequestDAO = new HelpRequestDAO();
    private DonationDAO donationDAO = new DonationDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"helper".equals(user.getUserType())) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        String action = request.getPathInfo();
        if (action == null || action.equals("/")) {
            action = "/dashboard";
        }
        
        switch (action) {
            case "/dashboard":
                showDashboard(request, response, user);
                break;
            case "/requests":
                showRequests(request, response);
                break;
            case "/view-request":
                viewRequest(request, response);
                break;
            case "/my-donations":
                showMyDonations(request, response, user);
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
        
        if (user == null || !"helper".equals(user.getUserType())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String action = request.getPathInfo();
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        switch (action) {
            case "/donate":
                createDonation(request, response, user);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<Donation> donations = donationDAO.getDonationsByHelper(user.getUserId());
        // Load help request details for each donation
        for (Donation donation : donations) {
            HelpRequest helpRequest = helpRequestDAO.getHelpRequestById(donation.getRequestId());
            donation.setHelpRequest(helpRequest);
        }
        request.setAttribute("donations", donations);
        
        // Get recent verified requests
        List<HelpRequest> recentRequests = helpRequestDAO.getVerifiedHelpRequests(null, null);
        request.setAttribute("recentRequests", recentRequests.size() > 5 ? 
            recentRequests.subList(0, 5) : recentRequests);
        
        request.getRequestDispatcher("/helper/dashboard.jsp").forward(request, response);
    }

    private void showRequests(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String helpType = request.getParameter("helpType");
        String location = request.getParameter("location");
        
        List<HelpRequest> requests = helpRequestDAO.getVerifiedHelpRequests(helpType, location);
        request.setAttribute("requests", requests);
        request.setAttribute("selectedHelpType", helpType);
        request.setAttribute("selectedLocation", location);
        request.getRequestDispatcher("/helper/requests.jsp").forward(request, response);
    }

    private void viewRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestIdStr = request.getParameter("id");
        if (requestIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try {
            int requestId = Integer.parseInt(requestIdStr);
            HelpRequest helpRequest = helpRequestDAO.getHelpRequestById(requestId);
            
            if (helpRequest == null || !helpRequest.isVerified() || 
                !"approved".equals(helpRequest.getStatus())) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            request.setAttribute("helpRequest", helpRequest);
            request.getRequestDispatcher("/helper/view-request.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void showMyDonations(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<Donation> donations = donationDAO.getDonationsByHelper(user.getUserId());
        request.setAttribute("donations", donations);
        request.getRequestDispatcher("/helper/my-donations.jsp").forward(request, response);
    }

    private void createDonation(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        String requestIdStr = request.getParameter("requestId");
        String donationType = request.getParameter("donationType");
        String amountStr = request.getParameter("amount");
        String itemDescription = request.getParameter("itemDescription");
        String serviceDescription = request.getParameter("serviceDescription");
        String notes = request.getParameter("notes");
        
        if (requestIdStr == null || donationType == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try {
            int requestId = Integer.parseInt(requestIdStr);
            HelpRequest helpRequest = helpRequestDAO.getHelpRequestById(requestId);
            
            if (helpRequest == null || !helpRequest.isVerified()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            Donation donation = new Donation();
            donation.setRequestId(requestId);
            donation.setHelperId(user.getUserId());
            donation.setDonationType(donationType);
            donation.setNotes(notes);
            
            if ("money".equals(donationType) && amountStr != null && !amountStr.trim().isEmpty()) {
                try {
                    donation.setAmount(new BigDecimal(amountStr));
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid amount");
                    viewRequest(request, response);
                    return;
                }
            } else if ("item".equals(donationType)) {
                donation.setItemDescription(itemDescription);
            } else if ("service".equals(donationType)) {
                donation.setServiceDescription(serviceDescription);
            }
            
            if (donationDAO.createDonation(donation)) {
                // Notify survivor
                User survivor = userDAO.getUserById(helpRequest.getSurvivorId());
                if (survivor != null) {
                    Notification notification = new Notification();
                    notification.setUserId(survivor.getUserId());
                    notification.setTitle("New Donation Received");
                    notification.setMessage("A helper has selected your help request: " + helpRequest.getTitle());
                    notification.setType("success");
                    notificationDAO.createNotification(notification);
                    
                    // Send email
                    EmailService.sendNotificationEmail(survivor.getEmail(), 
                        "New Donation Received", 
                        "A helper has selected your help request: " + helpRequest.getTitle());
                }
                
                response.sendRedirect(request.getContextPath() + "/helper/my-donations");
            } else {
                request.setAttribute("error", "Failed to create donation");
                viewRequest(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

