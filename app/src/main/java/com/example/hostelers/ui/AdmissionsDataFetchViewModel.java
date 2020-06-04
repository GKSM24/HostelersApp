package com.example.hostelers.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hostelers.backend.AdmissionsResult;
import com.example.hostelers.backend.RetrofitInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdmissionsDataFetchViewModel extends ViewModel {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";
    private MutableLiveData<ArrayList<AdmissionsResult>> data;

    public AdmissionsDataFetchViewModel(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        data = new MutableLiveData<>();
    }

    public LiveData<ArrayList<AdmissionsResult>> getData(){
        return data;
    }

    public void setData(String hostelName, String hostelLocation, String wardenId){
        Call<ArrayList<AdmissionsResult>> call = retrofitInterface.getAdmissionsList(hostelName, hostelLocation, wardenId);
        call.enqueue(new Callback<ArrayList<AdmissionsResult>>() {
            @Override
            public void onResponse(Call<ArrayList<AdmissionsResult>> call, Response<ArrayList<AdmissionsResult>> response) {
                if(response.code() == 200){
                    ArrayList<AdmissionsResult> list = response.body();
                    data.setValue(list);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AdmissionsResult>> call, Throwable t) {
                System.out.println("Connection Failure");
            }
        });
    }

}
