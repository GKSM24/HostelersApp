package com.example.hostelers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

public class BoarderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarder);
        TabLayout tabs = findViewById(R.id.tabs_boarder);
        ViewPager boarderViewPager = findViewById(R.id.viewpager_boarder);
        setViewPager(boarderViewPager);
        tabs.setupWithViewPager(boarderViewPager);
    }

    public void setViewPager(ViewPager viewPager){
        BoarderAdapter adapter = new BoarderAdapter(getSupportFragmentManager());
        adapter.addFragment(BoarderNotificationsFragment.newInstance(), "Notifications");
        adapter.addFragment(BoarderIssuesFragment.newInstance(), "Issues");
        adapter.addFragment(BoarderPaymentsFragment.newInstance(), "Payments");
        viewPager.setAdapter(adapter);
    }
}
