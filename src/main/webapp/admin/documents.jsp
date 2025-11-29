<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Manage Documents" />
</jsp:include>

<h2 class="mb-4"><i class="fas fa-file-alt"></i> Document Verification</h2>

<c:if test="${not empty requestId}">
    <div class="alert alert-info">
        <i class="fas fa-info-circle"></i> Viewing documents for Request ID: ${requestId}
        <a href="${pageContext.request.contextPath}/admin/documents" class="btn btn-sm btn-secondary ms-2">
            View All Documents
        </a>
    </div>
</c:if>

<div class="card">
    <div class="card-body">
        <c:if test="${empty documents}">
            <div class="alert alert-info">
                <i class="fas fa-info-circle"></i> No documents found.
            </div>
        </c:if>
        
        <c:if test="${not empty documents}">
            <div class="row">
                <c:forEach var="doc" items="${documents}">
                    <div class="col-md-6 mb-4">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0">${doc.documentType}</h5>
                            </div>
                            <div class="card-body">
                                <p><strong>File:</strong> ${doc.fileName}</p>
                                <p><strong>Size:</strong> ${doc.fileSize / 1024} KB</p>
                                <p><strong>Uploaded:</strong> ${doc.uploadedAt}</p>
                                
                                <c:if test="${doc.verified}">
                                    <p><span class="badge bg-success"><i class="fas fa-check"></i> Verified</span></p>
                                    <c:if test="${not empty doc.verificationNotes}">
                                        <p><strong>Notes:</strong> ${doc.verificationNotes}</p>
                                    </c:if>
                                </c:if>
                                <c:if test="${!doc.verified}">
                                    <p><span class="badge bg-warning"><i class="fas fa-clock"></i> Pending Verification</span></p>
                                    
                                    <form action="${pageContext.request.contextPath}/admin/verify-document" method="post" class="mt-3">
                                        <input type="hidden" name="documentId" value="${doc.documentId}">
                                        <div class="mb-3">
                                            <label class="form-label">Verification</label>
                                            <div>
                                                <button type="submit" name="verified" value="true" class="btn btn-success">
                                                    <i class="fas fa-check"></i> Approve
                                                </button>
                                                <button type="button" class="btn btn-danger" 
                                                        onclick="showRejectDocModal(${doc.documentId})">
                                                    <i class="fas fa-times"></i> Reject
                                                </button>
                                            </div>
                                        </div>
                                        <div class="mb-3">
                                            <label for="notes_${doc.documentId}" class="form-label">Notes</label>
                                            <textarea class="form-control" id="notes_${doc.documentId}" name="notes" rows="2"></textarea>
                                        </div>
                                    </form>
                                </c:if>
                                
                                <a href="${pageContext.request.contextPath}/uploads/documents/${doc.fileName}" 
                                   target="_blank" class="btn btn-sm btn-primary">
                                    <i class="fas fa-download"></i> View Document
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </div>
</div>

<!-- Reject Document Modal -->
<div class="modal fade" id="rejectDocModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/admin/verify-document" method="post">
                <div class="modal-header">
                    <h5 class="modal-title">Reject Document</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="documentId" id="rejectDocId">
                    <input type="hidden" name="verified" value="false">
                    <div class="mb-3">
                        <label for="rejectDocNotes" class="form-label">Reason for Rejection</label>
                        <textarea class="form-control" id="rejectDocNotes" name="notes" rows="3" required></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-danger">Reject Document</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function showRejectDocModal(docId) {
    document.getElementById('rejectDocId').value = docId;
    new bootstrap.Modal(document.getElementById('rejectDocModal')).show();
}
</script>

<jsp:include page="../includes/footer.jsp" />

