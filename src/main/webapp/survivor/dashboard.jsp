<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Survivor Dashboard" />
</jsp:include>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2><i class="fas fa-tachometer-alt"></i> My Dashboard</h2>
    <a href="${pageContext.request.contextPath}/survivor/create-request" class="btn btn-primary">
        <i class="fas fa-plus"></i> Create Help Request
    </a>
</div>

<div class="row mb-4">
    <div class="col-md-3">
        <div class="card text-white bg-primary">
            <div class="card-body">
                <h5>Total Requests</h5>
                <h3>${requests.size()}</h3>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card text-white bg-success">
            <div class="card-body">
                <h5>Approved</h5>
                <h3><c:set var="approvedCount" value="0" />
                <c:forEach var="req" items="${requests}">
                    <c:if test="${req.status == 'approved'}">
                        <c:set var="approvedCount" value="${approvedCount + 1}" />
                    </c:if>
                </c:forEach>
                ${approvedCount}</h3>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card text-white bg-warning">
            <div class="card-body">
                <h5>Pending</h5>
                <h3><c:set var="pendingCount" value="0" />
                <c:forEach var="req" items="${requests}">
                    <c:if test="${req.status == 'pending'}">
                        <c:set var="pendingCount" value="${pendingCount + 1}" />
                    </c:if>
                </c:forEach>
                ${pendingCount}</h3>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card text-white bg-info">
            <div class="card-body">
                <h5>Received</h5>
                <h3><c:set var="receivedCount" value="0" />
                <c:forEach var="req" items="${requests}">
                    <c:if test="${req.status == 'received'}">
                        <c:set var="receivedCount" value="${receivedCount + 1}" />
                    </c:if>
                </c:forEach>
                ${receivedCount}</h3>
            </div>
        </div>
    </div>
</div>

<div class="card">
    <div class="card-header">
        <h5 class="mb-0"><i class="fas fa-list"></i> My Help Requests</h5>
    </div>
    <div class="card-body">
        <c:if test="${empty requests}">
            <div class="alert alert-info">
                <i class="fas fa-info-circle"></i> You haven't created any help requests yet.
                <a href="${pageContext.request.contextPath}/survivor/create-request" class="alert-link">Create one now</a>
            </div>
        </c:if>
        
        <c:if test="${not empty requests}">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Help Type</th>
                            <th>Location</th>
                            <th>Status</th>
                            <th>Created</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="request" items="${requests}">
                            <tr>
                                <td><strong>${request.title}</strong></td>
                                <td><span class="badge bg-secondary">${request.helpType}</span></td>
                                <td>${request.location}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${request.status == 'pending'}">
                                            <span class="badge bg-warning">Pending</span>
                                        </c:when>
                                        <c:when test="${request.status == 'approved'}">
                                            <span class="badge bg-success">Approved</span>
                                        </c:when>
                                        <c:when test="${request.status == 'rejected'}">
                                            <span class="badge bg-danger">Rejected</span>
                                        </c:when>
                                        <c:when test="${request.status == 'received'}">
                                            <span class="badge bg-info">Received</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>${request.createdAt}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/survivor/view-request?id=${request.requestId}" 
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

<jsp:include page="../includes/footer.jsp" />

