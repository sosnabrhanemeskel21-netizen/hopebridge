<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Browse Help Requests" />
</jsp:include>

<h2 class="mb-4"><i class="fas fa-search"></i> Browse Help Requests</h2>

<div class="card mb-4">
    <div class="card-body">
        <form method="get" action="${pageContext.request.contextPath}/helper/requests">
            <div class="row">
                <div class="col-md-4 mb-3">
                    <label for="helpType" class="form-label">Help Type</label>
                    <select class="form-select" id="helpType" name="helpType">
                        <option value="">All Types</option>
                        <option value="money" ${selectedHelpType == 'money' ? 'selected' : ''}>Money</option>
                        <option value="food" ${selectedHelpType == 'food' ? 'selected' : ''}>Food</option>
                        <option value="clothing" ${selectedHelpType == 'clothing' ? 'selected' : ''}>Clothing</option>
                        <option value="shelter" ${selectedHelpType == 'shelter' ? 'selected' : ''}>Shelter</option>
                        <option value="medical" ${selectedHelpType == 'medical' ? 'selected' : ''}>Medical</option>
                        <option value="education" ${selectedHelpType == 'education' ? 'selected' : ''}>Education</option>
                        <option value="other" ${selectedHelpType == 'other' ? 'selected' : ''}>Other</option>
                    </select>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="location" class="form-label">Location</label>
                    <input type="text" class="form-control" id="location" name="location" 
                           value="${selectedLocation}" placeholder="e.g., Tigray">
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">&nbsp;</label>
                    <div>
                        <button type="submit" class="btn btn-primary w-100">
                            <i class="fas fa-search"></i> Search
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="row">
    <c:if test="${empty requests}">
        <div class="col-12">
            <div class="alert alert-info">
                <i class="fas fa-info-circle"></i> No verified help requests found matching your criteria.
            </div>
        </div>
    </c:if>
    
    <c:forEach var="request" items="${requests}">
        <div class="col-md-6 mb-4">
            <div class="card h-100 shadow-sm">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0">${request.title}</h5>
                </div>
                <div class="card-body">
                    <p class="card-text">${request.description.length() > 150 ? request.description.substring(0, 150) + '...' : request.description}</p>
                    
                    <div class="mb-2">
                        <span class="badge bg-secondary">${request.helpType}</span>
                        <span class="badge bg-success"><i class="fas fa-check"></i> Verified</span>
                    </div>
                    
                    <p class="mb-1"><i class="fas fa-map-marker-alt"></i> ${request.location}</p>
                    <c:if test="${request.amountNeeded != null}">
                        <p class="mb-1"><i class="fas fa-money-bill"></i> ETB ${request.amountNeeded}</p>
                    </c:if>
                    <p class="mb-3 text-muted"><small><i class="fas fa-clock"></i> ${request.createdAt}</small></p>
                    
                    <a href="${pageContext.request.contextPath}/helper/view-request?id=${request.requestId}" 
                       class="btn btn-primary w-100">
                        <i class="fas fa-hand-holding-heart"></i> Help This Request
                    </a>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<jsp:include page="../includes/footer.jsp" />

