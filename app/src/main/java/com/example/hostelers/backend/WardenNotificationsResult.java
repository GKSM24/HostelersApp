package com.example.hostelers.backend;

public class WardenNotificationsResult {
    private String boarderId, issueType, typeCategory, issueDescription, roomNumber;

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getBoarderId() {
        return boarderId;
    }

    public String getIssueType() {
        return issueType;
    }

    public String getTypeCategory() {
        return typeCategory;
    }

    public String getIssueDescription() {
        return issueDescription;
    }
}
