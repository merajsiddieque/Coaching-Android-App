package com.app.checking.ui.messages_Student;

public class Message {
    private String message;
    private String dateTime;

    // Constructor
    public Message(String message, String dateTime) {
        this.message = message;
        this.dateTime = dateTime;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}

