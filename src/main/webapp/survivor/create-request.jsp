<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Create Help Request" />
</jsp:include>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card shadow">
            <div class="card-header bg-primary text-white">
                <h4 class="mb-0"><i class="fas fa-plus-circle"></i> Create Help Request</h4>
            </div>
            <div class="card-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/survivor/create-request" method="post" class="needs-validation" novalidate>
                    <div class="mb-3">
                        <label for="title" class="form-label">Request Title <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="title" name="title" required 
                               placeholder="e.g., Need food assistance for my family">
                    </div>
                    
                    <div class="mb-3">
                        <label for="description" class="form-label">Description <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="description" name="description" rows="5" required
                                  placeholder="Please describe your situation and what kind of help you need..."></textarea>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="helpType" class="form-label">Type of Help Needed <span class="text-danger">*</span></label>
                            <select class="form-select" id="helpType" name="helpType" required>
                                <option value="">Select...</option>
                                <option value="money">Money</option>
                                <option value="food">Food</option>
                                <option value="clothing">Clothing</option>
                                <option value="shelter">Shelter</option>
                                <option value="medical">Medical</option>
                                <option value="education">Education</option>
                                <option value="other">Other</option>
                            </select>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="amountNeeded" class="form-label">Amount Needed (if applicable)</label>
                            <input type="number" class="form-control" id="amountNeeded" name="amountNeeded" 
                                   step="0.01" min="0" placeholder="0.00">
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="location" class="form-label">Location <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="location" name="location" required
                               placeholder="e.g., Mekelle, Tigray, Ethiopia">
                    </div>
                    
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle"></i> 
                        <strong>Note:</strong> After creating your request, you'll need to upload legal documents 
                        (ID, displacement certificate, humanitarian card) for verification. Only verified requests 
                        will be visible to helpers.
                    </div>
                    
                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                        <a href="${pageContext.request.contextPath}/survivor/dashboard" class="btn btn-secondary">
                            <i class="fas fa-times"></i> Cancel
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-check"></i> Create Request
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../includes/footer.jsp" />

