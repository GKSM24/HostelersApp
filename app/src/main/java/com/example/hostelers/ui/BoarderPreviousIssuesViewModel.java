package com.example.hostelers.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hostelers.backend.PreviousIssuesResult;
import com.example.hostelers.backend.RetrofitInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoarderPreviousIssuesViewModel extends ViewModel {
    private String BASE_URL = "http://10.0.2.2:3000";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private MutableLiveData<ArrayList<PreviousIssuesResult>> liveData;

    public BoarderPreviousIssuesViewModel(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        liveData = new MutableLiveData<>();
    }

    public LiveData<ArrayList<PreviousIssuesResult>> getData(){ return liveData; }

    public void setData(String id, String hostelName, String hostelLocation){
        Call<ArrayList<PreviousIssuesResult>> call = retrofitInterface.getPreviousIssues(hostelName, hostelLocation, id);
        call.enqueue(new Callback<ArrayList<PreviousIssuesResult>>() {
            @Override
            public void onResponse(Call<ArrayList<PreviousIssuesResult>> call, Response<ArrayList<PreviousIssuesResult>> response) {
                if(response.code() == 200){
                    ArrayList<PreviousIssuesResult> list = response.body();
                    liveData.setValue(list);
                    System.out.println("load successful");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PreviousIssuesResult>> call, Throwable t) {
                System.out.println("Connection failure");
            }
        });
    }

}
