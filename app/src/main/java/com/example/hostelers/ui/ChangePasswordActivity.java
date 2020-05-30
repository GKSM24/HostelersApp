package com.example.hostelers.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelers.R;
import com.example.hostelers.backend.RetrofitInterface;

import java.util.HashMap;
import java.util.regex.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChangePasswordActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";
    private Intent fromIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        fromIntent = getIntent();
        final TextView id = findViewById(R.id.etId);
        id.setText(fromIntent.getStringExtra("id"));
        final EditText new_password = findViewById(R.id.etPwd), confirm_password = findViewById(R.id.etRe_enterPwd);
        Button submit = findViewById(R.id.btnSubmit);
        MyEditTextListener myTextListener = new MyEditTextListener();
        id.setOnEditorActionListener(myTextListener);
        new_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String new_password_text = new_password.getText().toString();
                if (new_password_text.isEmpty()) {
                    new_password.setError("Mandatory: Can't be Empty.");
                } else{
                    int pwd_len = new_password_text.length();
                    if(pwd_len < 8){
                        new_password.setError("Mandatory: The number of characters used must be at least 8 and less than 16");
                    } else{
                        if(!Pattern.matches("^[A-Z][\\w@!%$]{7,15}",new_password_text)){
                            new_password.setError("Mandatory:\nThe password must start with a Upper Case alphabet\nIt can contain only letters, digits and special characters like @, !, % and $");
                        }
                    }
                }
                return true;
            }
        });
        confirm_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            String confirm_pwd_text = confirm_password.getText().toString();
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (confirm_pwd_text.isEmpty()) {
                    confirm_password.setError("Mandatory: Can't be Empty.");
                }else{
                    if(!new_password.getText().toString().equals(confirm_pwd_text)){
                        confirm_password.setError("Mandatory: Password not matching!");
                    }
                }
                return true;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                String id_text = id.getText().toString(), new_password_text = new_password.getText().toString(), confirm_pwd_text = confirm_password.getText().toString();
                String userType = fromIntent.getStringExtra("userType"), location = fromIntent.getStringExtra("hostelLocation"), h_name = fromIntent.getStringExtra("hostelName");
                if (new_password_text.isEmpty()) {
                    new_password.setError("Mandatory: Can't be Empty.");
                    flag = false;
                } else{
                    int pwd_len = new_password_text.length();
                    if(pwd_len < 8){
                        new_password.setError("Mandatory: The number of characters used must be at least 8 and less than 16");
                        flag = false;
                    } else{
                        if(!Pattern.matches("^[A-Z][\\w@!%$]{7,15}",new_password_text)){
                            new_password.setError("Mandatory:\nThe password must with a Upper Case alphabet\nIt can contain only letters, digits and special characters like @, !, % and $");
                            flag = false;
                        }
                    }
                }
                if (confirm_pwd_text.isEmpty()) {
                    confirm_password.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }else{
                    if(!new_password_text.equals(confirm_pwd_text)){
                        confirm_password.setError("Mandatory: Password not matching!");
                        flag = false;
                    }
                }
                if(flag){
                    HashMap<String, String> details = new HashMap<>();
                    Call<Void> call;
                    details.put("id", id_text);
                    details.put("newPassword", new_password_text);
                    details.put("hostelLocation", location);
                    details.put("hostelName", h_name);
                    if(userType.equalsIgnoreCase("boarder")){
                        call =  retrofitInterface.executeBoarderChangePassword(details);
                    }
                    else{
                        call =  retrofitInterface.executeWardenChangePassword(details);
                    }
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int response_code = response.code();
                            if(response_code == 200) {
                                Toast.makeText(getApplicationContext(), "Password Change Successful!", Toast.LENGTH_LONG).show();
                                finish(); // also helps us to go back to previous activity
                            }
                            else if(response_code == 404)
                                openAlertDialog("Id not found! Please sign up before you change password.");
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            openAlertDialog("Connection Error! Try Again!");
                        }
                    });
                }
            }
        });
    }

    private void openAlertDialog(String message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Error").setMessage(message).setNeutralButton("OK,", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //empty
            }
        });
    }
}
