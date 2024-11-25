package com.example.metal.Borrower.Notification_Feature;

public class NotificationModel {
    private String message;
    private String notificationId;
    private String remark;
    private long timestamp;
    private String userId;

    // Default constructor
    public NotificationModel() {
    }

    // Parameterized constructor
    public NotificationModel(String message, String notificationId, String remark, long timestamp, String userId) {
        this.message = message;
        this.notificationId = notificationId;
        this.remark = remark;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

