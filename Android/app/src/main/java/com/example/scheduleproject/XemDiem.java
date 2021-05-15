package com.example.scheduleproject;

import android.content.Intent;
import android.os.Bundle;

import com.example.scheduleproject.Adapter.MainViewPagerAdapter;
import com.example.scheduleproject.Fragment.AccountFragment;
import com.example.scheduleproject.Fragment.HomeFragment;
import com.example.scheduleproject.Fragment.SettingFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

public class XemDiem extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int RLACTIVITY =1;
    private static final int HOMEFRAGMENT = 2;
    private static final int SETTINGFRAGMENT = 3;
    private static final int SUPPORTFRAGMENT = 4;
    private static final int XEMDIEM=5;
    private static final int LICHTHI=6;
    private static final int TKB=7;
    private static final int ACCOUNT=8;
    private int currentFragment = XEMDIEM;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_diem);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout_XD);
        NavigationView navigationView = findViewById(R.id.nav_view_XD);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

//        replaceFragment(new HomeFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_XD);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if (HOMEFRAGMENT != currentFragment) {
                this.finish();
                replaceFragment(new HomeFragment());
                currentFragment = HOMEFRAGMENT;
            }
        } else if (id == R.id.nav_setting) {
            //Open fragment setting
            if (SETTINGFRAGMENT != currentFragment) {
                this.finish();
                replaceFragment(new SettingFragment());
                currentFragment = SETTINGFRAGMENT;
            }
        } else if (id == R.id.nav_account) {
            //Open fragment setting
            if (ACCOUNT  != currentFragment) {
                this.finish();
                replaceFragment(new AccountFragment());
                currentFragment = ACCOUNT;
            }
        } else if (id == R.id.nav_Chat) {
            //Open fragment support
        }else if (id == R.id.nav_xem_diem){
            if (XEMDIEM != currentFragment){
                startActivity(new Intent(this,XemDiem.class));
            }
        }else if (id == R.id.nav_LichThi) {
            if (LICHTHI != currentFragment) {
                startActivity(new Intent(this, LichThi.class));
            }
        }else if (id == R.id.nav_TKB) {
            if (TKB != currentFragment) {
                startActivity(new Intent(this, TKB.class));

            }
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_XD);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame_XD,fragment);
        fragmentTransaction.commit();
    }
}