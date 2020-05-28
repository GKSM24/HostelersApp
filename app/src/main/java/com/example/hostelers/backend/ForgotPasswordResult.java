package com.example.hostelers.backend;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordResult {
    private String password;
    private String email;

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
