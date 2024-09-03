package com.petopia.loginsignup;

public class ChatMessage {
    private String message;
    private boolean sentByCurrentUser;
    private String timestamp;

    public ChatMessage(String message, boolean sentByCurrentUser, String timestamp) {
        this.message = message;
        this.sentByCurrentUser = sentByCurrentUser;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSentByCurrentUser() {
        return sentByCurrentUser;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
