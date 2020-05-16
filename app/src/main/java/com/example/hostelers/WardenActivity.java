package com.example.hostelers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

public class WardenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warden);
        TextView tv1 = findViewById(R.id.user_name_welcome_text), tv2 = findViewById(R.id.user_id_text);
        tv1.setText("Welcome, Admin");
        tv2.setText("Warden ID");
        TabLayout tabs = findViewById(R.id.tabs_warden);
        ViewPager wardenViewPager = findViewById(R.id.viewpager_warden);
        setViewPager(wardenViewPager);
        tabs.setupWithViewPager(wardenViewPager);
    }

    public void setViewPager(ViewPager viewPager){
        WardenAdapter adapter = new WardenAdapter(getSupportFragmentManager());
        adapter.addFragment(WardenNotificationsFragment.newInstance(), "Notifications");
        adapter.addFragment(WardenNewAdmissionsFragment.newInstance(), "New Admissions");
        adapter.addFragment(WardenNotifyBoarderFragment.newInstance(), "Notify Boarder");
        viewPager.setAdapter(adapter);
    }
}
