package com.humanitarian.dao;

import com.humanitarian.model.HelpRequest;
import com.humanitarian.model.User;
import com.humanitarian.util.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelpRequestDAO {
    public boolean createHelpRequest(HelpRequest request) {
        String sql = "INSERT INTO help_requests (survivor_id, title, description, help_type, amount_needed, location) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, request.getSurvivorId());
            pstmt.setString(2, request.getTitle());
            pstmt.setString(3, request.getDescription());
            pstmt.setString(4, request.getHelpType());
            if (request.getAmountNeeded() != null) {
                pstmt.setBigDecimal(5, request.getAmountNeeded());
            } else {
                pstmt.setNull(5, Types.DECIMAL);
            }
            pstmt.setString(6, request.getLocation());
            
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    request.setRequestId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating help request: " + e.getMessage());
        }
        return false;
    }

    public HelpRequest getHelpRequestById(int requestId) {
        String sql = "SELECT hr.*, u.full_name, u.email, u.phone, " +
                     "(SELECT COUNT(*) FROM donations WHERE request_id = hr.request_id) as donation_count " +
                     "FROM help_requests hr " +
                     "LEFT JOIN users u ON hr.survivor_id = u.user_id " +
                     "WHERE hr.request_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, requestId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                HelpRequest request = mapResultSetToHelpRequest(rs);
                // Set survivor info if available
                try {
                    User survivor = new User();
                    survivor.setFullName(rs.getString("full_name"));
                    survivor.setEmail(rs.getString("email"));
                    survivor.setPhone(rs.getString("phone"));
                    request.setSurvivor(survivor);
                } catch (Exception e) {
                    // Ignore if survivor info not available
                }
                return request;
            }
        } catch (SQLException e) {
            System.err.println("Error getting help request: " + e.getMessage());
        }
        return null;
    }

    public List<HelpRequest> getHelpRequestsBySurvivor(int survivorId) {
        List<HelpRequest> requests = new ArrayList<>();
        String sql = "SELECT hr.*, " +
                     "(SELECT COUNT(*) FROM donations WHERE request_id = hr.request_id) as donation_count " +
                     "FROM help_requests hr WHERE hr.survivor_id = ? ORDER BY hr.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, survivorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                requests.add(mapResultSetToHelpRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting help requests: " + e.getMessage());
        }
        return requests;
    }

    public List<HelpRequest> getVerifiedHelpRequests(String helpType, String location) {
        List<HelpRequest> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT hr.*, u.full_name, u.email, " +
            "(SELECT COUNT(*) FROM donations WHERE request_id = hr.request_id) as donation_count " +
            "FROM help_requests hr " +
            "LEFT JOIN users u ON hr.survivor_id = u.user_id " +
            "WHERE hr.status = 'approved' AND hr.is_verified = TRUE AND hr.status != 'received' "
        );
        
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        
        if (helpType != null && !helpType.isEmpty()) {
            conditions.add("hr.help_type = ?");
            params.add(helpType);
        }
        if (location != null && !location.isEmpty()) {
            conditions.add("hr.location LIKE ?");
            params.add("%" + location + "%");
        }
        
        if (!conditions.isEmpty()) {
            sql.append("AND ").append(String.join(" AND ", conditions));
        }
        
        sql.append(" ORDER BY hr.created_at DESC");
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                HelpRequest request = mapResultSetToHelpRequest(rs);
                // Set survivor info if available
                try {
                    User survivor = new User();
                    survivor.setFullName(rs.getString("full_name"));
                    survivor.setEmail(rs.getString("email"));
                    request.setSurvivor(survivor);
                } catch (Exception e) {
                    // Ignore if survivor info not available
                }
                requests.add(request);
            }
        } catch (SQLException e) {
            System.err.println("Error getting verified help requests: " + e.getMessage());
        }
        return requests;
    }

    public List<HelpRequest> getAllHelpRequestsForAdmin() {
        List<HelpRequest> requests = new ArrayList<>();
        String sql = "SELECT hr.*, u.full_name, u.email, " +
                     "(SELECT COUNT(*) FROM donations WHERE request_id = hr.request_id) as donation_count " +
                     "FROM help_requests hr " +
                     "LEFT JOIN users u ON hr.survivor_id = u.user_id " +
                     "ORDER BY hr.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                requests.add(mapResultSetToHelpRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all help requests: " + e.getMessage());
        }
        return requests;
    }

    public boolean updateHelpRequestStatus(int requestId, String status, String adminNotes, int adminId) {
        String sql = "UPDATE help_requests SET status = ?, admin_notes = ?, updated_at = CURRENT_TIMESTAMP WHERE request_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, adminNotes);
            pstmt.setInt(3, requestId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating help request status: " + e.getMessage());
            return false;
        }
    }

    public boolean verifyHelpRequest(int requestId, boolean verified, int adminId) {
        String sql = "UPDATE help_requests SET is_verified = ?, updated_at = CURRENT_TIMESTAMP WHERE request_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBoolean(1, verified);
            pstmt.setInt(2, requestId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error verifying help request: " + e.getMessage());
            return false;
        }
    }

    private HelpRequest mapResultSetToHelpRequest(ResultSet rs) throws SQLException {
        HelpRequest request = new HelpRequest();
        request.setRequestId(rs.getInt("request_id"));
        request.setSurvivorId(rs.getInt("survivor_id"));
        request.setTitle(rs.getString("title"));
        request.setDescription(rs.getString("description"));
        request.setHelpType(rs.getString("help_type"));
        BigDecimal amount = rs.getBigDecimal("amount_needed");
        if (amount != null) {
            request.setAmountNeeded(amount);
        }
        request.setLocation(rs.getString("location"));
        request.setStatus(rs.getString("status"));
        request.setVerified(rs.getBoolean("is_verified"));
        request.setAdminNotes(rs.getString("admin_notes"));
        request.setCreatedAt(rs.getTimestamp("created_at"));
        request.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        try {
            request.setDonationCount(rs.getInt("donation_count"));
        } catch (SQLException e) {
            request.setDonationCount(0);
        }
        
        return request;
    }
}

