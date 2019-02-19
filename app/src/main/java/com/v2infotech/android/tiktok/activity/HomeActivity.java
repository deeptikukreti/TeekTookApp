package com.v2infotech.android.tiktok.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.Utils.BottomNavigationViewHelper;
import com.v2infotech.android.tiktok.camera.BaseCameraActivity;
import com.v2infotech.android.tiktok.camera.PortrateActivity;
import com.v2infotech.android.tiktok.fragment.DashboardFragment;
import com.v2infotech.android.tiktok.fragment.NotificationFragment;
import com.v2infotech.android.tiktok.fragment.ProfileFragment;
import com.v2infotech.android.tiktok.fragment.SearchFragment;
import com.v2infotech.android.tiktok.fragment.VideoFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = getSupportActionBar();

        // load the store fragment by default


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        loadFragment(new DashboardFragment());
        // toolbar.setTitle("Shop");
    }


    private boolean loadFragment(android.support.v4.app.Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                loadFragment(new DashboardFragment());
                return true;
            case R.id.navigation_dashboard:
                loadFragment(new SearchFragment());
                return true;
            case R.id.navigation_notifications:
                loadFragment(new NotificationFragment());
                return true;
            case R.id.navigation_profile:
                loadFragment(new ProfileFragment());
                return true;
            case R.id.navigation_video:
//               loadFragment(new VideoFragment());
                Intent intent = new Intent(HomeActivity.this, PortrateActivity.class);
                startActivity(intent);

                return true;
        }

        return false;
    }
}