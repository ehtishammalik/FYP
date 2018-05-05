package com.example.android.rideshareplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.settings_rclr_view);
        ProfilelvAdapter adapter = new ProfilelvAdapter(this,setData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    public List<ProfileListviewItems> setData(){
        String [] eventnames = {"CNIC", "Driver's Licence", "Phone","Email"};
        int [] images = {R.drawable.ic_phone_white,R.drawable.ic_email_white_24dp_1x,R.drawable.ic_phone_white_24dp_1x,R.drawable.ic_email_white_24dp_2x};
        String add = "ADD";
        String verified = "Verified";
        List<ProfileListviewItems> data = new ArrayList<>();
        for (int i=0; i< eventnames.length;i++){
            ProfileListviewItems temp = new ProfileListviewItems();
            temp.setEventName(eventnames[i]);
            temp.setValue(add);
            temp.setIsVerified(verified);
            temp.setImageId(images[i]);
            data.add(temp);
        }
        return data;
    }
}
