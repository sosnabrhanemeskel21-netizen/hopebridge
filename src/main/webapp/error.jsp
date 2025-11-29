<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="includes/header.jsp">
    <jsp:param name="title" value="Error" />
</jsp:include>

<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card shadow">
            <div class="card-body text-center">
                <i class="fas fa-exclamation-triangle fa-5x text-warning mb-4"></i>
                <h2>Oops! Something went wrong</h2>
                <p class="text-muted">We're sorry, but an error occurred while processing your request.</p>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                    <i class="fas fa-home"></i> Go to Home
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="includes/footer.jsp" />

