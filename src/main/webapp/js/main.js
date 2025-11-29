// Main JavaScript for Humanitarian Support Platform

document.addEventListener('DOMContentLoaded', function() {
    // Auto-hide alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // Confirm delete actions
    const deleteButtons = document.querySelectorAll('[data-confirm-delete]');
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            if (!confirm('Are you sure you want to delete this item?')) {
                e.preventDefault();
            }
        });
    });

    // File upload preview
    const fileInputs = document.querySelectorAll('input[type="file"]');
    fileInputs.forEach(function(input) {
        input.addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                const fileSize = (file.size / 1024 / 1024).toFixed(2);
                if (fileSize > 10) {
                    alert('File size should be less than 10MB');
                    e.target.value = '';
                }
            }
        });
    });

    // Form validation
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Mark notifications as read
    const notificationLinks = document.querySelectorAll('.notification-item');
    notificationLinks.forEach(function(link) {
        link.addEventListener('click', function() {
            const notificationId = this.dataset.notificationId;
            if (notificationId) {
                fetch('/project1/api/notifications/mark-read?id=' + notificationId, {
                    method: 'POST'
                });
            }
        });
    });
});

// Language toggle function
function toggleLanguage() {
    const currentLang = document.documentElement.lang || 'en';
    const newLang = currentLang === 'en' ? 'am' : 'en';
    window.location.href = '?lang=' + newLang;
}

