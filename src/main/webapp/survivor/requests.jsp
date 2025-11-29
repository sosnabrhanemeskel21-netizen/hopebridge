<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="My Help Requests" />
</jsp:include>

<h2 class="mb-4"><i class="fas fa-list"></i> My Help Requests</h2>

<div class="card">
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
                            <th>Verified</th>
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
                                <td>
                                    <c:if test="${request.verified}">
                                        <span class="badge bg-success"><i class="fas fa-check"></i></span>
                                    </c:if>
                                    <c:if test="${!request.verified}">
                                        <span class="badge bg-warning"><i class="fas fa-clock"></i></span>
                                    </c:if>
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

