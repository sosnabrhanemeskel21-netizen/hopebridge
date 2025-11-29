<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="includes/header.jsp">
    <jsp:param name="title" value="Humanitarian Support Platform - Home" />
</jsp:include>

<div class="hero-section bg-primary text-white text-center py-5 mb-5 rounded">
    <div class="container">
        <h1 class="display-4 mb-3"><i class="fas fa-hands-helping"></i> Humanitarian Support Platform</h1>
        <p class="lead">Connecting survivors of conflicts with donors, volunteers, and organizations</p>
        <p class="mb-4">Supporting those affected by the Tigray crisis in Ethiopia</p>
        <c:if test="${sessionScope.user == null}">
            <a href="${pageContext.request.contextPath}/auth/register" class="btn btn-light btn-lg me-2">
                <i class="fas fa-user-plus"></i> Get Started
            </a>
            <a href="${pageContext.request.contextPath}/auth/login" class="btn btn-outline-light btn-lg">
                <i class="fas fa-sign-in-alt"></i> Login
            </a>
        </c:if>
        <c:if test="${sessionScope.user != null}">
            <c:choose>
                <c:when test="${sessionScope.userType == 'survivor'}">
                    <a href="${pageContext.request.contextPath}/survivor/dashboard" class="btn btn-light btn-lg">
                        <i class="fas fa-tachometer-alt"></i> Go to Dashboard
                    </a>
                </c:when>
                <c:when test="${sessionScope.userType == 'helper'}">
                    <a href="${pageContext.request.contextPath}/helper/dashboard" class="btn btn-light btn-lg">
                        <i class="fas fa-tachometer-alt"></i> Go to Dashboard
                    </a>
                </c:when>
                <c:when test="${sessionScope.userType == 'admin'}">
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-light btn-lg">
                        <i class="fas fa-tachometer-alt"></i> Admin Dashboard
                    </a>
                </c:when>
            </c:choose>
        </c:if>
    </div>
</div>

<div class="row mb-5">
    <div class="col-md-4 mb-4">
        <div class="card h-100 shadow-sm">
            <div class="card-body text-center">
                <i class="fas fa-user-injured fa-3x text-primary mb-3"></i>
                <h3 class="card-title">For Survivors</h3>
                <p class="card-text">Create verified help requests, upload legal documents, and track your requests through the approval process.</p>
                <ul class="list-unstyled text-start">
                    <li><i class="fas fa-check text-success"></i> Register and verify your identity</li>
                    <li><i class="fas fa-check text-success"></i> Post help requests with documents</li>
                    <li><i class="fas fa-check text-success"></i> Track request status</li>
                </ul>
            </div>
        </div>
    </div>
    <div class="col-md-4 mb-4">
        <div class="card h-100 shadow-sm">
            <div class="card-body text-center">
                <i class="fas fa-heart fa-3x text-danger mb-3"></i>
                <h3 class="card-title">For Helpers</h3>
                <p class="card-text">View verified help requests and donate money, items, or services to those in need.</p>
                <ul class="list-unstyled text-start">
                    <li><i class="fas fa-check text-success"></i> Browse verified requests</li>
                    <li><i class="fas fa-check text-success"></i> Donate securely</li>
                    <li><i class="fas fa-check text-success"></i> Receive notifications</li>
                </ul>
            </div>
        </div>
    </div>
    <div class="col-md-4 mb-4">
        <div class="card h-100 shadow-sm">
            <div class="card-body text-center">
                <i class="fas fa-shield-alt fa-3x text-warning mb-3"></i>
                <h3 class="card-title">Secure & Verified</h3>
                <p class="card-text">All documents are verified by administrators. Only approved requests are visible to helpers.</p>
                <ul class="list-unstyled text-start">
                    <li><i class="fas fa-check text-success"></i> Document verification</li>
                    <li><i class="fas fa-check text-success"></i> Secure transactions</li>
                    <li><i class="fas fa-check text-success"></i> Admin oversight</li>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-12">
        <div class="card">
            <div class="card-header bg-info text-white">
                <h4 class="mb-0"><i class="fas fa-info-circle"></i> How It Works</h4>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-3 text-center mb-3">
                        <div class="step-number bg-primary text-white rounded-circle d-inline-flex align-items-center justify-content-center" style="width: 60px; height: 60px; font-size: 24px; font-weight: bold;">1</div>
                        <h5 class="mt-3">Register</h5>
                        <p>Create an account as a survivor or helper</p>
                    </div>
                    <div class="col-md-3 text-center mb-3">
                        <div class="step-number bg-primary text-white rounded-circle d-inline-flex align-items-center justify-content-center" style="width: 60px; height: 60px; font-size: 24px; font-weight: bold;">2</div>
                        <h5 class="mt-3">Verify</h5>
                        <p>Survivors upload legal documents for verification</p>
                    </div>
                    <div class="col-md-3 text-center mb-3">
                        <div class="step-number bg-primary text-white rounded-circle d-inline-flex align-items-center justify-content-center" style="width: 60px; height: 60px; font-size: 24px; font-weight: bold;">3</div>
                        <h5 class="mt-3">Approve</h5>
                        <p>Admins verify documents and approve requests</p>
                    </div>
                    <div class="col-md-3 text-center mb-3">
                        <div class="step-number bg-primary text-white rounded-circle d-inline-flex align-items-center justify-content-center" style="width: 60px; height: 60px; font-size: 24px; font-weight: bold;">4</div>
                        <h5 class="mt-3">Connect</h5>
                        <p>Helpers view and respond to verified requests</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="includes/footer.jsp" />
