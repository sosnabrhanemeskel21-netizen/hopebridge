package com.humanitarian.dao;

import com.humanitarian.model.Donation;
import com.humanitarian.util.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonationDAO {
    public boolean createDonation(Donation donation) {
        String sql = "INSERT INTO donations (request_id, helper_id, donation_type, amount, item_description, service_description, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, donation.getRequestId());
            pstmt.setInt(2, donation.getHelperId());
            pstmt.setString(3, donation.getDonationType());
            if (donation.getAmount() != null) {
                pstmt.setBigDecimal(4, donation.getAmount());
            } else {
                pstmt.setNull(4, Types.DECIMAL);
            }
            pstmt.setString(5, donation.getItemDescription());
            pstmt.setString(6, donation.getServiceDescription());
            pstmt.setString(7, donation.getNotes());
            
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    donation.setDonationId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating donation: " + e.getMessage());
        }
        return false;
    }

    public List<Donation> getDonationsByHelper(int helperId) {
        List<Donation> donations = new ArrayList<>();
        String sql = "SELECT d.*, hr.title, hr.description FROM donations d " +
                     "LEFT JOIN help_requests hr ON d.request_id = hr.request_id " +
                     "WHERE d.helper_id = ? ORDER BY d.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, helperId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                donations.add(mapResultSetToDonation(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting donations: " + e.getMessage());
        }
        return donations;
    }

    public List<Donation> getDonationsByRequest(int requestId) {
        List<Donation> donations = new ArrayList<>();
        String sql = "SELECT d.*, u.full_name, u.email FROM donations d " +
                     "LEFT JOIN users u ON d.helper_id = u.user_id " +
                     "WHERE d.request_id = ? ORDER BY d.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, requestId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                donations.add(mapResultSetToDonation(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting donations: " + e.getMessage());
        }
        return donations;
    }

    public boolean updateDonationStatus(int donationId, String status) {
        String sql = "UPDATE donations SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE donation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, donationId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating donation status: " + e.getMessage());
            return false;
        }
    }

    private Donation mapResultSetToDonation(ResultSet rs) throws SQLException {
        Donation donation = new Donation();
        donation.setDonationId(rs.getInt("donation_id"));
        donation.setRequestId(rs.getInt("request_id"));
        donation.setHelperId(rs.getInt("helper_id"));
        donation.setDonationType(rs.getString("donation_type"));
        BigDecimal amount = rs.getBigDecimal("amount");
        if (amount != null) {
            donation.setAmount(amount);
        }
        donation.setItemDescription(rs.getString("item_description"));
        donation.setServiceDescription(rs.getString("service_description"));
        donation.setStatus(rs.getString("status"));
        donation.setNotes(rs.getString("notes"));
        donation.setCreatedAt(rs.getTimestamp("created_at"));
        donation.setUpdatedAt(rs.getTimestamp("updated_at"));
        return donation;
    }
}

