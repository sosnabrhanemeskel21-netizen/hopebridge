<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="My Donations" />
</jsp:include>

<h2 class="mb-4"><i class="fas fa-heart"></i> My Donations</h2>

<div class="card">
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
                            <th>Donation Type</th>
                            <th>Details</th>
                            <th>Status</th>
                            <th>Date</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="donation" items="${donations}">
                            <tr>
                                <td><strong>${donation.helpRequest.title}</strong></td>
                                <td><span class="badge bg-secondary">${donation.donationType}</span></td>
                                <td>
                                    <c:if test="${donation.amount != null}">
                                        ETB ${donation.amount}
                                    </c:if>
                                    <c:if test="${donation.itemDescription != null}">
                                        ${donation.itemDescription}
                                    </c:if>
                                    <c:if test="${donation.serviceDescription != null}">
                                        ${donation.serviceDescription}
                                    </c:if>
                                </td>
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
                                        <c:when test="${donation.status == 'cancelled'}">
                                            <span class="badge bg-danger">Cancelled</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>${donation.createdAt}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/helper/view-request?id=${donation.requestId}" 
                                       class="btn btn-sm btn-primary">
                                        <i class="fas fa-eye"></i> View Request
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

