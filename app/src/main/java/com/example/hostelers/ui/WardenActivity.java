package com.example.hostelers.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hostelers.R;
import com.google.android.material.tabs.TabLayout;

public class WardenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warden);
        Intent intent = getIntent();
        TextView tv1 = findViewById(R.id.user_name_welcome_text), tv2 = findViewById(R.id.user_id_text);
        tv2.setText("ID: "+intent.getStringExtra("warden_id"));
        tv1.setText("Welcome, "+ intent.getStringExtra("warden_name"));
        TabLayout tabs = findViewById(R.id.tabs_warden);
        ViewPager wardenViewPager = findViewById(R.id.viewpager_warden);
        setViewPager(wardenViewPager);
        tabs.setupWithViewPager(wardenViewPager);
    }

    public void setViewPager(ViewPager viewPager){
        WardenViewPagerAdapter adapter = new WardenViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(WardenNotificationsFragment.newInstance(), "Notifications");
        adapter.addFragment(WardenNewAdmissionsFragment.newInstance(), "New Admissions");
        adapter.addFragment(WardenNotifyBoarderFragment.newInstance(), "Notify Boarder");
        viewPager.setAdapter(adapter);
    }
}
