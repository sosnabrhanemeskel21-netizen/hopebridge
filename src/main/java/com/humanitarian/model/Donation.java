package com.humanitarian.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Donation {
    private int donationId;
    private int requestId;
    private int helperId;
    private String donationType; // money, item, service
    private BigDecimal amount;
    private String itemDescription;
    private String serviceDescription;
    private String status; // pending, confirmed, delivered, cancelled
    private String notes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private HelpRequest helpRequest;
    private User helper;

    public Donation() {}

    public Donation(int requestId, int helperId, String donationType) {
        this.requestId = requestId;
        this.helperId = helperId;
        this.donationType = donationType;
        this.status = "pending";
    }

    // Getters and Setters
    public int getDonationId() { return donationId; }
    public void setDonationId(int donationId) { this.donationId = donationId; }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public int getHelperId() { return helperId; }
    public void setHelperId(int helperId) { this.helperId = helperId; }

    public String getDonationType() { return donationType; }
    public void setDonationType(String donationType) { this.donationType = donationType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getItemDescription() { return itemDescription; }
    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }

    public String getServiceDescription() { return serviceDescription; }
    public void setServiceDescription(String serviceDescription) { this.serviceDescription = serviceDescription; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public HelpRequest getHelpRequest() { return helpRequest; }
    public void setHelpRequest(HelpRequest helpRequest) { this.helpRequest = helpRequest; }

    public User getHelper() { return helper; }
    public void setHelper(User helper) { this.helper = helper; }
}

