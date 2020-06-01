package com.example.hostelers.ui;

import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hostelers.backend.HostelBoardersListItemResult;
import com.example.hostelers.backend.RetrofitInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataFetchViewModel extends ViewModel {
    private MutableLiveData<List<HostelBoardersListItemResult>> listData = new MutableLiveData<>();
    private String BASE_URL = "http://10.0.2.2:3000";
    public LiveData<List<HostelBoardersListItemResult>> getData(){
        return listData;
    }

    public void setData(String id, String hostelName, String hostelLocation){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<List<HostelBoardersListItemResult>> call = retrofitInterface.executeFetchBoarderList(hostelName, hostelLocation, id);
        call.enqueue(new Callback<List<HostelBoardersListItemResult>>() {
            @Override
            public void onResponse(Call<List<HostelBoardersListItemResult>> call, Response<List<HostelBoardersListItemResult>> response) {
                int response_code = response.code();
                if(response_code == 200){
                    List<HostelBoardersListItemResult> result = response.body();
                    listData.setValue(result);
                }
                else if(response_code == 404){
                    System.out.println("Empty List");
                }
            }

            @Override
            public void onFailure(Call<List<HostelBoardersListItemResult>> call, Throwable t) {
                System.out.println("Connect Failed!");
            }
        });
    }
}
