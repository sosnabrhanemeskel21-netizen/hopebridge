package com.humanitarian.servlet;

import com.humanitarian.dao.UserDAO;
import com.humanitarian.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        if (action == null || action.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        switch (action) {
            case "/login":
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                break;
            case "/register":
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        switch (action) {
            case "/login":
                handleLogin(request, response);
                break;
            case "/register":
                handleRegister(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Email and password are required");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        User user = userDAO.loginUser(email, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userType", user.getUserType());
            session.setAttribute("userName", user.getFullName());
            
            // Redirect based on user type
            String redirectPath = getRedirectPath(user.getUserType());
            response.sendRedirect(request.getContextPath() + redirectPath);
        } else {
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String userType = request.getParameter("userType");
        String location = request.getParameter("location");
        String language = request.getParameter("language");
        
        // Validation
        if (email == null || password == null || fullName == null || userType == null ||
            email.trim().isEmpty() || password.trim().isEmpty() || fullName.trim().isEmpty()) {
            request.setAttribute("error", "Please fill all required fields");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        if (userDAO.emailExists(email)) {
            request.setAttribute("error", "Email already exists");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setUserType(userType);
        user.setLocation(location);
        user.setLanguagePreference(language != null ? language : "en");
        
        if (userDAO.registerUser(user)) {
            request.setAttribute("success", "Registration successful! Please login.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Registration failed. Please try again.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/");
    }

    private String getRedirectPath(String userType) {
        switch (userType) {
            case "admin":
                return "/admin/dashboard";
            case "survivor":
                return "/survivor/dashboard";
            case "helper":
                return "/helper/dashboard";
            default:
                return "/";
        }
    }
}

