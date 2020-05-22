package com.example.hostelers.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.example.hostelers.R;
import com.example.hostelers.backend.HostelListItem;
import com.example.hostelers.backend.RetrofitInterface;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HostelListActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";
    private ArrayList<RadioButton> radioButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_list);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<List<HostelListItem>> call = retrofitInterface.executeGetHostelList();
        call.enqueue(new Callback<List<HostelListItem>>() {
            @Override
            public void onResponse(Call<List<HostelListItem>> call, Response<List<HostelListItem>> response) {
                int response_code = response.code();
                if(response_code == 200){
                    List<HostelListItem> resultList = response.body();
                    RadioGroup buttonGroup = findViewById(R.id.hostel_list_radio_group);
                    radioButtons = new ArrayList<>();
                    for(HostelListItem item:resultList){
                        RadioButton button = new RadioButton(getApplicationContext());
                        button.setText(item.getHostelName()+", "+item.getHostelLocation());
                        button.setTextSize(26);
                        button.setPadding(16,16,16,16);
                        buttonGroup.addView(button);
                        radioButtons.add(button);
                    }
                }
                else if(response_code == 404){
                    openAlertDialog("List Empty!");
                }
            }

            @Override
            public void onFailure(Call<List<HostelListItem>> call, Throwable t) {
                openAlertDialog("Couldn't load hostel list!");
            }
        });
    }

    public void onSubmit(View view){
        boolean isChecked = false;
        String buttonText="";
        for(RadioButton button:radioButtons){
            if(button.isChecked()){
                buttonText = button.getText().toString();
                isChecked = true;
                break;
            }
        }
        if(isChecked) {
            String details[] = buttonText.split(", ");
            Intent intent = new Intent(this, SelectUserActivity.class);
            intent.putExtra("hostelName",details[0]);
            intent.putExtra("hostelLocation", details[1]);
            startActivity(intent);
        }
        else{
            Snackbar.make(findViewById(R.id.hostel_list_root_layout),"Please Select a Hostel!", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void openAlertDialog(String message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Error!").setMessage(message).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //empty
            }
        }).show();
    }
}
