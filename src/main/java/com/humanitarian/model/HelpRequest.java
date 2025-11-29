package com.humanitarian.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class HelpRequest {
    private int requestId;
    private int survivorId;
    private String title;
    private String description;
    private String helpType; // money, food, clothing, shelter, medical, education, other
    private BigDecimal amountNeeded;
    private String location;
    private String status; // pending, approved, received, rejected
    private boolean isVerified;
    private String adminNotes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<Document> documents;
    private User survivor;
    private int donationCount;

    public HelpRequest() {}

    public HelpRequest(int survivorId, String title, String description, String helpType, String location) {
        this.survivorId = survivorId;
        this.title = title;
        this.description = description;
        this.helpType = helpType;
        this.location = location;
        this.status = "pending";
    }

    // Getters and Setters
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public int getSurvivorId() { return survivorId; }
    public void setSurvivorId(int survivorId) { this.survivorId = survivorId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getHelpType() { return helpType; }
    public void setHelpType(String helpType) { this.helpType = helpType; }

    public BigDecimal getAmountNeeded() { return amountNeeded; }
    public void setAmountNeeded(BigDecimal amountNeeded) { this.amountNeeded = amountNeeded; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public String getAdminNotes() { return adminNotes; }
    public void setAdminNotes(String adminNotes) { this.adminNotes = adminNotes; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public List<Document> getDocuments() { return documents; }
    public void setDocuments(List<Document> documents) { this.documents = documents; }

    public User getSurvivor() { return survivor; }
    public void setSurvivor(User survivor) { this.survivor = survivor; }

    public int getDonationCount() { return donationCount; }
    public void setDonationCount(int donationCount) { this.donationCount = donationCount; }
}

