package com.example.hostelers.backend;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @POST("/hostelSignUp")
    Call<HostelSignUpResult> executeHostelSignUp(@Body HashMap<String, String> signUpDetails);

    @POST("/wardenSignIn")
    Call<WardenSignInResult> executeWardenSignIn(@Body HashMap<String, String> wardenCredentials);

    @POST("/change_password")
    Call<Void> executeChangePassword(@Body HashMap<String, String> passwordDetails);

    @POST("/hostel_list")
    Call<List<HostelListItem>> executeGetHostelList();

    @GET("/wardenSignIn/{hostelLocation}/{hostelName}/{wardenId}")
    Call<ForgotPasswordResult> executeForgotPassword(@Path("hostelLocation") String location, @Path("hostelName") String name, @Path("wardenId") String id);
}
