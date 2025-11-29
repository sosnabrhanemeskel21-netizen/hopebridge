-- Humanitarian Support Platform Database Schema
-- For Tigray Crisis Support System

CREATE DATABASE IF NOT EXISTS humanitarian_support CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE humanitarian_support;

-- Users table (Survivors, Helpers, Admins)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    user_type ENUM('survivor', 'helper', 'admin') NOT NULL,
    location VARCHAR(255),
    language_preference ENUM('en', 'am') DEFAULT 'en',
    is_verified BOOLEAN DEFAULT FALSE,
    is_blocked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    INDEX idx_email (email),
    INDEX idx_user_type (user_type),
    INDEX idx_location (location)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Help Requests table
CREATE TABLE help_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    survivor_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    help_type ENUM('money', 'food', 'clothing', 'shelter', 'medical', 'education', 'other') NOT NULL,
    amount_needed DECIMAL(10,2),
    location VARCHAR(255) NOT NULL,
    status ENUM('pending', 'approved', 'received', 'rejected') DEFAULT 'pending',
    is_verified BOOLEAN DEFAULT FALSE,
    admin_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (survivor_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_survivor (survivor_id),
    INDEX idx_status (status),
    INDEX idx_help_type (help_type),
    INDEX idx_location (location)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Documents table (Legal documents uploaded by survivors)
CREATE TABLE documents (
    document_id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT NOT NULL,
    document_type ENUM('id', 'displacement_certificate', 'humanitarian_card', 'other') NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT,
    mime_type VARCHAR(100),
    is_verified BOOLEAN DEFAULT FALSE,
    verified_by INT NULL,
    verification_notes TEXT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verified_at TIMESTAMP NULL,
    FOREIGN KEY (request_id) REFERENCES help_requests(request_id) ON DELETE CASCADE,
    FOREIGN KEY (verified_by) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_request (request_id),
    INDEX idx_verified (is_verified)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Donations table
CREATE TABLE donations (
    donation_id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT NOT NULL,
    helper_id INT NOT NULL,
    donation_type ENUM('money', 'item', 'service') NOT NULL,
    amount DECIMAL(10,2),
    item_description TEXT,
    service_description TEXT,
    status ENUM('pending', 'confirmed', 'delivered', 'cancelled') DEFAULT 'pending',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (request_id) REFERENCES help_requests(request_id) ON DELETE CASCADE,
    FOREIGN KEY (helper_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_request (request_id),
    INDEX idx_helper (helper_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Notifications table
CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type ENUM('info', 'success', 'warning', 'error') DEFAULT 'info',
    is_read BOOLEAN DEFAULT FALSE,
    email_sent BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Admin Actions Log
CREATE TABLE admin_actions (
    action_id INT AUTO_INCREMENT PRIMARY KEY,
    admin_id INT NOT NULL,
    action_type VARCHAR(100) NOT NULL,
    target_type VARCHAR(50),
    target_id INT,
    description TEXT,
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_admin (admin_id),
    INDEX idx_action_type (action_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert default admin user (password: admin123 - should be hashed in production)
-- Password hash for 'admin123' using BCrypt: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- For now, using simple hash - in production use proper password hashing
INSERT INTO users (email, password, full_name, user_type, is_verified, location) 
VALUES ('admin@humanitarian.org', 'admin123', 'System Administrator', 'admin', TRUE, 'Ethiopia');

