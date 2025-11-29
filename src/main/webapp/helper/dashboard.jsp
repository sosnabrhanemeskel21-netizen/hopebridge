<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Helper Dashboard" />
</jsp:include>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2><i class="fas fa-tachometer-alt"></i> Helper Dashboard</h2>
    <a href="${pageContext.request.contextPath}/helper/requests" class="btn btn-primary">
        <i class="fas fa-search"></i> Browse Requests
    </a>
</div>

<div class="row mb-4">
    <div class="col-md-6">
        <div class="card text-white bg-primary">
            <div class="card-body">
                <h5>My Donations</h5>
                <h3>${donations.size()}</h3>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card text-white bg-success">
            <div class="card-body">
                <h5>Available Requests</h5>
                <h3>${recentRequests.size()}</h3>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-8">
        <div class="card mb-4">
            <div class="card-header">
                <h5 class="mb-0"><i class="fas fa-list"></i> My Donations</h5>
            </div>
            <div class="card-body">
                <c:if test="${empty donations}">
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle"></i> You haven't made any donations yet.
                        <a href="${pageContext.request.contextPath}/helper/requests" class="alert-link">Browse requests</a>
                    </div>
                </c:if>
                
                <c:if test="${not empty donations}">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Request</th>
                                    <th>Type</th>
                                    <th>Status</th>
                                    <th>Date</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="donation" items="${donations}">
                                    <tr>
                                        <td>${donation.helpRequest.title}</td>
                                        <td><span class="badge bg-secondary">${donation.donationType}</span></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${donation.status == 'pending'}">
                                                    <span class="badge bg-warning">Pending</span>
                                                </c:when>
                                                <c:when test="${donation.status == 'confirmed'}">
                                                    <span class="badge bg-success">Confirmed</span>
                                                </c:when>
                                                <c:when test="${donation.status == 'delivered'}">
                                                    <span class="badge bg-info">Delivered</span>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td>${donation.createdAt}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/helper/view-request?id=${donation.requestId}" 
                                               class="btn btn-sm btn-primary">
                                                <i class="fas fa-eye"></i> View
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
    
    <div class="col-md-4">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0"><i class="fas fa-fire"></i> Recent Requests</h5>
            </div>
            <div class="card-body">
                <c:if test="${empty recentRequests}">
                    <p class="text-muted">No recent requests available.</p>
                </c:if>
                
                <c:forEach var="request" items="${recentRequests}">
                    <div class="mb-3 pb-3 border-bottom">
                        <h6>${request.title}</h6>
                        <p class="mb-1"><small>${request.location}</small></p>
                        <p class="mb-1"><span class="badge bg-secondary">${request.helpType}</span></p>
                        <a href="${pageContext.request.contextPath}/helper/view-request?id=${request.requestId}" 
                           class="btn btn-sm btn-primary">
                            View Details
                        </a>
                    </div>
                </c:forEach>
                
                <a href="${pageContext.request.contextPath}/helper/requests" class="btn btn-primary w-100 mt-3">
                    View All Requests
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../includes/footer.jsp" />

