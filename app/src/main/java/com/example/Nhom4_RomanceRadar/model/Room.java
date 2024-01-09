package com.example.Nhom4_RomanceRadar.model;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private String roomId;
    private Map<String, Message> messages;
    private String user1;
    private String user2;
    private String lastMess;

    public Room() {
    }

    public Room(String roomId, Map<String, Message> messages, String user1, String user2, String lastMess) {
        this.roomId = roomId;
        this.messages = messages;
        this.user1 = user1;
        this.user2 = user2;
        this.lastMess = lastMess;
    }

    public String getRoomId() {
        return roomId;
    }

    public Map<String, Message> getMessages() {
        return messages;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getLastMess() {
        return lastMess;
    }

    public void setLastMess(String lastMess) {
        this.lastMess = lastMess;
    }
}
