package com.humanitarian.dao;

import com.humanitarian.model.Document;
import com.humanitarian.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {
    public boolean saveDocument(Document document) {
        String sql = "INSERT INTO documents (request_id, document_type, file_name, file_path, file_size, mime_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, document.getRequestId());
            pstmt.setString(2, document.getDocumentType());
            pstmt.setString(3, document.getFileName());
            pstmt.setString(4, document.getFilePath());
            pstmt.setLong(5, document.getFileSize());
            pstmt.setString(6, document.getMimeType());
            
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    document.setDocumentId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving document: " + e.getMessage());
        }
        return false;
    }

    public List<Document> getDocumentsByRequestId(int requestId) {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE request_id = ? ORDER BY uploaded_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, requestId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                documents.add(mapResultSetToDocument(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting documents: " + e.getMessage());
        }
        return documents;
    }

    public Document getDocumentById(int documentId) {
        String sql = "SELECT * FROM documents WHERE document_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, documentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToDocument(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting document: " + e.getMessage());
        }
        return null;
    }

    public boolean verifyDocument(int documentId, boolean verified, int adminId, String notes) {
        String sql = "UPDATE documents SET is_verified = ?, verified_by = ?, verification_notes = ?, verified_at = CURRENT_TIMESTAMP WHERE document_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBoolean(1, verified);
            pstmt.setInt(2, adminId);
            pstmt.setString(3, notes);
            pstmt.setInt(4, documentId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error verifying document: " + e.getMessage());
            return false;
        }
    }

    public List<Document> getUnverifiedDocuments() {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE is_verified = FALSE ORDER BY uploaded_at ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                documents.add(mapResultSetToDocument(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting unverified documents: " + e.getMessage());
        }
        return documents;
    }

    private Document mapResultSetToDocument(ResultSet rs) throws SQLException {
        Document doc = new Document();
        doc.setDocumentId(rs.getInt("document_id"));
        doc.setRequestId(rs.getInt("request_id"));
        doc.setDocumentType(rs.getString("document_type"));
        doc.setFileName(rs.getString("file_name"));
        doc.setFilePath(rs.getString("file_path"));
        doc.setFileSize(rs.getLong("file_size"));
        doc.setMimeType(rs.getString("mime_type"));
        doc.setVerified(rs.getBoolean("is_verified"));
        Integer verifiedBy = rs.getInt("verified_by");
        if (!rs.wasNull()) {
            doc.setVerifiedBy(verifiedBy);
        }
        doc.setVerificationNotes(rs.getString("verification_notes"));
        doc.setUploadedAt(rs.getTimestamp("uploaded_at"));
        doc.setVerifiedAt(rs.getTimestamp("verified_at"));
        return doc;
    }
}

