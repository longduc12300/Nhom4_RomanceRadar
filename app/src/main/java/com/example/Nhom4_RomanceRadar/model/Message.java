package com.example.Nhom4_RomanceRadar.model;

import java.util.Map;

public class Message {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String content;
    private String user1;
    private String user2;
    private long timestamp;

    public Message(String messageId, String senderId, String receiverId, String content, long timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Message(String senderId, String receiverId, String content, long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Message() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public static Message fromMap(Map<String, Object> messageMap) {
        String content = (String) messageMap.get("content");
        String receiverId = (String) messageMap.get("receiverId");
        String senderId = (String) messageMap.get("senderId");
        long timestamp = (long) messageMap.get("timestamp");
        return new Message(content, receiverId, senderId, timestamp);
    }
}