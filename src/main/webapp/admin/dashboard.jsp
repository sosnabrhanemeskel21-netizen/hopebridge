<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Admin Dashboard" />
</jsp:include>

<h2 class="mb-4"><i class="fas fa-tachometer-alt"></i> Admin Dashboard</h2>

<div class="row mb-4">
    <div class="col-md-3">
        <div class="stats-card">
            <h3>${stats.totalSurvivors}</h3>
            <p>Total Survivors</p>
        </div>
    </div>
    <div class="col-md-3">
        <div class="stats-card">
            <h3>${stats.totalHelpers}</h3>
            <p>Total Helpers</p>
        </div>
    </div>
    <div class="col-md-3">
        <div class="stats-card">
            <h3>${stats.totalRequests}</h3>
            <p>Total Requests</p>
        </div>
    </div>
    <div class="col-md-3">
        <div class="stats-card">
            <h3>${stats.pendingRequests}</h3>
            <p>Pending Requests</p>
        </div>
    </div>
</div>

<div class="row mb-4">
    <div class="col-md-3">
        <div class="stats-card">
            <h3>${stats.approvedRequests}</h3>
            <p>Approved Requests</p>
        </div>
    </div>
    <div class="col-md-3">
        <div class="stats-card">
            <h3>${stats.totalDonations}</h3>
            <p>Total Donations</p>
        </div>
    </div>
    <div class="col-md-3">
        <div class="stats-card">
            <h3>${stats.unverifiedDocuments}</h3>
            <p>Unverified Documents</p>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card">
            <div class="card-body text-center">
                <a href="${pageContext.request.contextPath}/admin/reports" class="btn btn-primary">
                    <i class="fas fa-chart-bar"></i> View Reports
                </a>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-6">
        <div class="card mb-4">
            <div class="card-header bg-warning text-white">
                <h5 class="mb-0"><i class="fas fa-clock"></i> Pending Requests</h5>
            </div>
            <div class="card-body">
                <c:if test="${empty pendingRequests}">
                    <p class="text-muted">No pending requests.</p>
                </c:if>
                <c:if test="${not empty pendingRequests}">
                    <div class="list-group">
                        <c:forEach var="request" items="${pendingRequests}">
                            <div class="list-group-item">
                                <h6>${request.title}</h6>
                                <p class="mb-1"><small>${request.location} - ${request.helpType}</small></p>
                                <a href="${pageContext.request.contextPath}/admin/requests" class="btn btn-sm btn-primary">
                                    Review
                                </a>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
    
    <div class="col-md-6">
        <div class="card mb-4">
            <div class="card-header bg-danger text-white">
                <h5 class="mb-0"><i class="fas fa-file-alt"></i> Unverified Documents</h5>
            </div>
            <div class="card-body">
                <c:if test="${empty unverifiedDocs}">
                    <p class="text-muted">No unverified documents.</p>
                </c:if>
                <c:if test="${not empty unverifiedDocs}">
                    <div class="list-group">
                        <c:forEach var="doc" items="${unverifiedDocs}">
                            <div class="list-group-item">
                                <h6>${doc.documentType}</h6>
                                <p class="mb-1"><small>${doc.fileName}</small></p>
                                <a href="${pageContext.request.contextPath}/admin/documents?requestId=${doc.requestId}" 
                                   class="btn btn-sm btn-primary">
                                    Verify
                                </a>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-12">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0"><i class="fas fa-link"></i> Quick Links</h5>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-3 mb-2">
                        <a href="${pageContext.request.contextPath}/admin/requests" class="btn btn-primary w-100">
                            <i class="fas fa-list"></i> All Requests
                        </a>
                    </div>
                    <div class="col-md-3 mb-2">
                        <a href="${pageContext.request.contextPath}/admin/documents" class="btn btn-primary w-100">
                            <i class="fas fa-file-alt"></i> Documents
                        </a>
                    </div>
                    <div class="col-md-3 mb-2">
                        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-primary w-100">
                            <i class="fas fa-users"></i> Users
                        </a>
                    </div>
                    <div class="col-md-3 mb-2">
                        <a href="${pageContext.request.contextPath}/admin/reports" class="btn btn-primary w-100">
                            <i class="fas fa-chart-bar"></i> Reports
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../includes/footer.jsp" />

