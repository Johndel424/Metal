package com.example.metal.Borrower.Chat_Feature;


public class MessageModel {
    private String senderProfileImg;
    private String senderUid;
    private String senderUsername;
    private String text;
    private String key;
    private long timestamp;
    private String messageType;
    private String unread;

    public MessageModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    }

    public MessageModel(String senderProfileImg, String senderUid, String senderUsername, String text, long timestamp, String messageType) {
        this.senderProfileImg = senderProfileImg;
        this.senderUid = senderUid;
        this.senderUsername = senderUsername;
        this.text = text;
        this.timestamp = timestamp;
        this.messageType = messageType;
        this.unread = "unread";
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    // Add getters and setters as needed
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Example:
    public String getSenderProfileImg() {
        return senderProfileImg;
    }

    public void setSenderProfileImg(String senderProfileImg) {
        this.senderProfileImg = senderProfileImg;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }
// Repeat for other fields
}
