package com.example.hostelers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.hostelers.R;

// Here main activity is the splash screen.
public class MainActivity extends AppCompatActivity {

    private static final long SCREEN_TIME = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hides the action bar
        getSupportActionBar().hide();

        // this opens the Activity in full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // sets the main activity's layout
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run(){
                //creating explicit intent to start an activity after 2 seconds.
                Intent intent = new Intent(MainActivity.this, InitialActivity.class);

                startActivity(intent);

                //finished the current activity
                finish();
            }

        }, SCREEN_TIME);

    }
}
