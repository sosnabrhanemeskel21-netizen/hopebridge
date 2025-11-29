<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Manage Requests" />
</jsp:include>

<h2 class="mb-4"><i class="fas fa-list"></i> All Help Requests</h2>

<div class="card">
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Survivor</th>
                        <th>Type</th>
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
                            <td>${request.requestId}</td>
                            <td><strong>${request.title}</strong></td>
                            <td>${request.survivor.fullName}</td>
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
                                <div class="btn-group">
                                    <c:if test="${request.status == 'pending'}">
                                        <form action="${pageContext.request.contextPath}/admin/approve-request" method="post" class="d-inline">
                                            <input type="hidden" name="requestId" value="${request.requestId}">
                                            <button type="submit" class="btn btn-sm btn-success" 
                                                    onclick="return confirm('Approve this request?')">
                                                <i class="fas fa-check"></i>
                                            </button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/admin/reject-request" method="post" class="d-inline">
                                            <input type="hidden" name="requestId" value="${request.requestId}">
                                            <button type="button" class="btn btn-sm btn-danger" 
                                                    onclick="showRejectModal(${request.requestId})">
                                                <i class="fas fa-times"></i>
                                            </button>
                                        </form>
                                    </c:if>
                                    <a href="${pageContext.request.contextPath}/admin/documents?requestId=${request.requestId}" 
                                       class="btn btn-sm btn-info">
                                        <i class="fas fa-file-alt"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Reject Modal -->
<div class="modal fade" id="rejectModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/admin/reject-request" method="post">
                <div class="modal-header">
                    <h5 class="modal-title">Reject Request</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="requestId" id="rejectRequestId">
                    <div class="mb-3">
                        <label for="rejectNotes" class="form-label">Reason for Rejection</label>
                        <textarea class="form-control" id="rejectNotes" name="notes" rows="3" required></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-danger">Reject Request</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function showRejectModal(requestId) {
    document.getElementById('rejectRequestId').value = requestId;
    new bootstrap.Modal(document.getElementById('rejectModal')).show();
}
</script>

<jsp:include page="../includes/footer.jsp" />

