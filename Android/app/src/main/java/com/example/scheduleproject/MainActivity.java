package com.example.scheduleproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.os.Bundle;

import com.example.scheduleproject.Adapter.MainViewPagerAdapter;
import com.example.scheduleproject.Fragment.AccountFragment;
import com.example.scheduleproject.Fragment.HomeFragment;
import com.example.scheduleproject.Fragment.SettingFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa();
        init();
    }
    private void anhxa() {
        tabLayout = findViewById(R.id.TapLayout_Home);
        viewPager = findViewById(R.id.ViewPagerHome);
    }

    private void init() {
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mainViewPagerAdapter.addFragment(new HomeFragment(),"Trang chủ");
        mainViewPagerAdapter.addFragment(new SettingFragment(),"Cài đặt");
        mainViewPagerAdapter.addFragment(new AccountFragment(),"Tài khoản");
        viewPager.setAdapter(mainViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_settings);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_account);

    }
}