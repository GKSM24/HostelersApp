package com.example.hostelers.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hostelers.R;
import com.google.android.material.tabs.TabLayout;

public class BoarderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarder);
        TabLayout tabs = findViewById(R.id.tabs_boarder);
        ViewPager boarderViewPager = findViewById(R.id.viewpager_boarder);
        TextView name = findViewById(R.id.user_name_welcome_text), id = findViewById(R.id.user_id_text);
        ImageView photo = findViewById(R.id.user_photo);
        Intent fromIntent = getIntent();
        id.setText(fromIntent.getStringExtra("boarderId"));
        name.setText(fromIntent.getStringExtra("boarderName"));
        photo.setImageBitmap(stringToImage(fromIntent.getStringExtra("boarderPhoto")));
        setViewPager(boarderViewPager);
        tabs.setupWithViewPager(boarderViewPager);
    }

    public void setViewPager(ViewPager viewPager){
        BoarderViewPagerAdapter adapter = new BoarderViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BoarderNotificationsFragment.newInstance(), "Notifications");
        adapter.addFragment(BoarderIssuesFragment.newInstance(), "Issues");
        adapter.addFragment(BoarderPaymentsFragment.newInstance(), "Payments");
        viewPager.setAdapter(adapter);
    }

    private Bitmap stringToImage(String encodedString){
        byte[] bytes = Base64.decode(encodedString, Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }
}
