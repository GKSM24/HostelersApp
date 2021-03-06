package com.example.hostelers.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hostelers.R;
import com.google.android.material.tabs.TabLayout;

public class BoarderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ViewPager.OnPageChangeListener  {
    private ViewPager boarderViewPager;
    private BoarderNotificationsFragment notificationsFragment;
    private BoarderIssuesFragment issuesFragment;
    private BoarderPaymentsFragment paymentsFragment;


    public BoarderActivity(){
        notificationsFragment = BoarderNotificationsFragment.newInstance();
        issuesFragment = BoarderIssuesFragment.newInstance();
        paymentsFragment = BoarderPaymentsFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarder);
        TabLayout tabs = findViewById(R.id.tabs_boarder);
        boarderViewPager = findViewById(R.id.viewpager_boarder);
        TextView name = findViewById(R.id.user_name_welcome_text), id = findViewById(R.id.user_id_text);
        ImageView photo = findViewById(R.id.user_photo);
        Spinner menu = findViewById(R.id.user_menu_spinner);
        Intent fromIntent = getIntent();
        id.setText("ID: "+ fromIntent.getStringExtra("boarderId"));
        name.setText("Welcome, "+ fromIntent.getStringExtra("boarderName"));
        photo.setImageBitmap(stringToImage(fromIntent.getStringExtra("boarderPhoto")));
        boarderViewPager.setOnPageChangeListener(this);
        menu.setOnItemSelectedListener(this);
        setViewPager(boarderViewPager);
        tabs.setupWithViewPager(boarderViewPager);
    }

    public void setViewPager(ViewPager viewPager){
        BoarderViewPagerAdapter adapter = new BoarderViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(notificationsFragment, "Notifications");
        adapter.addFragment(issuesFragment, "Issues");
        adapter.addFragment(paymentsFragment, "Payments");
        viewPager.setAdapter(adapter);
    }

    private Bitmap stringToImage(String encodedString){
        byte[] bytes = Base64.decode(encodedString, Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences prefObj = getSharedPreferences("BoarderUser", Context.MODE_PRIVATE);
        switch (position){
            case 1:
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                intent.putExtra("userType", "Boarder");
                intent.putExtra("id", prefObj.getString("BoarderId", null));
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
        //empty
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
