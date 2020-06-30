package com.example.hostelers.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hostelers.backend.PaymentListItemResult;
import com.example.hostelers.backend.RetrofitInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoarderPreviousPaymentsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<PaymentListItemResult>> data = new MutableLiveData<>();
    private final String BASE_URL = "http://10.0.2.2:3000";

    public LiveData<ArrayList<PaymentListItemResult>> getData(){
        return data;
    }

    public void setData(String hostelName, String hostelLocation, String boarderId){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ArrayList<PaymentListItemResult>> call = retrofitInterface.getBoarderPreviousPayments(hostelName, hostelLocation, boarderId);
        call.enqueue(new Callback<ArrayList<PaymentListItemResult>>() {
            @Override
            public void onResponse(Call<ArrayList<PaymentListItemResult>> call, Response<ArrayList<PaymentListItemResult>> response) {
                int response_code = response.code();
                if(response_code == 200){
                    ArrayList<PaymentListItemResult> paymentList = response.body();
                    data.setValue(paymentList);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PaymentListItemResult>> call, Throwable t) {
                System.out.println("Connecting to server failed!");
            }
        });
    }
}
