package com.example.android.rideshareplanner;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class DriverRegistration extends AppCompatActivity {

    private EditText licenceNo, vehicleRegNo;
    private TextView skip;
    private Button register;
    private Spinner vehicleType, vehicleColor;
    private String[] types = new String[]{"Hatchback", "Sedan", "MPV", "SUV", "Crossover", "Coupe","Convertible"};
    private String[] colors = new String[]{"Red", "Green", "Black", "White", "Yellow", "Blue", "Orange", "Purple"};
    private ArrayAdapter <String> typeadapter, coloradapter;
    private Users user;
    FirebaseDatabase database;
    DatabaseReference reference;
    private int PICK_IMAGE_REQUEST = 101;
    private Uri imageUri;
    private ImageView vehicleImage;
    private StorageReference storageRef;
    public String imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        licenceNo = (EditText) findViewById(R.id.dr_driver_licence);
        vehicleRegNo = (EditText) findViewById(R.id.dr_vehicle_registration_number);
        vehicleImage = (ImageView) findViewById(R.id.dr_vehicle_image);
        vehicleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        skip = (TextView) findViewById(R.id.dr_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(DriverRegistration.this,PhoneVerification.class);
                startActivity(i);
            }
        });

        register = (Button) findViewById(R.id.dr_reg_vehicle_Btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upload();

            }
        });


        vehicleType = (Spinner) findViewById(R.id.dr_vehicle_type);
        typeadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        vehicleType.setAdapter(typeadapter);


        vehicleColor = (Spinner) findViewById(R.id.dr_vehicle_color);
        coloradapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colors);
        vehicleColor.setAdapter(coloradapter);
    }
    protected void storeRecord() {
//        user = new Users();
//        user.setEmail(SignUp.email);
//        user.setName(SignUp.name);
//        user.setDriverLicenceNo(licenceNo.getText().toString());
//        user.setDriverRegNo(vehicleRegNo.getText().toString());
//        database = FirebaseDatabase.getInstance();
//        reference = database.getReference("Users");
//
//        reference.child(user.getEmail()).setValue(user);


    }

    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                vehicleImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void upload() {
        if (imageUri != null) {
            storageRef = FirebaseStorage.getInstance().getReference().child("VPs").child(System.currentTimeMillis()+"."+getImageExt(imageUri));
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            imageUrl=taskSnapshot.getDownloadUrl().toString();
                            String driverLicence = licenceNo.getText().toString();
                            String RegNo = vehicleRegNo.getText().toString();
                            String vehicletype = vehicleType.getSelectedItem().toString();
                            String vehiclecolor = vehicleColor.getSelectedItem().toString();
                            SignUp.userData.setDriverLicenceNo(driverLicence);
                            VehicleDetails vehicleDetails = new VehicleDetails(vehicletype,vehiclecolor,RegNo,imageUrl);
                            SignUp.userData.setVehicleDetails(vehicleDetails);



                            Intent i  = new Intent(DriverRegistration.this,PhoneVerification.class);
                            startActivity(i);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(DriverRegistration.this,"Choose the Image Again" , Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }
}
