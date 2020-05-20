package com.example.hostelers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hostelers.R;

import java.util.HashMap;

public class BoarderSignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarder_sign_in);
        final EditText user_id = findViewById(R.id.etBId), pwd = findViewById(R.id.etBPwd);
        Button signIn = findViewById(R.id.btnBSignIn), signUp = findViewById(R.id.btnBSignUp), forgot_pwd = findViewById(R.id.btnBForgotPwd);
        MyEditTextListener myTextListener = new MyEditTextListener();
        user_id.setOnEditorActionListener(myTextListener);
        pwd.setOnEditorActionListener(myTextListener);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                if (user_id.getText().toString().isEmpty()) {
                    user_id.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if (pwd.getText().toString().isEmpty()) {
                    pwd.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if (flag) {
                    /*Intent fromIntent = getIntent();
                    HashMap<String, String> credentials = new HashMap<>();
                    credentials.put("hostelName", fromIntent.getStringExtra("hostelName"));
                    credentials.put("hostelLocation", fromIntent.getStringExtra("hostelLocation"));*/
                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BoarderSignInActivity.this, BoarderActivity.class);
                    startActivity(intent);
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoarderSignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoarderSignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
