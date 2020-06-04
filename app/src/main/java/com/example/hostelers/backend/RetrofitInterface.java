package com.example.hostelers.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @PUT("/boarder/change_password")
    Call<Void> executeBoarderChangePassword(@Body HashMap<String, String> passwordDetails);

    @PUT("/warden/change_password")
    Call<Void> executeWardenChangePassword(@Body HashMap<String, String> passwordDetails);

    @POST("/hostel_list")
    Call<List<HostelListItem>> executeGetHostelList();

    @GET("/wardenSignIn/{hostelLocation}/{hostelName}/{wardenId}")
    Call<ForgotPasswordResult> executeWardenForgotPassword(@Path("hostelLocation") String location, @Path("hostelName") String name, @Path("wardenId") String id);

    @GET("/boarderSignIn/{hostelLocation}/{hostelName}/{boarderId}")
    Call<ForgotPasswordResult> executeBoarderForgotPassword(@Path("hostelLocation") String location, @Path("hostelName") String name, @Path("boarderId") String id);

    @GET("/warden/boarder_list/{hostelName}/{hostelLocation}")
    Call<List<HostelBoardersListItemResult>> executeFetchBoarderList( @Path("hostelName") String name, @Path("hostelLocation") String location);

    @PUT("/notify_boarder")
    Call<Void> executeNotifyBoarder(@Body HashMap<String, String> msgDetails);

    @GET("/boarder_notifications/{hostelName}/{hostelLocation}/{boarderId}")
    Call<List<BoarderNotificationItemResult>> getBoarderNotifications(@Path("hostelName") String hostelName, @Path("hostelLocation") String hostelLocation, @Path("boarderId") String boarderId);

    @DELETE("/boarder_notifications/delete_notification/{hostelName}/{hostelLocation}/{boarderId}/{notification_position}")
    Call<Void> deleteBoarderNotification(@Path("hostelName") String hostelName, @Path("hostelLocation") String hostelLocation, @Path("boarderId") String boarderId, @Path("notification_position") int position);

    @GET("/warden/boarder_admissions/{hostelName}/{hostelLocation}/{wardenId}")
    Call<ArrayList<AdmissionsResult>> getAdmissionsList(@Path("hostelName") String hostelName, @Path("hostelLocation") String hostelLocation, @Path("wardenId") String wardenId);

    @PUT("/warden/boarder_allocate_room")
    Call<Void> updateRoomStatusForBoarder(@Body HashMap<String, String> details);
}
