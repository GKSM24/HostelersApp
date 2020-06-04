package com.example.hostelers.backend;

public class AdmissionsResult {
    private String boarderId, boarderName, fatherName, boarderJob, boarderMobile;
    private String idProof, photo;
    private String joiningDate, roomNumber, boarderEmail;

    public String getBoarderId() {
        return boarderId;
    }

    public String getBoarderEmail() {
        return boarderEmail;
    }

    public String getBoarderName() {
        return boarderName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getBoarderJob() {
        return boarderJob;
    }

    public String getBoarderMobile() {
        return boarderMobile;
    }

    public String getIdProof() {
        return idProof;
    }

    public String getPhoto() {
        return photo;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}
