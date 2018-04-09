package com.example.android.rideshareplanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.example.android.rideshareplanner.SearchFragment;
import com.example.android.rideshareplanner.ShareFragment;
import com.example.android.rideshareplanner.CarpoolFragment;
import com.example.android.rideshareplanner.NotificationsFragment;
import com.example.android.rideshareplanner.ProfileFragment;

public class Main2Activity extends AppCompatActivity {
    public android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // load the store fragment by default
        toolbar.setTitle("Search");
        this.setTitle("Search");
        loadFragment(new SearchFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_search:

                    toolbar.setTitle("Search");
                    getSupportActionBar().show();
                    fragment = new SearchFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_share:
                    toolbar.setTitle("Share Ride");
                    getSupportActionBar().show();
                    fragment = new ShareFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_carpool:
                    toolbar.setTitle("My CarPools");
                    fragment = new CarpoolFragment();
                    getSupportActionBar().hide();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_notifications:
                    toolbar.setTitle("Notifications");
                    getSupportActionBar().show();
                    fragment = new NotificationsFragment();
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    getSupportActionBar().hide();
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
