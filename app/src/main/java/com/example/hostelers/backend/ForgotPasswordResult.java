package com.example.hostelers.backend;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordResult {
    @SerializedName("wardenPassword")
    private String password;

    @SerializedName("wardenEmail")
    private String email;

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
