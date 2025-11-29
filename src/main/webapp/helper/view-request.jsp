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
                        <p><strong>Status:</strong> <span class="badge bg-success">Approved & Verified</span></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <div class="col-md-4">
        <div class="card">
            <div class="card-header bg-success text-white">
                <h5 class="mb-0"><i class="fas fa-hand-holding-heart"></i> Make a Donation</h5>
            </div>
            <div class="card-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/helper/donate" method="post">
                    <input type="hidden" name="requestId" value="${helpRequest.requestId}">
                    
                    <div class="mb-3">
                        <label for="donationType" class="form-label">Donation Type <span class="text-danger">*</span></label>
                        <select class="form-select" id="donationType" name="donationType" required 
                                onchange="toggleDonationFields()">
                            <option value="">Select...</option>
                            <option value="money">Money</option>
                            <option value="item">Item</option>
                            <option value="service">Service</option>
                        </select>
                    </div>
                    
                    <div class="mb-3" id="amountField" style="display: none;">
                        <label for="amount" class="form-label">Amount (ETB)</label>
                        <input type="number" class="form-control" id="amount" name="amount" 
                               step="0.01" min="0">
                    </div>
                    
                    <div class="mb-3" id="itemField" style="display: none;">
                        <label for="itemDescription" class="form-label">Item Description</label>
                        <textarea class="form-control" id="itemDescription" name="itemDescription" rows="3"
                                  placeholder="Describe the item you want to donate..."></textarea>
                    </div>
                    
                    <div class="mb-3" id="serviceField" style="display: none;">
                        <label for="serviceDescription" class="form-label">Service Description</label>
                        <textarea class="form-control" id="serviceDescription" name="serviceDescription" rows="3"
                                  placeholder="Describe the service you want to provide..."></textarea>
                    </div>
                    
                    <div class="mb-3">
                        <label for="notes" class="form-label">Additional Notes</label>
                        <textarea class="form-control" id="notes" name="notes" rows="2"
                                  placeholder="Any additional information..."></textarea>
                    </div>
                    
                    <button type="submit" class="btn btn-success w-100">
                        <i class="fas fa-check"></i> Submit Donation
                    </button>
                </form>
            </div>
        </div>
        
        <div class="card mt-3">
            <div class="card-body">
                <a href="${pageContext.request.contextPath}/helper/requests" class="btn btn-secondary w-100">
                    <i class="fas fa-arrow-left"></i> Back to Requests
                </a>
            </div>
        </div>
    </div>
</div>

<script>
function toggleDonationFields() {
    const donationType = document.getElementById('donationType').value;
    document.getElementById('amountField').style.display = donationType === 'money' ? 'block' : 'none';
    document.getElementById('itemField').style.display = donationType === 'item' ? 'block' : 'none';
    document.getElementById('serviceField').style.display = donationType === 'service' ? 'block' : 'none';
}
</script>

<jsp:include page="../includes/footer.jsp" />

