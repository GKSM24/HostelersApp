package com.example.hostelers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hostelers.R;
import com.example.hostelers.backend.BoarderSignInResult;
import com.example.hostelers.backend.BoarderSignUpResult;
import com.example.hostelers.backend.ForgotPasswordResult;
import com.example.hostelers.backend.RetrofitInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoarderSignInActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarder_sign_in);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        final EditText user_id = findViewById(R.id.etBId), pwd = findViewById(R.id.etBPwd);
        Button signIn = findViewById(R.id.btnBSignIn), signUp = findViewById(R.id.btnBSignUp), forgot_pwd = findViewById(R.id.btnBForgotPwd);
        MyEditTextListener myTextListener = new MyEditTextListener();
        user_id.setOnEditorActionListener(myTextListener);
        pwd.setOnEditorActionListener(myTextListener);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                String username = user_id.getText().toString(), password = pwd.getText().toString();
                if (username.isEmpty()) {
                    user_id.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if (password.isEmpty()) {
                    pwd.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if (flag) {
                    validateCredentials(username, password);
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoarderSignInActivity.this, SignUpActivity.class), fromIntent = getIntent();
                intent.putExtra("hostelName", fromIntent.getStringExtra("hostelName"));
                intent.putExtra("hostelLocation", fromIntent.getStringExtra("hostelLocation"));
                startActivity(intent);
            }
        });
        forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                String username = user_id.getText().toString();
                if (username.isEmpty()) {
                    user_id.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if(flag){
                    Intent fromIntent = getIntent();
                    String hostelName = fromIntent.getStringExtra("hostelName"), hostelLocation = fromIntent.getStringExtra("hostelLocation");
                    Call<ForgotPasswordResult> call = retrofitInterface.executeBoarderForgotPassword(hostelLocation, hostelName,username);
                    call.enqueue(new Callback<ForgotPasswordResult>() {
                        @Override
                        public void onResponse(Call<ForgotPasswordResult> call, Response<ForgotPasswordResult> response) {
                            int response_code = response.code();
                            if(response_code == 200){
                                ForgotPasswordResult result = response.body();
                                sendMail(result.getEmail(),result.getPassword());
                                Toast.makeText(getApplicationContext(), "Mail will be sent to your registered email", Toast.LENGTH_LONG).show();
                            }
                            else if(response_code == 404){
                                Toast.makeText(getApplicationContext(), "Id not found!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ForgotPasswordResult> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void validateCredentials(String id, String pwd){
        Intent fromIntent = getIntent();
        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("hostelName", fromIntent.getStringExtra("hostelName"));
        credentials.put("hostelLocation", fromIntent.getStringExtra("hostelLocation"));
        credentials.put("boarderId", id);
        credentials.put("boarderPassword", pwd);
        Call<BoarderSignInResult> call = retrofitInterface.executeBoarderSignIn(credentials);
        call.enqueue(new Callback<BoarderSignInResult>() {
            @Override
            public void onResponse(Call<BoarderSignInResult> call, Response<BoarderSignInResult> response) {
                int response_code = response.code();
                if(response_code == 200){
                    BoarderSignInResult result = response.body();
                    Intent intent = new Intent(BoarderSignInActivity.this, BoarderActivity.class);
                    intent.putExtra("boarderName", result.getBoarderName());
                    intent.putExtra("boarderId", result.getBoarderId());
                    intent.putExtra("boarderPhoto", result.getBoarderPhoto());
                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else if(response_code == 404){
                    Toast.makeText(getApplicationContext(), "Invalid credentials. Please try again!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BoarderSignInResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendMail(String emailId, String password){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailId});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Forgot Password Request Hostelers");
        intent.putExtra(Intent.EXTRA_TEXT, "Your password: "+password);
        startActivity(intent);
    }
}
