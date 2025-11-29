<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Reports & Statistics" />
</jsp:include>

<h2 class="mb-4"><i class="fas fa-chart-bar"></i> Reports & Statistics</h2>

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
            <h3>${stats.totalDonations}</h3>
            <p>Total Donations</p>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">Request Status Breakdown</h5>
            </div>
            <div class="card-body">
                <p><strong>Pending:</strong> ${stats.pendingRequests}</p>
                <p><strong>Approved:</strong> ${stats.approvedRequests}</p>
                <p><strong>Unverified Documents:</strong> ${stats.unverifiedDocuments}</p>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">System Health</h5>
            </div>
            <div class="card-body">
                <p><strong>Active Users:</strong> ${stats.totalSurvivors + stats.totalHelpers}</p>
                <p><strong>Verification Rate:</strong> 
                    <c:set var="totalDocs" value="${stats.totalRequests * 3}" />
                    <c:set var="verifiedDocs" value="${totalDocs - stats.unverifiedDocuments}" />
                    <c:if test="${totalDocs > 0}">
                        ${(verifiedDocs / totalDocs * 100)}%
                    </c:if>
                    <c:if test="${totalDocs == 0}">
                        N/A
                    </c:if>
                </p>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../includes/footer.jsp" />

