<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="includes/header.jsp">
    <jsp:param name="title" value="Register - Humanitarian Support Platform" />
</jsp:include>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card shadow">
            <div class="card-header bg-primary text-white">
                <h4 class="mb-0"><i class="fas fa-user-plus"></i> Register</h4>
            </div>
            <div class="card-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/auth/register" method="post" class="needs-validation" novalidate>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="fullName" class="form-label">Full Name <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="fullName" name="fullName" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="email" class="form-label">Email Address <span class="text-danger">*</span></label>
                            <input type="email" class="form-control" id="email" name="email" required>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="password" class="form-label">Password <span class="text-danger">*</span></label>
                            <input type="password" class="form-control" id="password" name="password" required minlength="6">
                            <small class="form-text text-muted">Minimum 6 characters</small>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="phone" class="form-label">Phone Number</label>
                            <input type="tel" class="form-control" id="phone" name="phone">
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="userType" class="form-label">I am a <span class="text-danger">*</span></label>
                            <select class="form-select" id="userType" name="userType" required>
                                <option value="">Select...</option>
                                <option value="survivor">Survivor (Need Help)</option>
                                <option value="helper">Helper (Want to Help)</option>
                            </select>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="location" class="form-label">Location</label>
                            <input type="text" class="form-control" id="location" name="location" placeholder="e.g., Tigray, Ethiopia">
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="language" class="form-label">Language Preference</label>
                        <select class="form-select" id="language" name="language">
                            <option value="en">English</option>
                            <option value="am">አማርኛ (Amharic)</option>
                        </select>
                    </div>
                    
                    <div class="d-grid">
                        <button type="submit" class="btn btn-primary btn-lg">
                            <i class="fas fa-user-plus"></i> Register
                        </button>
                    </div>
                </form>
                
                <hr>
                <p class="text-center mb-0">
                    Already have an account? 
                    <a href="${pageContext.request.contextPath}/auth/login">Login here</a>
                </p>
            </div>
        </div>
    </div>
</div>

<jsp:include page="includes/footer.jsp" />

