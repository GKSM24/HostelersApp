package com.example.hostelers.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.hostelers.R;
import com.google.android.material.tabs.TabLayout;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        setViewPagerAdapter(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setViewPagerAdapter(ViewPager viewPager){
        SignUpAdapter adapter = new SignUpAdapter(getSupportFragmentManager());
        adapter.addFragment(FormFragment.newInstance(), "Form");
        adapter.addFragment(RulesFragment.newInstance(), "Rules");
        adapter.addFragment(IdProofsFragment.newInstance(), "Id Proofs");
        adapter.addFragment(PaymentFragment.newInstance(), "Payment");
        viewPager.setAdapter(adapter);
    }
}
