package com.example.hostelers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdminLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        final EditText userName = findViewById(R.id.WardenUsernameID), password = findViewById(R.id.WardenPasswordId);
        Button signIn = findViewById(R.id.WardenSignInButtonId);
        MyEditTextListener myTextListener = new MyEditTextListener();
        userName.setOnEditorActionListener(myTextListener);
        password.setOnEditorActionListener(myTextListener);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                if (userName.getText().toString().isEmpty()) {
                    userName.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if (password.getText().toString().isEmpty()) {
                    password.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if(flag){
                    Intent intent = new Intent(AdminLoginActivity.this,WardenActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
     public void gotoForgotPassword(View v){
         Intent intent = new Intent(this,ForgotPasswordActivity.class);
         intent.putExtras(intent);
         startActivity(intent);
     }
}
