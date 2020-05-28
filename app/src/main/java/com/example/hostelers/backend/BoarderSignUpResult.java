package com.example.hostelers.backend;

public class BoarderSignUpResult {
    private String boarderName, wardenName, hostelName, boarderEmail, wardenEmail;
    private String wardenMobile, boarderId;

    public String getBoarderName() {
        return boarderName;
    }

    public String getWardenEmail() {
        return wardenEmail;
    }

    public String getBoarderEmail() {
        return boarderEmail;
    }

    public String getWardenName() {
        return wardenName;
    }

    public String getHostelName() {
        return hostelName;
    }

    public String getWardenMobile() {
        return wardenMobile;
    }

    public String getBoarderId() {
        return boarderId;
    }
}
