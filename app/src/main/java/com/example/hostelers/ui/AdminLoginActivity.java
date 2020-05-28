package com.example.hostelers.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hostelers.R;
import com.example.hostelers.backend.ForgotPasswordResult;
import com.example.hostelers.backend.RetrofitInterface;
import com.example.hostelers.backend.WardenSignInResult;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminLoginActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        final EditText userName = findViewById(R.id.WardenUsernameID), password = findViewById(R.id.WardenPasswordId);
        Button signIn = findViewById(R.id.WardenSignInButtonId), forgot_password = findViewById(R.id.WardenForgotPwdId);
        MyEditTextListener myTextListener = new MyEditTextListener();
        userName.setOnEditorActionListener(myTextListener);
        password.setOnEditorActionListener(myTextListener);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                String id = userName.getText().toString(), pwd = password.getText().toString();
                if (id.isEmpty()) {
                    userName.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if (pwd.isEmpty()) {
                    password.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if(flag){
                    Intent fromIntent = getIntent();
                    HashMap<String, String> credentials = new HashMap<>();
                    credentials.put("hostelName", fromIntent.getStringExtra("hostelName"));
                    credentials.put("hostelLocation", fromIntent.getStringExtra("hostelLocation"));
                    credentials.put("wardenId", id);
                    credentials.put("wardenPassword", pwd);
                    Call<WardenSignInResult> call = retrofitInterface.executeWardenSignIn(credentials);
                    call.enqueue(new Callback<WardenSignInResult>() {
                        @Override
                        public void onResponse(Call<WardenSignInResult> call, Response<WardenSignInResult> response) {
                            int response_code = response.code();
                            if(response_code == 200){
                                WardenSignInResult result = response.body();
                                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AdminLoginActivity.this,WardenActivity.class);
                                intent.putExtra("warden_id", result.getWardenId());
                                intent.putExtra("warden_name", result.getWardenName());
                                startActivity(intent);
                            }
                            else if(response_code == 404){
                                openAlertDialog("Incorrect credentials!\nEither Sign Up before logging in or Check your hostel Selection Again.");
                            }
                        }

                        @Override
                        public void onFailure(Call<WardenSignInResult> call, Throwable t) {
                            openAlertDialog("Connection Failure! Please Try Again!");
                        }
                    });
                }
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                String id = userName.getText().toString();
                if (id.isEmpty()) {
                    Snackbar.make(userName, "Enter the Id to get the Password!", Snackbar.LENGTH_LONG).show();
                    flag = false;
                }
                if(flag){
                    Intent fromIntent = getIntent();
                    String hostelName = fromIntent.getStringExtra("hostelName"), hostelLocation = fromIntent.getStringExtra("hostelLocation");
                    Call<ForgotPasswordResult> call = retrofitInterface.executeWardenForgotPassword(hostelLocation, hostelName, id);
                    call.enqueue(new Callback<ForgotPasswordResult>() {
                        @Override
                        public void onResponse(Call<ForgotPasswordResult> call, Response<ForgotPasswordResult> response) {
                            int response_code = response.code();
                            if(response_code == 200){
                                ForgotPasswordResult result = response.body();
                                sendMail(result.getEmail(), result.getPassword());
                                openAlertDialog("Current Password is sent to your Registered Email!");
                            }
                            else if(response_code == 404){
                                openAlertDialog("Id not found in the hostel your searching for.Sign up before you Sign In");
                            }
                        }

                        @Override
                        public void onFailure(Call<ForgotPasswordResult> call, Throwable t) {
                            openAlertDialog("Connection Failed!");
                        }
                    });
                }
            }
        });
    }


     private void openAlertDialog(String message){
         AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
         dialogBuilder.setMessage(message).setNeutralButton("OK", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 //empty
             }
         }).show();
     }

     private void sendMail(String email, String password){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hostelers Login Password");
        intent.putExtra(Intent.EXTRA_TEXT, "Your Password: "+password);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
     }
}
