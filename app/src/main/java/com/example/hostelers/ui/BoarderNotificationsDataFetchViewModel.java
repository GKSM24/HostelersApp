package com.example.hostelers.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hostelers.backend.BoarderNotificationItemResult;
import com.example.hostelers.backend.HostelBoardersListItemResult;
import com.example.hostelers.backend.RetrofitInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoarderNotificationsDataFetchViewModel extends ViewModel {
    private MutableLiveData<List<BoarderNotificationItemResult>> listData = new MutableLiveData<>();
    private String BASE_URL = "http://10.0.2.2:3000";

    public LiveData<List<BoarderNotificationItemResult>> getData(){
        return listData;
    }

    public void setData(String hostelName, String hostelLocation,String boarderId) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
       Call<List<BoarderNotificationItemResult>> call = retrofitInterface.getBoarderNotifications(hostelName, hostelLocation, boarderId);
        call.enqueue(new Callback<List<BoarderNotificationItemResult>>() {
            @Override
            public void onResponse(Call<List<BoarderNotificationItemResult>> call, Response<List<BoarderNotificationItemResult>> response) {
                int response_code = response.code();
                if(response_code == 200){
                    List<BoarderNotificationItemResult> result = response.body();
                    listData.setValue(result);
                }
                else if(response_code == 404){
                    System.out.println("Empty List");
                }
            }

            @Override
            public void onFailure(Call<List<BoarderNotificationItemResult>> call, Throwable t) {
                System.out.println("Connect Failed!");
            }
        });
    }
}
