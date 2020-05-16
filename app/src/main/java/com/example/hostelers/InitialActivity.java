package com.example.hostelers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
    }

    public void gotoHostelList(View view){
        Intent intent = new Intent(this, HostelListActivity.class);
        startActivity(intent);
    }

    public void gotoHostelSignUp(View view){
        Intent intent = new Intent(this, HostelSignUpActivity.class);
        startActivity(intent);
    }
}
