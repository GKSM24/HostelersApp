package com.example.hostelers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SelectUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
    }

    public void gotoAdminSignIn(View v){
        Intent intent = new Intent(this, AdminLoginActivity.class);
        startActivity(intent);
    }

    public void gotoBoarderSignIn(View v){
        Intent intent = new Intent(this, BoarderSignInActivity.class);
        startActivity(intent);
    }
}
