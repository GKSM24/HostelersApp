package com.example.hostelers.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hostelers.R;
import com.google.android.material.tabs.TabLayout;

public class WardenActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warden);
        Intent fromIntent = getIntent();
        TextView greeting_text_view = findViewById(R.id.user_name_welcome_text), id_text_view = findViewById(R.id.user_id_text);
        Spinner menu = findViewById(R.id.user_menu_spinner);
        id_text_view.setText("ID: "+ fromIntent.getStringExtra("warden_id"));
        greeting_text_view.setText("Welcome, "+ fromIntent.getStringExtra("warden_name"));
        menu.setOnItemSelectedListener(this);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences prefObj = getSharedPreferences("WardenUser", Context.MODE_PRIVATE);
        switch (position){
            case 1:
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                intent.putExtra("userType", "Warden");
                intent.putExtra("id", prefObj.getString("WardenId", null));
                intent.putExtra("hostelName", prefObj.getString("HostelName", null));
                intent.putExtra("hostelLocation", prefObj.getString("HostelLocation", null));
                startActivity(intent);
                break;
            case 2:
                SharedPreferences.Editor editor = prefObj.edit();
                editor.clear();
                editor.commit();
                finish();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
