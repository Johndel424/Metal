package com.example.metal.Secretary.Secretary_Approval.Secretary_LoanApproval;

public class SecretaryLoanModel {
    private String loanId;
    private double loanAmount;
    private double interestAmount;
    private double totalRepayment;
    private double dailyInstallment;
    private String userId;
    private String status;
    private String rejectedReason;
    private double totalRepaymentBalance;
    private String userProfile;
    private String userAddress;
    private String userName;
    private long timestamp;

    // Constructor
    public SecretaryLoanModel() {
        // Default constructor required for Firebase
    }

    public String getUserProfile() {
        return userProfile;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getters and setters (can be generated automatically by IDE)
    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public double getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(double totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    public double getDailyInstallment() {
        return dailyInstallment;
    }

    public void setDailyInstallment(double dailyInstallment) {
        this.dailyInstallment = dailyInstallment;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalRepaymentBalance() {
        return totalRepaymentBalance;
    }

    public void setTotalRepaymentBalance(double totalRepaymentBalance) {
        this.totalRepaymentBalance = totalRepaymentBalance;
    }
}
