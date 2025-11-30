-- Humanitarian Support Platform Database Schema
-- For Tigray Crisis Support System
-- PostgreSQL Version

-- Create database (run this separately as superuser)
-- CREATE DATABASE humanitarian_support;
-- \c humanitarian_support;

-- Enable UUID extension if needed
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users table (Survivors, Helpers, Admins)
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    user_type VARCHAR(20) NOT NULL CHECK (user_type IN ('survivor', 'helper', 'admin')),
    location VARCHAR(255),
    language_preference VARCHAR(2) DEFAULT 'en' CHECK (language_preference IN ('en', 'am')),
    is_verified BOOLEAN DEFAULT FALSE,
    is_blocked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);

CREATE INDEX idx_email ON users(email);
CREATE INDEX idx_user_type ON users(user_type);
CREATE INDEX idx_location ON users(location);

-- Help Requests table
CREATE TABLE help_requests (
    request_id SERIAL PRIMARY KEY,
    survivor_id INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    help_type VARCHAR(20) NOT NULL CHECK (help_type IN ('money', 'food', 'clothing', 'shelter', 'medical', 'education', 'other')),
    amount_needed DECIMAL(10,2),
    location VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'approved', 'received', 'rejected')),
    is_verified BOOLEAN DEFAULT FALSE,
    admin_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (survivor_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_survivor ON help_requests(survivor_id);
CREATE INDEX idx_status ON help_requests(status);
CREATE INDEX idx_help_type ON help_requests(help_type);
CREATE INDEX idx_location ON help_requests(location);

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Trigger to auto-update updated_at for help_requests
CREATE TRIGGER update_help_requests_updated_at BEFORE UPDATE ON help_requests
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Documents table (Legal documents uploaded by survivors)
CREATE TABLE documents (
    document_id SERIAL PRIMARY KEY,
    request_id INTEGER NOT NULL,
    document_type VARCHAR(30) NOT NULL CHECK (document_type IN ('id', 'displacement_certificate', 'humanitarian_card', 'other')),
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT,
    mime_type VARCHAR(100),
    is_verified BOOLEAN DEFAULT FALSE,
    verified_by INTEGER NULL,
    verification_notes TEXT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verified_at TIMESTAMP NULL,
    FOREIGN KEY (request_id) REFERENCES help_requests(request_id) ON DELETE CASCADE,
    FOREIGN KEY (verified_by) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE INDEX idx_request ON documents(request_id);
CREATE INDEX idx_verified ON documents(is_verified);

-- Donations table
CREATE TABLE donations (
    donation_id SERIAL PRIMARY KEY,
    request_id INTEGER NOT NULL,
    helper_id INTEGER NOT NULL,
    donation_type VARCHAR(20) NOT NULL CHECK (donation_type IN ('money', 'item', 'service')),
    amount DECIMAL(10,2),
    item_description TEXT,
    service_description TEXT,
    status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'confirmed', 'delivered', 'cancelled')),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (request_id) REFERENCES help_requests(request_id) ON DELETE CASCADE,
    FOREIGN KEY (helper_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_request ON donations(request_id);
CREATE INDEX idx_helper ON donations(helper_id);
CREATE INDEX idx_status ON donations(status);

-- Trigger to auto-update updated_at for donations
CREATE TRIGGER update_donations_updated_at BEFORE UPDATE ON donations
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Notifications table
CREATE TABLE notifications (
    notification_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(20) DEFAULT 'info' CHECK (type IN ('info', 'success', 'warning', 'error')),
    is_read BOOLEAN DEFAULT FALSE,
    email_sent BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_user ON notifications(user_id);
CREATE INDEX idx_read ON notifications(is_read);

-- Admin Actions Log
CREATE TABLE admin_actions (
    action_id SERIAL PRIMARY KEY,
    admin_id INTEGER NOT NULL,
    action_type VARCHAR(100) NOT NULL,
    target_type VARCHAR(50),
    target_id INTEGER,
    description TEXT,
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_admin ON admin_actions(admin_id);
CREATE INDEX idx_action_type ON admin_actions(action_type);

-- Insert default admin user (password: admin123 - should be hashed in production)
-- Password hash for 'admin123' using SHA-256: 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a
-- For now, using simple hash - in production use proper password hashing
INSERT INTO users (email, password, full_name, user_type, is_verified, location) 
VALUES ('sosnabrhanemeskel21@gmail.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a', 'System Administrator', 'admin', TRUE, 'Ethiopia');
