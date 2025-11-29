<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="View Help Request" />
</jsp:include>

<div class="row">
    <div class="col-md-8">
        <div class="card mb-4">
            <div class="card-header bg-primary text-white">
                <h4 class="mb-0">${helpRequest.title}</h4>
            </div>
            <div class="card-body">
                <p class="text-muted">Created: ${helpRequest.createdAt}</p>
                <p><strong>Description:</strong></p>
                <p>${helpRequest.description}</p>
                
                <div class="row mt-3">
                    <div class="col-md-6">
                        <p><strong>Help Type:</strong> <span class="badge bg-secondary">${helpRequest.helpType}</span></p>
                        <p><strong>Location:</strong> ${helpRequest.location}</p>
                    </div>
                    <div class="col-md-6">
                        <c:if test="${helpRequest.amountNeeded != null}">
                            <p><strong>Amount Needed:</strong> ETB ${helpRequest.amountNeeded}</p>
                        </c:if>
                        <p><strong>Status:</strong> 
                            <c:choose>
                                <c:when test="${helpRequest.status == 'pending'}">
                                    <span class="badge bg-warning">Pending</span>
                                </c:when>
                                <c:when test="${helpRequest.status == 'approved'}">
                                    <span class="badge bg-success">Approved</span>
                                </c:when>
                                <c:when test="${helpRequest.status == 'rejected'}">
                                    <span class="badge bg-danger">Rejected</span>
                                </c:when>
                                <c:when test="${helpRequest.status == 'received'}">
                                    <span class="badge bg-info">Received</span>
                                </c:when>
                            </c:choose>
                        </p>
                        <p><strong>Verified:</strong> 
                            <c:if test="${helpRequest.verified}">
                                <span class="badge bg-success"><i class="fas fa-check"></i> Yes</span>
                            </c:if>
                            <c:if test="${!helpRequest.verified}">
                                <span class="badge bg-warning"><i class="fas fa-clock"></i> Pending</span>
                            </c:if>
                        </p>
                    </div>
                </div>
                
                <c:if test="${not empty helpRequest.adminNotes}">
                    <div class="alert alert-info mt-3">
                        <strong>Admin Notes:</strong> ${helpRequest.adminNotes}
                    </div>
                </c:if>
            </div>
        </div>
        
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0"><i class="fas fa-file-upload"></i> Upload Documents</h5>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/survivor/upload-document" method="post" 
                      enctype="multipart/form-data">
                    <input type="hidden" name="requestId" value="${helpRequest.requestId}">
                    
                    <div class="mb-3">
                        <label for="documentType" class="form-label">Document Type</label>
                        <select class="form-select" id="documentType" name="documentType" required>
                            <option value="id">National ID</option>
                            <option value="displacement_certificate">Displacement Certificate</option>
                            <option value="humanitarian_card">Humanitarian Card</option>
                            <option value="other">Other</option>
                        </select>
                    </div>
                    
                    <div class="mb-3">
                        <label for="document" class="form-label">Select Document</label>
                        <input type="file" class="form-control" id="document" name="document" 
                               accept=".pdf,.jpg,.jpeg,.png" required>
                        <small class="form-text text-muted">Accepted formats: PDF, JPG, PNG (Max 10MB)</small>
                    </div>
                    
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-upload"></i> Upload Document
                    </button>
                </form>
            </div>
        </div>
    </div>
    
    <div class="col-md-4">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0"><i class="fas fa-file-alt"></i> Uploaded Documents</h5>
            </div>
            <div class="card-body">
                <c:if test="${empty helpRequest.documents}">
                    <p class="text-muted">No documents uploaded yet.</p>
                </c:if>
                
                <c:forEach var="doc" items="${helpRequest.documents}">
                    <div class="document-item mb-3">
                        <h6>${doc.documentType}</h6>
                        <p class="mb-1"><small>${doc.fileName}</small></p>
                        <p class="mb-1">
                            <c:if test="${doc.verified}">
                                <span class="badge bg-success"><i class="fas fa-check"></i> Verified</span>
                            </c:if>
                            <c:if test="${!doc.verified}">
                                <span class="badge bg-warning"><i class="fas fa-clock"></i> Pending</span>
                            </c:if>
                        </p>
                        <p class="mb-0"><small class="text-muted">Uploaded: ${doc.uploadedAt}</small></p>
                    </div>
                </c:forEach>
            </div>
        </div>
        
        <div class="card mt-3">
            <div class="card-body">
                <a href="${pageContext.request.contextPath}/survivor/dashboard" class="btn btn-secondary w-100">
                    <i class="fas fa-arrow-left"></i> Back to Dashboard
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../includes/footer.jsp" />

