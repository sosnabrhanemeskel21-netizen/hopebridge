package com.humanitarian.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;

import com.humanitarian.dao.DocumentDAO;
import com.humanitarian.dao.HelpRequestDAO;
import com.humanitarian.dao.NotificationDAO;
import com.humanitarian.model.Document;
import com.humanitarian.model.HelpRequest;
import com.humanitarian.model.Notification;
import com.humanitarian.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/survivor/*")
@MultipartConfig(
    maxFileSize = 10485760, // 10MB
    maxRequestSize = 10485760,
    fileSizeThreshold = 1024
)
public class SurvivorServlet extends HttpServlet {
    private HelpRequestDAO helpRequestDAO = new HelpRequestDAO();
    private DocumentDAO documentDAO = new DocumentDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();
    private static final String UPLOAD_DIR = "uploads/documents";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"survivor".equals(user.getUserType())) {
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
                showRequests(request, response, user);
                break;
            case "/create-request":
                showCreateRequest(request, response);
                break;
            case "/view-request":
                viewRequest(request, response, user);
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
        
        if (user == null || !"survivor".equals(user.getUserType())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String action = request.getPathInfo();
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        switch (action) {
            case "/create-request":
                createHelpRequest(request, response, user);
                break;
            case "/upload-document":
                uploadDocument(request, response, user);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<HelpRequest> requests = helpRequestDAO.getHelpRequestsBySurvivor(user.getUserId());
        request.setAttribute("requests", requests);
        request.getRequestDispatcher("/survivor/dashboard.jsp").forward(request, response);
    }

    private void showRequests(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<HelpRequest> requests = helpRequestDAO.getHelpRequestsBySurvivor(user.getUserId());
        request.setAttribute("requests", requests);
        request.getRequestDispatcher("/survivor/requests.jsp").forward(request, response);
    }

    private void showCreateRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/survivor/create-request.jsp").forward(request, response);
    }

    private void viewRequest(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        String requestIdStr = request.getParameter("id");
        if (requestIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try {
            int requestId = Integer.parseInt(requestIdStr);
            HelpRequest helpRequest = helpRequestDAO.getHelpRequestById(requestId);
            
            if (helpRequest == null || helpRequest.getSurvivorId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            
            List<Document> documents = documentDAO.getDocumentsByRequestId(requestId);
            helpRequest.setDocuments(documents);
            
            request.setAttribute("helpRequest", helpRequest);
            request.getRequestDispatcher("/survivor/view-request.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void createHelpRequest(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String helpType = request.getParameter("helpType");
        String location = request.getParameter("location");
        String amountStr = request.getParameter("amountNeeded");
        
        if (title == null || description == null || helpType == null || location == null ||
            title.trim().isEmpty() || description.trim().isEmpty()) {
            request.setAttribute("error", "Please fill all required fields");
            request.getRequestDispatcher("/survivor/create-request.jsp").forward(request, response);
            return;
        }
        
        HelpRequest helpRequest = new HelpRequest();
        helpRequest.setSurvivorId(user.getUserId());
        helpRequest.setTitle(title);
        helpRequest.setDescription(description);
        helpRequest.setHelpType(helpType);
        helpRequest.setLocation(location);
        
        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                helpRequest.setAmountNeeded(new BigDecimal(amountStr));
            } catch (NumberFormatException e) {
                // Invalid amount, ignore
            }
        }
        
        if (helpRequestDAO.createHelpRequest(helpRequest)) {
            // Notify admin
            Notification notification = new Notification();
            notification.setUserId(1); // Admin user ID
            notification.setTitle("New Help Request");
            notification.setMessage("A new help request has been created: " + helpRequest.getTitle());
            notification.setType("info");
            notificationDAO.createNotification(notification);
            
            response.sendRedirect(request.getContextPath() + "/survivor/dashboard");
        } else {
            request.setAttribute("error", "Failed to create help request");
            request.getRequestDispatcher("/survivor/create-request.jsp").forward(request, response);
        }
    }

    private void uploadDocument(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        String requestIdStr = request.getParameter("requestId");
        if (requestIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try {
            int requestId = Integer.parseInt(requestIdStr);
            HelpRequest helpRequest = helpRequestDAO.getHelpRequestById(requestId);
            
            if (helpRequest == null || helpRequest.getSurvivorId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            
            // Create upload directory if it doesn't exist
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // Get document type from form parameter
            String documentType = request.getParameter("documentType");
            
            // Process file upload using Jakarta Part API
            Collection<Part> parts = request.getParts();
            for (Part part : parts) {
                if (part.getName().equals("document") && part.getSize() > 0) {
                    String fileName = getFileName(part);
                    if (fileName != null && !fileName.isEmpty()) {
                        // Sanitize filename
                        String sanitizedFileName = System.currentTimeMillis() + "_" + 
                            fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
                        
                        String filePath = uploadPath + File.separator + sanitizedFileName;
                        File storeFile = new File(filePath);
                        
                        // Save the file
                        try (InputStream inputStream = part.getInputStream()) {
                            Files.copy(inputStream, storeFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                        
                        Document document = new Document();
                        document.setRequestId(requestId);
                        document.setDocumentType(documentType != null ? documentType : "other");
                        document.setFileName(fileName);
                        document.setFilePath(filePath);
                        document.setFileSize(part.getSize());
                        document.setMimeType(part.getContentType());
                        
                        if (documentDAO.saveDocument(document)) {
                            // Notify admin
                            Notification notification = new Notification();
                            notification.setUserId(1);
                            notification.setTitle("New Document Uploaded");
                            notification.setMessage("A new document has been uploaded for request: " + helpRequest.getTitle());
                            notification.setType("info");
                            notificationDAO.createNotification(notification);
                        }
                    }
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/survivor/view-request?id=" + requestId);
        } catch (Exception e) {
            System.err.println("Error uploading document: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Extract filename from Part header
     */
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition != null) {
            String[] tokens = contentDisposition.split(";");
            for (String token : tokens) {
                if (token.trim().startsWith("filename")) {
                    String fileName = token.substring(token.indexOf("=") + 2, token.length() - 1);
                    // Extract just the filename, not the full path
                    return fileName.substring(fileName.lastIndexOf(File.separator) + 1);
                }
            }
        }
        return null;
    }
}

