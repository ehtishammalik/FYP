package com.example.android.rideshareplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class DriverRegistration extends AppCompatActivity {

    private EditText licence, vehicle;
    private TextView skip;
    private Button register;
    private Spinner dropdown;
    private String[] items = new String[]{"Hatchback", "Sedan", "MPV", "SUV", "Crossover", "Coupe","Convertible"};
    private ArrayAdapter <String> adapter;
    private Users user;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        licence = (EditText) findViewById(R.id.driver_licence);
        vehicle = (EditText) findViewById(R.id.vehicle_registration_number);

        skip = (TextView) findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(DriverRegistration.this,LoginActivity.class);
                startActivity(i);
            }
        });

        register = (Button) findViewById(R.id.reg_vehicle_Btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i  = new Intent(DriverRegistration.this,LoginActivity.class);
                startActivity(i);
            }
        });
        dropdown = (Spinner) findViewById(R.id.vehicle_type);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }
    protected void storeRecord() {
        user = new Users();
        user.setEmail(SignUp.email);
        user.setName(SignUp.name);
        user.setLicenceNo(licence.getText().toString());
        user.setRegNo(vehicle.getText().toString());
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

        reference.child(user.getEmail()).setValue(user);


    }
}
