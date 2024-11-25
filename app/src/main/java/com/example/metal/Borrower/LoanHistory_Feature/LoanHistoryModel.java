package com.example.metal.Borrower.LoanHistory_Feature;

public class LoanHistoryModel {
    private String loanId;
    private String userId;
    private String userName;
    private String userAddress;
    private String userProfile;
    private String status;
    private long startDate;
    private long endDate;
    private long timestamp;
    private String approvedStartDate;  // Changed to long for consistency with date handling
    private String approvedEndDate;    // Changed to long for consistency with date handling
    private double loanAmount;
    private double interestAmount;
    private double totalRepayment;
    private double totalRepaymentBalance;
    private double totalPayoutAmount;
    private double dailyInstallment;

    // Empty constructor (required for Firebase)
    public LoanHistoryModel() {}



    // Getters and Setters
    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getApprovedStartDate() {
        return approvedStartDate;
    }

    public void setApprovedStartDate(String approvedStartDate) {
        this.approvedStartDate = approvedStartDate;
    }

    public String getApprovedEndDate() {
        return approvedEndDate;
    }

    public void setApprovedEndDate(String approvedEndDate) {
        this.approvedEndDate = approvedEndDate;
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

    public double getTotalRepaymentBalance() {
        return totalRepaymentBalance;
    }

    public void setTotalRepaymentBalance(double totalRepaymentBalance) {
        this.totalRepaymentBalance = totalRepaymentBalance;
    }

    public double getTotalPayoutAmount() {
        return totalPayoutAmount;
    }

    public void setTotalPayoutAmount(double totalPayoutAmount) {
        this.totalPayoutAmount = totalPayoutAmount;
    }

    public double getDailyInstallment() {
        return dailyInstallment;
    }

    public void setDailyInstallment(double dailyInstallment) {
        this.dailyInstallment = dailyInstallment;
    }
}

