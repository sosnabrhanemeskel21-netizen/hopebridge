<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String lang = (String) session.getAttribute("language");
    if (lang == null) lang = "en";
    
    // Language strings
    java.util.Map<String, String> strings = new java.util.HashMap<>();
    if ("am".equals(lang)) {
        strings.put("home", "መነሻ");
        strings.put("login", "ግባ");
        strings.put("register", "ተመዝግብ");
        strings.put("logout", "ውጣ");
        strings.put("welcome", "እንኳን ደህና መጡ");
    } else {
        strings.put("home", "Home");
        strings.put("login", "Login");
        strings.put("register", "Register");
        strings.put("logout", "Logout");
        strings.put("welcome", "Welcome");
    }
    pageContext.setAttribute("strings", strings);
%>
<!DOCTYPE html>
<html lang="${lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title != null ? param.title : 'Humanitarian Support Platform'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                <i class="fas fa-hands-helping"></i> Humanitarian Support
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <c:if test="${sessionScope.user == null}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/"><i class="fas fa-home"></i> ${strings.home}</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/auth/login"><i class="fas fa-sign-in-alt"></i> ${strings.login}</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/auth/register"><i class="fas fa-user-plus"></i> ${strings.register}</a>
                        </li>
                    </c:if>
                    <c:if test="${sessionScope.user != null}">
                        <li class="nav-item">
                            <span class="navbar-text me-3 text-white">
                                <i class="fas fa-user"></i> ${sessionScope.userName}
                            </span>
                        </li>
                        <c:if test="${sessionScope.userType == 'survivor'}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/survivor/dashboard">Dashboard</a>
                            </li>
                        </c:if>
                        <c:if test="${sessionScope.userType == 'helper'}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/helper/dashboard">Dashboard</a>
                            </li>
                        </c:if>
                        <c:if test="${sessionScope.userType == 'admin'}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">Admin</a>
                            </li>
                        </c:if>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/auth/logout"><i class="fas fa-sign-out-alt"></i> ${strings.logout}</a>
                        </li>
                    </c:if>
                </ul>
            </div>
        </div>
    </nav>
    <main class="container my-4">

