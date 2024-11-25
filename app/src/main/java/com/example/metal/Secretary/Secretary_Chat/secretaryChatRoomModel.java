package com.example.metal.Secretary.Secretary_Chat;

public class secretaryChatRoomModel {
    private String borrowerProfile;

    private String borrowerName;
    private String borrowerId;
    private String secretaryProfile;
    private String secretaryName;

    private String secretaryId;
    private String key;
    private String lastMessage;
    private String statusSecretary;
    private String statusBorrower;
    private Long lastMessageTimestamp;
    private String lastMessageType;

    public String getBorrowerProfile() {
        return borrowerProfile;
    }

    public void setBorrowerProfile(String borrowerProfile) {
        this.borrowerProfile = borrowerProfile;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getSecretaryProfile() {
        return secretaryProfile;
    }

    public void setSecretaryProfile(String secretaryProfile) {
        this.secretaryProfile = secretaryProfile;
    }

    public String getSecretaryName() {
        return secretaryName;
    }

    public void setSecretaryName(String secretaryName) {
        this.secretaryName = secretaryName;
    }

    public String getSecretaryId() {
        return secretaryId;
    }

    public void setSecretaryId(String secretaryId) {
        this.secretaryId = secretaryId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Long  lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getStatusSecretary() {
        return statusSecretary;
    }

    public void setStatusSecretary(String statusSecretary) {
        this.statusSecretary = statusSecretary;
    }

    public String getStatusBorrower() {
        return statusBorrower;
    }

    public void setStatusBorrower(String statusBorrower) {
        this.statusBorrower = statusBorrower;
    }

    public String getLastMessageType() {
        return lastMessageType;
    }

    public void setLastMessageType(String lastMessageType) {
        this.lastMessageType = lastMessageType;
    }

    public secretaryChatRoomModel() {
        // Default constructor required for Firebase Realtime Database
    }

}



