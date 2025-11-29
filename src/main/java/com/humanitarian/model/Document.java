package com.humanitarian.model;

import java.sql.Timestamp;

public class Document {
    private int documentId;
    private int requestId;
    private String documentType; // id, displacement_certificate, humanitarian_card, other
    private String fileName;
    private String filePath;
    private long fileSize;
    private String mimeType;
    private boolean isVerified;
    private Integer verifiedBy;
    private String verificationNotes;
    private Timestamp uploadedAt;
    private Timestamp verifiedAt;

    public Document() {}

    public Document(int requestId, String documentType, String fileName, String filePath) {
        this.requestId = requestId;
        this.documentType = documentType;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    // Getters and Setters
    public int getDocumentId() { return documentId; }
    public void setDocumentId(int documentId) { this.documentId = documentId; }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public Integer getVerifiedBy() { return verifiedBy; }
    public void setVerifiedBy(Integer verifiedBy) { this.verifiedBy = verifiedBy; }

    public String getVerificationNotes() { return verificationNotes; }
    public void setVerificationNotes(String verificationNotes) { this.verificationNotes = verificationNotes; }

    public Timestamp getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Timestamp uploadedAt) { this.uploadedAt = uploadedAt; }

    public Timestamp getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(Timestamp verifiedAt) { this.verifiedAt = verifiedAt; }
}

