package com.humanitarian.servlet;

import com.humanitarian.dao.*;
import com.humanitarian.model.*;
import com.humanitarian.util.EmailService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/survivor/*")
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
            
            // Process file upload
            if (ServletFileUpload.isMultipartContent(request)) {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
                ServletFileUpload upload = new ServletFileUpload(factory);
                
                List<FileItem> formItems = upload.parseRequest(request);
                
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String documentType = request.getParameter("documentType");
                        
                        String filePath = uploadPath + File.separator + System.currentTimeMillis() + "_" + fileName;
                        File storeFile = new File(filePath);
                        item.write(storeFile);
                        
                        Document document = new Document();
                        document.setRequestId(requestId);
                        document.setDocumentType(documentType != null ? documentType : "other");
                        document.setFileName(fileName);
                        document.setFilePath(filePath);
                        document.setFileSize(item.getSize());
                        document.setMimeType(item.getContentType());
                        
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
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

