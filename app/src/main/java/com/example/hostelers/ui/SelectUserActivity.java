package com.example.hostelers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hostelers.R;

public class SelectUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
    }

    public void gotoAdminSignIn(View v){
        Intent intent = new Intent(this, AdminLoginActivity.class), fromIntent = getIntent();
        intent.putExtra("hostelName", fromIntent.getStringExtra("hostelName"));
        intent.putExtra("hostelLocation", fromIntent.getStringExtra("hostelLocation"));
        startActivity(intent);
    }

    public void gotoBoarderSignIn(View v){
        Intent intent = new Intent(this, BoarderSignInActivity.class), fromIntent = getIntent();
        intent.putExtra("hostelName", fromIntent.getStringExtra("hostelName"));
        intent.putExtra("hostelLocation", fromIntent.getStringExtra("hostelLocation"));
        startActivity(intent);
    }
}
