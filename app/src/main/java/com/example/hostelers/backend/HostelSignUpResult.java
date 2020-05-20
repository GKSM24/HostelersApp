package com.example.hostelers.backend;

import com.google.gson.annotations.SerializedName;

public class HostelSignUpResult {
    private String wardenId;
    private String wardenKey;

    @SerializedName("wardenEmail")
    private String email;

    public String getEmail() {
        return email;
    }

    public String getWardenId() {
        return wardenId;
    }

    public String getWardenKey() {
        return wardenKey;
    }
}
