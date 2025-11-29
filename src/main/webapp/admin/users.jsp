<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Manage Users" />
</jsp:include>

<h2 class="mb-4"><i class="fas fa-users"></i> User Management</h2>

<div class="card mb-4">
    <div class="card-body">
        <form method="get" action="${pageContext.request.contextPath}/admin/users">
            <div class="row">
                <div class="col-md-6">
                    <label for="type" class="form-label">Filter by Type</label>
                    <select class="form-select" id="type" name="type" onchange="this.form.submit()">
                        <option value="survivor" ${selectedType == 'survivor' ? 'selected' : ''}>Survivors</option>
                        <option value="helper" ${selectedType == 'helper' ? 'selected' : ''}>Helpers</option>
                    </select>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="card">
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Location</th>
                        <th>Status</th>
                        <th>Created</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td>${user.userId}</td>
                            <td><strong>${user.fullName}</strong></td>
                            <td>${user.email}</td>
                            <td>${user.phone != null ? user.phone : 'N/A'}</td>
                            <td>${user.location != null ? user.location : 'N/A'}</td>
                            <td>
                                <c:if test="${user.blocked}">
                                    <span class="badge bg-danger">Blocked</span>
                                </c:if>
                                <c:if test="${!user.blocked}">
                                    <span class="badge bg-success">Active</span>
                                </c:if>
                            </td>
                            <td>${user.createdAt}</td>
                            <td>
                                <c:if test="${user.blocked}">
                                    <form action="${pageContext.request.contextPath}/admin/unblock-user" method="post" class="d-inline">
                                        <input type="hidden" name="userId" value="${user.userId}">
                                        <button type="submit" class="btn btn-sm btn-success" 
                                                onclick="return confirm('Unblock this user?')">
                                            <i class="fas fa-unlock"></i> Unblock
                                        </button>
                                    </form>
                                </c:if>
                                <c:if test="${!user.blocked}">
                                    <form action="${pageContext.request.contextPath}/admin/block-user" method="post" class="d-inline">
                                        <input type="hidden" name="userId" value="${user.userId}">
                                        <button type="submit" class="btn btn-sm btn-danger" 
                                                onclick="return confirm('Block this user?')">
                                            <i class="fas fa-ban"></i> Block
                                        </button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../includes/footer.jsp" />

