package com.example.scheduleproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.os.Bundle;

import com.example.scheduleproject.Adapter.MainViewPagerAdapter;
import com.example.scheduleproject.Fragment.AccountFragment;
import com.example.scheduleproject.Fragment.ChatFragment;
import com.example.scheduleproject.Fragment.HomeFragment;
import com.example.scheduleproject.Fragment.SettingFragment;
import com.example.scheduleproject.Fragment.TodoFragment;
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

    public void init() {
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mainViewPagerAdapter.addFragment(new ChatFragment(),"Chat");
        mainViewPagerAdapter.addFragment(new TodoFragment(),"To do");
        mainViewPagerAdapter.addFragment(new HomeFragment(),"Home");
        mainViewPagerAdapter.addFragment(new AccountFragment(),"Account");
        mainViewPagerAdapter.addFragment(new SettingFragment(),"Setting");
        viewPager.setAdapter(mainViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_chat);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_list__todo);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_account);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_settings);

        tabLayout.selectTab(tabLayout.getTabAt(2));
    }

}