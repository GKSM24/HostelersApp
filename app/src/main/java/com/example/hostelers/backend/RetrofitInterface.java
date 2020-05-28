package com.example.hostelers.backend;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @FormUrlEncoded
    @POST("/hostelSignUp")
    Call<HostelSignUpResult> executeHostelSignUp(@FieldMap HashMap<String, String> signUpDetails);

    @FormUrlEncoded
    @POST("/boarderSignUp")
    Call<BoarderSignUpResult> executeBoarderSignUp(@FieldMap HashMap<String, String> boarderDetails);

    @POST("/wardenSignIn")
    Call<WardenSignInResult> executeWardenSignIn(@Body HashMap<String, String> wardenCredentials);

    @POST("/boarderSignIn")
    Call<BoarderSignInResult> executeBoarderSignIn(@Body HashMap<String, String> boarderCredentials);

    @PUT("/change_password")
    Call<Void> executeChangePassword(@Body HashMap<String, String> passwordDetails);

    @POST("/hostel_list")
    Call<List<HostelListItem>> executeGetHostelList();

    @GET("/wardenSignIn/{hostelLocation}/{hostelName}/{wardenId}")
    Call<ForgotPasswordResult> executeWardenForgotPassword(@Path("hostelLocation") String location, @Path("hostelName") String name, @Path("wardenId") String id);

    @GET("/boarderSignIn/{hostelLocation}/{hostelName}/{boarderId}")
    Call<ForgotPasswordResult> executeBoarderForgotPassword(@Path("hostelLocation") String location, @Path("hostelName") String name, @Path("boarderId") String id);
}
