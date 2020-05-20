package com.example.hostelers.backend;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("/hostelSignUp")
    Call<HostelSignUpResult> executeHostelSignUp(@Body HashMap<String, String> signUpDetails);

    @POST("/wardenSignIn")
    Call<WardenSignInResult> executeWardenSignIn(@Body HashMap<String, String> wardenCredentials);

    @POST("/forgot_password")
    Call<Void> executeForgotPassword(@Body HashMap<String, String> passwordDetails);

    @POST("/hostel_list")
    Call<List<HostelListItem>> executeGetHostelList();
}
